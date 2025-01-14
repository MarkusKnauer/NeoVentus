
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
import java.util.*;
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
	private List<SideDishGroup> allSideDishGroup;

	private Random random;
	private List<OrderItem> orderItems = null;

	DefaultDemoDataIntoDB(boolean allNew, DeskRepository deskRepository, UserRepository userRepository,
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


		if (checkLists(menuItems)){
			generateSideDish();
		}
		if(checkLists(users)&&checkLists(desks)){
			if(allNew){
				generateReservation();
			}

		}
		if (checkLists(users)&&checkLists(desks)&&checkLists(wp)&&checkLists(menuItems)){
			if(allNew){
				generateFinishedOrderItem();
				generateActualOrderItem();
			}
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
		allSideDishGroup = (List<SideDishGroup>) this.sideDishRepository.findAll();
		if(allSideDishGroup != null){
			for( SideDishGroup sdg: allSideDishGroup){
				sdg.setActiveItem(false);
			}
		}
		// -------------------------------------------------------------------------------------
		sideDishGroup = saveSideDish("Softdrink - klein", true, "Coca Cola", "Coca Cola light", "Fanta Orange", "Cola Mix");
		saveMenuSideDishItem(sideDishGroup, "Softdrinks klein");
		// -------------------------------------------------------------------------------------
		sideDishGroup = saveSideDish("Spare Ribs", false, "Baked Potato", "Pommes frites", "Rösti", "Kroketten", "Country-Kartoffeln", "Butterreis", "Red Beans", "Maiskolben vom Grill", "Frische Champignons", "Frische Sour Cream");
		saveMenuSideDishItem(sideDishGroup, "Spare Ribs 300g", "Spare Ribs 550g");
		// -------------------------------------------------------------------------------------
		sideDishGroup = saveSideDish("Schorle", true, "Apfelsaft", "Orangensaft", "Multivitaminsaft", "Tomatensaft");
		saveMenuSideDishItem(sideDishGroup, "Kleine Saftschorle", "Große Saftschorle");
		// -------------------------------------------------------------------------------------
		sideDishGroup = saveSideDish("Wein-Schorle", true, "Cabernet Sauvignon Weiß", "Cabernet Sauvignon Rot");
		saveMenuSideDishItem(sideDishGroup, "Weinschorle - süß", "Weinschorle - sauer");
		// -------------------------------------------------------------------------------------
		LOGGER.info("FINISHED SideDishes");
	}

	private SideDishGroup sideDishFindByName(String value){
		if(allSideDishGroup != null){
			for( SideDishGroup sdg: allSideDishGroup){
				if(sdg.getName().equals(value)){
					return sdg;
				}
			}
		}
		return null;
	}
	private SideDishGroup saveSideDish(String sidename, Boolean selectionRequired, String... items) {
		SideDishGroup sideDishGroup;
		if(sideDishFindByName(sidename) != null){
			sideDishGroup = sideDishFindByName(sidename);
			sideDishGroup.setActiveItem(true);

			for (String i : items) {
				MenuItem tmp = menuItemRepository.findByName(i);
				if(sideDishGroup.getSideDishes() != null){
					if(sideDishGroup.getSideDishes().contains(tmp)){
						sideDishGroup.addSideDish(tmp);
					}
				} else{
					sideDishGroup.addSideDish(tmp);
				}
			}
		}else {
			sideDishGroup = new SideDishGroup(sidename, selectionRequired);
			this.sideDishRepository.save(sideDishGroup);
			for (String i : items) {
				MenuItem tmp = menuItemRepository.findByName(i);
				sideDishGroup.addSideDish(tmp);
			}
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
		query.with(new Sort(Sort.Direction.ASC, "workingDay"));
		List<Workingplan> wp = mongoTemplate.find(query, Workingplan.class);
		Workingplan plan = wp.get(0);
		Workingplan lastPlan = wp.get(wp.size()-1);
		if(lastPlan.getCreatedPlan().compareTo(new Date(System.currentTimeMillis()))>0) return;
		Calendar calendar = new GregorianCalendar(plan.getWorkingDay().getYear()+1900,plan.getWorkingDay().getMonth(),plan.getWorkingDay().getDate(),10,0);

		// one week
		//for (int j = 0; j < 7; j++) {
		int j = 0;
		// Day Counter, if totalDay = 0 <- write all
		int totalDay = 365;
		Calendar today = getCalendar(calendar,totalDay);
		LOGGER.info(lastPlan.getWorkingDay()+ " Lastday");
		LOGGER.info(today.getTime()+ " getTime");
		while(
			plan!= null&&
				plan.getWorkingDay().compareTo(today.getTime())<1&&
			lastPlan.getWorkingDay().compareTo(today.getTime())>0&&
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
					billing = new Billing(new Date(calendar.getTimeInMillis()+delay+billingtime), (double) 0,billingItemList, randomUserService((calendar.getTimeInMillis()+delay+billingtime), plan.getWorkingshift()));
					billing.setId(String.valueOf(new ObjectId()));
					for (int i = 0; i < ordersPerDesk; i++) {

						ord = new OrderItem(randomUserService(calendar.getTimeInMillis()+delay, plan.getWorkingshift()),desks.get(k),randomMenuItem(menu),"");
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
			calendar = getCalendar(calendar, 1);
			j++;
			plan = findWorkingplan(calendar);
		}

		// Bulk-Insert + Bulk-Update
		if(orderItems != null && !orderItems.isEmpty()){
			insertOrders(orderItems);
			insertBilling(billingList);
		}
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
		Workingplan plan = findWorkingplan(new GregorianCalendar());
		if(plan == null || plan.getCreatedPlan().compareTo(new Date(System.currentTimeMillis()))>0) return;
		Calendar calendar = new GregorianCalendar(plan.getWorkingDay().getYear()+1900,plan.getWorkingDay().getMonth(),plan.getWorkingDay().getDate(),11,0);


		// Time warp for other desks, 7 * 2h = 14 <-- max. Opening time
		for(int b = 0; b < 1; b++){
			// delay: random time after 30 min = 1800 sec. and 90 mins fix
			delay = (int)(b * Math.random() * 1800000+5400000);
			for(int k = 0; k < desks.size()-1; k++){
				// For Orders
				// orders per Desk
				int ordersPerDesk = (int)(Math.random() * 4)+4;
				for (int i = 0; i < ordersPerDesk; i++) {
					ord = new OrderItem(randomUserService((calendar.getTimeInMillis()+delay), plan.getWorkingshift()),desks.get(k),randomMenuItem(menu),"");
					ord.setStates(setNewStates(calendar.getTime(),delay));
					orderItems.add(ord);
				}
				// waiter time (walk to next Desk) max. 6 mins - min. 1 min
				delay += Math.random()*300000+60000;
			}

		}

		// Bulk-Insert + Bulk-Update
		if(orderItems != null && !orderItems.isEmpty()) {
			insertOrders(orderItems);
		}
	LOGGER.info("Finished creating random orders");
}

	// *******************************************************************************
	// ********************* Orderitem-Help-Functions ********************************
	/**
	 * MenuItem , WorkingPlan, User(in direction of Workingplan), TimeStates
	 *
	 *	Inserts / Updates
	 */
	private GregorianCalendar getCalendar(Calendar calendar, int day){

		if(day==0) return new GregorianCalendar();

		return new GregorianCalendar(
			calendar.get(Calendar.YEAR),
			calendar.get(Calendar.MONTH),
			calendar.get(Calendar.DAY_OF_MONTH) + day,
			calendar.get(Calendar.HOUR_OF_DAY),
			calendar.get(Calendar.MINUTE)
		);
	}
	private Workingplan findWorkingplan(Calendar calendar){
		Date date1 = calendar.getTime();
		Date date2 = calendar.getTime();
		Query query = new Query();
		date1.setHours(0);
		date1.setMinutes(0);
		date2.setMinutes(0);
		date2.setHours(26);
		query.addCriteria(Criteria.where("workingDay").gte(date1).lt(date2));
		return mongoTemplate.findOne(query,Workingplan.class);
	}
	private List<OrderItemState> setStates(Calendar calendar, long delay, int billingtime){
	// For BI-Group new Timestemp
	List<OrderItemState> states = new ArrayList<>();

	OrderItemState state = new OrderItemState(OrderItemState.State.NEW);
	state.setDate(new Date(calendar.getTimeInMillis()+delay));

	states.add(state);

	OrderItemState finished= new OrderItemState(OrderItemState.State.FINISHED);
	finished.setDate(new Date(calendar.getTimeInMillis()+delay+billingtime));

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
		LOGGER.info("Write New-OrderItem in DB ");
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
