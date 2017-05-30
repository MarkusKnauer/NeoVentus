
package de.neoventus.init;

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Dennis Thanner, Julian Beck, Markus Knauer, Tim Heidelbach
 **/

class DefaultDemoDataIntoDB {

	private static final Logger LOGGER = Logger.getLogger(DefaultDemoDataIntoDB.class.getName());
	private static final int DAYS_FOUR = 345600000;
	private static final int HOURS_TWO = 7200000;

	private MenuItemRepository menuItemRepository;
	private MenuItemCategoryRepository menuItemCategoryRepository;
	private UserRepository userRepository;
	private DeskRepository deskRepository;
	private OrderItemRepository orderItemRepository;
	private BillingRepository billingRepository;
	private ReservationRepository reservationRepository;
	private SideDishRepository sideDishRepository;
	private MongoTemplate mongoTemplate;
	private final WorkingPlanRepository workingPlanRepository;

	// init documents saved for class based access
	private List<Desk> desks;
	private List<MenuItem> menuItems;
	private List<User> users;

	private Random random;
	private List<OrderItem> orderItems = null;

	DefaultDemoDataIntoDB(DeskRepository deskRepository, UserRepository userRepository,
								 MenuItemRepository menuItemRepository, MenuItemCategoryRepository menuItemCategoryRepository, OrderItemRepository orderItemRepository,
								 ReservationRepository reservationRepository, BillingRepository billingRepository, SideDishRepository sideDishRepository, MongoTemplate mongoTemplate,WorkingPlanRepository workingPlanRepository) {

		random = new Random();

		this.menuItemRepository = menuItemRepository;
		this.menuItemCategoryRepository = menuItemCategoryRepository;
		this.userRepository = userRepository;
		this.deskRepository = deskRepository;
		this.billingRepository = billingRepository;
		this.reservationRepository = reservationRepository;
		this.orderItemRepository = orderItemRepository;
		this.sideDishRepository = sideDishRepository;
		this.mongoTemplate = mongoTemplate;
		this.workingPlanRepository = workingPlanRepository;
		clearIndexes();
		this.desks = deskRepository.findAll();
		this.menuItems = menuItemRepository.findAll();
		this.users = userRepository.findAll();

		List<Workingplan> wp = (List<Workingplan>) workingPlanRepository.findAll();

		List<MenuItemCategory> menuItemCategories = (List<MenuItemCategory>) menuItemCategoryRepository.findAll();
		List<MenuItem> menuItems = menuItemRepository.findAll();

		if (checkLists(menuItems)){
			this.sideDishRepository.deleteAll();
			generateSideDish();
		}
		if(checkLists(users)&&checkLists(desks)){
			this.reservationRepository.deleteAll();
			generateReservation();
		}
		if (checkLists(users)&&checkLists(desks)&&checkLists(wp)&&checkLists(menuItems)){
			this.billingRepository.deleteAll();
			this.orderItemRepository.deleteAll();
			generateFinishedOrderItem();
			generateActualOrderItem();
		}
		//	generateReservation(); // DBRef: User, Desk
	//	generateSideDish(); // DbRef: MenuItem
	//	updateUserDesk();
	//	generateFinishedOrderItem(); //DBREF: All
	//	generateActualOrderItem();
	//	generateOrderItem(); //DBREF: All
	//	generateBillings();

	}
	private boolean checkLists(List<?> list){
		return list != null && !list.isEmpty();
	}

	private static double round(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	// DANGER! Here must be Parametres in use for dynamic assignment
	private void updateUserDesk() {
		User u = userRepository.findByUsername("Karl");
		u.getDesks().add(deskRepository.findByNumber(1));
		u.getDesks().add(deskRepository.findByNumber(2));
		u.getDesks().add(deskRepository.findByNumber(3));
		u.getDesks().add(deskRepository.findByNumber(4));
		userRepository.save(u);

		u = userRepository.findByUsername("Katja");
		u.getDesks().add(deskRepository.findByNumber(5));
		u.getDesks().add(deskRepository.findByNumber(6));
		u.getDesks().add(deskRepository.findByNumber(7));
		userRepository.save(u);

		u = userRepository.findByUsername("Knut");
		u.getDesks().add(deskRepository.findByNumber(8));
		u.getDesks().add(deskRepository.findByNumber(9));
		u.getDesks().add(deskRepository.findByNumber(10));
		userRepository.save(u);

	}

	private void generateOrderItem() {
		LOGGER.info("Creating random orders");
		List<OrderItem> orderItems = new ArrayList<>();
		List<User> waiter = new ArrayList<>();
		List<OrderItemState> states;
		OrderItemState state;
		// Only for Waiters
		users.parallelStream().forEach(user -> {
			if (user.getPermissions().contains(Permission.SERVICE)) waiter.add(user);
		});


		OrderItem ord;
		List<MenuItem> sideDishSelektion;
		MenuItem menu;
		Desk desk;
		User user;
		for (int i = 0; i < 50; i++) {
			sideDishSelektion = new ArrayList<>();
			menu = menuItems.get((int) (Math.random() * menuItems.size()));
			user = waiter.get((int) (Math.random() * waiter.size()));
			desk = desks.get((int) (Math.random() * desks.size()));

			// For BI-Group new Timestemp
			states = new ArrayList<>();
			state = new OrderItemState(OrderItemState.State.NEW);
			Long millitime = System.currentTimeMillis() - ((long) (Math.random() * HOURS_TWO) - HOURS_TWO);
			state.setDate(new Date(millitime));
			states.add(state);

			// Check if Menuitem has selectable SideDishGroup options
			sideDishSelektion.add(menu);
			while (menu.getSideDishGroup() != null) {
				menu = menu.getSideDishGroup().getSideDishes().get((int) (Math.random() * menu.getSideDishGroup().getSideDishes().size()));
				sideDishSelektion.add(menu);
			}

			for (MenuItem m : sideDishSelektion) {
				ord = new OrderItem(user, desk, m, "");
				ord.setStates(states);
				orderItems.add(ord);
			}
		}

		orderItemRepository.save(orderItems);
		LOGGER.info("Finished creating random orders");
	}

	// ------------- START Semantic group: SideDishGroup -------------------------
	public void generateSideDish() {
		LOGGER.info("Creating Sidedishes");
		SideDishGroup sideDishGroup;

		// ----------------------------- Pommes ------------------------------------------------
		// sideDishGroup = saveSideDish("Pommesbeilage", "Mayonaisse", "Ketchup");
		// saveMenuSideDishItem(sideDishGroup, "Chicken Nuggets", "Paniertes Schnitzel", "Großer Pommes-Teller", "Pommes frites", "Schnitzel “Wiener Art”");
		// ----------------------------- Eissorten ---------------------------------------------
		// sideDishGroup = saveSideDish("Eissorten", "Vanilleeis", "Schokoladeneis", "Erdbeereis", "Walnußeis", "Zitroneneis");
		// saveMenuSideDishItem(sideDishGroup, "Drei Kugeln Eis", "Chicken Nuggets", "Paniertes Schnitzel", "Großer Pommes-Teller");
		// ----------------------------- Kaffee ------------------------------------------------
		// sideDishGroup = saveSideDish("Kaffee", "Kaffeesahne", "Milch");
		// saveMenuSideDishItem(sideDishGroup, "Tasse Kaffee");
		// ----------------------------- Sprudel ------------------------------------------------
		// sideDishGroup = saveSideDish("Sprudelwahl", "Saurer Sprudel", "Süßer Sprudel");
		// saveMenuSideDishItem(sideDishGroup, "Rotwein", "Weißwein");
		// ----------------------------- Weinwahl -----------------------------------------------
		// sideDishGroup = saveSideDish("Weinwahl", "Rotwein", "Weißwein");
		// saveMenuSideDishItem(sideDishGroup, "Weinschorle");
		// ----------------------------- Apfelstrudel -------------------------------------------
		// sideDishGroup = saveSideDish("Apfelstrudel", "Vanilleeis", "Schlagsahne");
		// saveMenuSideDishItem(sideDishGroup, "warmer hausgemachter Apfelstrudel");
		// ----------------------------- Tee ----------------------------------------------------
		// sideDishGroup = saveSideDish("Teesorten", "Schwarztee", "Grüntee", "Waldfruchttee", "Pfefferminztee");
		// saveMenuSideDishItem(sideDishGroup, "Glas Tee");
		// ----------------------------- Spare-Rib ---------------------------------------------
		sideDishGroup = saveSideDish("Spare Ribs", false, "Baked Potato", "Pommes frites", "Rösti", "Krokettten", "Country-Kartoffeln", "Butterreis", "Red Beans", "Maiskolben vom Grill", "Frische Champignons", "Frische Sour Cream");
		saveMenuSideDishItem(sideDishGroup, "Spare Ribs 300g", "Spare Ribs 550g");
		// -------------------------------------------------------------------------------------
		sideDishGroup = saveSideDish("Schorle", true, "Apfelsaft", "Orangensaft", "Multivitaminsaft", "Tomatensaft");
		saveMenuSideDishItem(sideDishGroup, "Kleine Saftschorle", "Große Saftschorle");
		// -------------------------------------------------------------------------------------
		sideDishGroup = saveSideDish("Wein-Schorle", true, "Cabernet Sauvignon Weiß", "Cabernet Sauvignon Rot");
		saveMenuSideDishItem(sideDishGroup, "Weinschorle - süß", "Weinschorle - sauer");
		// -------------------------------------------------------------------------------------
	}

	private SideDishGroup saveSideDish(String sidename, Boolean selectionRequired, String... items) {
		SideDishGroup sideDishGroup = new SideDishGroup(sidename, selectionRequired);
		this.sideDishRepository.save(sideDishGroup);
		for (String i : items) {
			sideDishGroup.addSideDish(menuItemRepository.findByName(i));
		}
		this.sideDishRepository.save(sideDishGroup);
		return sideDishGroup;
	}

	private void saveMenuSideDishItem(SideDishGroup sideDishGroup, String... menunames) {
		MenuItem menuItem;
		for (String s : menunames) {
			try {
				menuItem = menuItemRepository.findByName(s);
				menuItem.setSideDishGroup(sideDishGroup);
				menuItemRepository.save(menuItem);
			} catch (NullPointerException e) {
				LOGGER.warning("NPE when saving menu side dish item");
				// FIXME: DANGER!! - NPE is thrown when submitting excel sheet.
			}
		}

	}

	// ------------- END OF Semantic group: SideDishGroup ----------------------------
	public void generateReservation() {
		LOGGER.info("Generate Reservations");
		List<Permission> permissions = new ArrayList<>();
		permissions.add(Permission.ADMIN);
		permissions.add(Permission.SERVICE);
		permissions.add(Permission.CEO);
		List<User> user = userRepository.findAllByPermissionsContaining(permissions);
		List<Reservation> listReservation = new ArrayList<>();
		Reservation reservation;

		for (int i = 0; i < 20; i++) {
			Long millitime = System.currentTimeMillis() - ((long) (Math.random() * HOURS_TWO) - HOURS_TWO);
			Date createdAt = new Date(millitime);
			millitime = System.currentTimeMillis() + ((long) (Math.random() * DAYS_FOUR));//Reservation in the next 4 days
			Date time = new Date(millitime);
			reservation = new Reservation(
				user.get((int) (Math.random() * user.size())),
				this.desks.get((int) (Math.random() * this.desks.size())),
				createdAt,
				time,
				30);
			listReservation.add(reservation);
		}
		reservationRepository.save(listReservation);

	}

	private void generateBillings() {

		double minX = 0.49f;
		double maxX = 13.37f;

		for (int i = 0; i < 20; i++) {

			Billing billing = new Billing();

			Date billedAt = new Date(System.currentTimeMillis() - ((long) (Math.random() * DAYS_FOUR) - DAYS_FOUR));
			billing.setBilledAt(billedAt);

			billing.setWaiter(userRepository.findByUsername("Karl"));

			List<BillingItem> billingItems = new ArrayList<>();
			double totalPaid = 0f;
			for (int j = 0; j < 3; j++) {
				BillingItem billingItem = new BillingItem();
				billingItem.setItem(getRandomOrderItem());
				Double price = round((maxX - minX) * random.nextDouble() * minX);
				billingItem.setPrice(price);
				totalPaid += price;
				billingItems.add(billingItem);
			}
			totalPaid += 2; // gratuity
			billing.setTotalPaid(totalPaid);
			billing.setItems(billingItems);

			billingRepository.save(billing);
		}
	}

//------------------- START OF Semantic group: BI- orders ------------------------------------
	// Finished Orders
	public void generateFinishedOrderItem() {
		LOGGER.info("Creating random orders");
		List<OrderItem> orderItems = new ArrayList<>();
		List<MenuItem> menu = menuItemRepository.findAll();

		OrderItem ord;
		// Billing variables
		BillingItem billingItem;
		List<BillingItem> billingItemList;
		List<Billing> billingList = new ArrayList<>();
		Billing billing;
		double totalprice;

		// Time variable in Millis
		long delay;
		//  BI - Starttime (11:00 because in Mongo it is 09:00)
	//Find first and Last
		Query query = new Query();
		query.limit(1);
		query.with(new Sort(Sort.Direction.ASC, "lastModifiedDate"));
		Workingplan plan =mongoTemplate.findOne(query, Workingplan.class);
		query = new Query();
		query.limit(1);
		query.with(new Sort(Sort.Direction.DESC, "lastModifiedDate"));
		Workingplan lastPlan =mongoTemplate.findOne(query, Workingplan.class);
		if(lastPlan.getCreatedPlan().compareTo(new Date(System.currentTimeMillis()))>0) return;
		LOGGER.info(plan.toString());
		Date calendar = plan.getWorkingDay();
		calendar.setHours(11);
		calendar.setMinutes(0);
		// one week
		//for (int j = 0; j < 7; j++) {
	int j = 0;
		while(
			plan!= null&&
			lastPlan.getCreatedPlan().compareTo(new Date(System.currentTimeMillis()))<1&&
			!lastPlan.equals(plan)){
			// Time warp for other desks, 7 * 2h = 14 <-- max. Opening time
			for(int b = 0; b < 7; b++){
				// delay: random time after 30 min = 1800 sec. and 90 mins fix
				delay = (int)(b * Math.random() * 1800000+5400000);
				for(int k = 0; k < desks.size(); k++){
				// For Orders
					billingItemList = new ArrayList<BillingItem>();
					totalprice = 0;
					// orders per Desk
					int ordersPerDesk = (int)(Math.random() * 4)+4;
					// Billing after 30 mins + Randomvalue 20 mins - Max. 50 mins - Min. 30 mins
					int billingtime = (int)(Math.random()*1200000+1800000);
					billing = new Billing(new Date(calendar.getTime()+delay+billingtime), (double) 0,billingItemList, randomUserService((calendar.getTime()+delay+billingtime), plan.getWorkingshift()));
					billing.setId(String.valueOf(new ObjectId()));
					for (int i = 0; i < ordersPerDesk; i++) {

						ord = new OrderItem(randomUserService(calendar.getTime()+delay, plan.getWorkingshift()),desks.get(k),randomMenuItem(menu),"");
						ord.setStates(setStates(calendar,delay,billingtime));
						ord.setBilling(billing);
						double price = ord.getItem().getPrice();
						totalprice += price;
						ord.setId(String.valueOf(new ObjectId()));
						billingItem = new BillingItem(ord,price);
						billingItemList.add(billingItem);
						orderItems.add(ord);
					}
					// 10-15% Drinking-cash
					totalprice += totalprice*(Math.random()*5+10)/100;
					totalprice = (Math.round(totalprice*10))/10;
					billing.setTotalPaid(totalprice);
					billingList.add(billing);


					// waiter time (walk to next Desk) max. 6 mins - min. 1 min
					delay += Math.random()*300000+60000;
				}

			}
			// set next day
			calendar.setDate(calendar.getDate()+1);
			j++;
			plan = findWorkingplan(calendar);

		}

		// Bulk-Insert + Bulk-Update
		insertOrders(orderItems);
		insertBilling(billingList);

		//updateBillingReferenceInOrders(billingList);

		LOGGER.info("Finished creating random orders");
	}

	//  Actual orders
	public void generateActualOrderItem() {
	LOGGER.info("Creating actual random orders");
	List<OrderItem> orderItems = new ArrayList<>();
	List<MenuItem> menu = menuItemRepository.findAll();

	OrderItem ord;
	// Time variable in Millis
	long delay;
	//  BI - Starttime (11:00 because in Mongo it is 09:00)
		Query query = new Query();
		query.limit(1);
		query.with(new Sort(Sort.Direction.DESC, "lastModifiedDate"));
		Workingplan plan =mongoTemplate.findOne(query, Workingplan.class);

		if(plan.getCreatedPlan().compareTo(new Date(System.currentTimeMillis()))>0) return;
	Date calendar = plan.getWorkingDay();
		calendar.setHours(11);
		calendar.setMinutes(0);

		//for one Year

	for(int j = 0; j < 1; j++ ){
		// Time warp for other desks, 7 * 2h = 14 <-- max. Opening time
		for(int b = 0; b < 1; b++){
			// delay: random time after 30 min = 1800 sec. and 90 mins fix
			delay = (int)(b * Math.random() * 1800000+5400000);
			for(int k = 0; k < desks.size()-1; k++){
				// For Orders
				// orders per Desk
				int ordersPerDesk = (int)(Math.random() * 4)+4;
				for (int i = 0; i < ordersPerDesk; i++) {
					ord = new OrderItem(randomUserService((calendar.getTime()+delay), plan.getWorkingshift()),desks.get(k),randomMenuItem(menu),"");
					ord.setStates(setNewStates(calendar,delay));
					orderItems.add(ord);
				}
				// waiter time (walk to next Desk) max. 6 mins - min. 1 min
				delay += Math.random()*300000+60000;
			}

		}
		// set next day
		calendar.setDate(calendar.getDay()+1);
		plan = findWorkingplan(calendar);
	}
	// Bulk-Insert
	insertOrders(orderItems);

	LOGGER.info("Finished creating random orders");
}

	// *******************************************************************************
	// ********************* Orderitem-Help-Functions ********************************
	/**
	 * MenuItem , WorkingPlan, User(in direction of Workingplan), TimeStates
	 *
	 *	Inserts / Updates
	 */
	private Workingplan findWorkingplan(Date calendar){
		Date date1 = calendar;
		Date date2 = calendar;
		Query query = new Query();
		date1.setHours(0);
		date1.setMinutes(0);
		date2.setMinutes(0);
		date2.setHours(26);
		query.addCriteria(Criteria.where("workingDay").gte(date1).lt(date2));
		return mongoTemplate.findOne(query,Workingplan.class);
	}
	private List<OrderItemState> setStates(Date calendar, long delay, int billingtime){
	// For BI-Group new Timestemp
	List<OrderItemState> states = new ArrayList<>();

	OrderItemState state = new OrderItemState(OrderItemState.State.NEW);
	state.setDate(new Date(calendar.getTime()+delay));

	states.add(state);

	OrderItemState finished= new OrderItemState(OrderItemState.State.FINISHED);
	finished.setDate(new Date(calendar.getTime()+delay+billingtime));

	states.add(finished);

	return states;

}
	private User randomUserService(long delay, List<Workingshift> workingshifts){
		Date date = new Date(delay);
		List<User> user = new ArrayList<>();
		for(Workingshift shift : workingshifts){
			if(date.after(shift.getStartShift())&& date.before(shift.getEndShift())){
				user.add(shift.getUser());
			}
		}
		return user.get((int) (Math.random() * user.size()));
	}
	private MenuItem randomMenuItem(List<MenuItem> menuItems){
		return menuItems.get((int) (Math.random() * menuItems.size()));
	}
	private List<OrderItemState> setNewStates(Date calendar, long delay){
		// For BI-Group new Timestemp
		List<OrderItemState> states = new ArrayList<>();

		OrderItemState state = new OrderItemState(OrderItemState.State.NEW);
		state.setDate(new Date(calendar.getTime()+delay));

		states.add(state);

		return states;

	}
	//
	private void insertOrders(List<OrderItem> orderItems){
		LOGGER.info("Write New-OrderItem in DB");
		BulkOperations bulkOrders = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,OrderItem.class);
		bulkOrders.insert(orderItems);
		bulkOrders.execute();
	}
	private void insertBilling(List<Billing> billingList){
		LOGGER.info("Write Billing in DB");
		BulkOperations bulkBillings = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,Billing.class);
		bulkBillings.insert(billingList);
		bulkBillings.execute();

	}
	// *******************************************************************************
	// *******************************************************************************
// --------------------- END of OrderItem-functions ----------------------------------------

	/**
	 * clear before regenerate to allow changes
	 */
	private void clearData() {
		orderItemRepository.deleteAll();
		reservationRepository.deleteAll();
		sideDishRepository.deleteAll();
		billingRepository.deleteAll();
	}
	/**
	 * clear existing indexes
	 */
	private void clearIndexes() {
		for (String collection : this.mongoTemplate.getDb().getCollectionNames()) {
			this.mongoTemplate.getDb().getCollection(collection).dropIndexes();
		}
	}

	private OrderItem getRandomOrderItem() {
		if (orderItems == null) {
			orderItems = (List<OrderItem>) orderItemRepository.findAll();
		}
		return orderItems.get(random.nextInt(orderItems.size()));
	}
}
