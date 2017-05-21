
package de.neoventus.init;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.DBCollection;
import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;
import java.util.logging.Logger;


class DefaultDemoDataIntoDB {

	private final static Logger LOGGER = Logger.getLogger(DefaultDemoDataIntoDB.class.getName());
	private MenuItemRepository menuItemRepository;
	private MenuItemCategoryRepository menuItemCategoryRepository;
	private UserRepository userRepository;
	private DeskRepository deskRepository;
	private OrderItemRepository orderItemRepository;
	private BillingRepository billingRepository;
	private ReservationRepository reservationRepository;
	private SideDishRepository sideDishRepository;
	private MongoTemplate mongoTemplate;

	// init documents saved for class based access
	private List<Desk> desks;
	private List<MenuItem> menuItems;
	private List<User> users;

	public DefaultDemoDataIntoDB(DeskRepository deskRepository, UserRepository userRepository,
								 MenuItemRepository menuItemRepository, MenuItemCategoryRepository menuItemCategoryRepository, OrderItemRepository orderItemRepository,
								 ReservationRepository reservationRepository, BillingRepository billingRepository, SideDishRepository sideDishRepository, MongoTemplate mongoTemplate) {

		this.menuItemRepository = menuItemRepository;
		this.menuItemCategoryRepository = menuItemCategoryRepository;
		this.userRepository = userRepository;
		this.deskRepository = deskRepository;
		this.billingRepository = billingRepository;
		this.reservationRepository = reservationRepository;
		this.orderItemRepository = orderItemRepository;
		this.sideDishRepository = sideDishRepository;
		this.mongoTemplate = mongoTemplate;


		this.desks = deskRepository.findAll();
		this.menuItems = menuItemRepository.findAll();
		this.users = userRepository.findAll();

		clearData();
		clearIndexes();
		generateReservation(); // DBRef: User, Desk
		generateSideDish(); // DbRef: MenuItem
		updateUserDesk();
		generateFinishedOrderItem(); //DBREF: All
		generateActualOrderItem();
	}




	// ------------- START Semantic group: SideDishGroup -------------------------
	private void generateSideDish() {
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
		saveMenuSideDishItem(sideDishGroup, "Kleine Schorle", "Große Schorle");
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
			menuItem = menuItemRepository.findByName(s);
			menuItem.setSideDishGroup(sideDishGroup);
			menuItemRepository.save(menuItem);
		}

	}
	// ------------- END OF Semantic group: SideDishGroup ----------------------------
	public void generateReservation() {
		LOGGER.info("Generate Reservations");
		List<Permission> permissions = new ArrayList<Permission>();
		permissions.add(Permission.ADMIN);
		permissions.add(Permission.SERVICE);
		permissions.add(Permission.CEO);
		List<User> user = userRepository.findAllByPermissionsContaining(permissions);
		List<Reservation> listReservation = new ArrayList<>();
		Reservation reservation;

		for (int i = 0; i < 20; i++) {
			Long millitime = System.currentTimeMillis() - ((long) (Math.random() * 7200000) - 7200000);
			Date createdAt = new Date(millitime);
			millitime = System.currentTimeMillis() + ((long) (Math.random() * 345600000));//Reservation in the next 4 days
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



	// ------------- START OF Semantic group: BI- orders ------------------------------------
	private void generateFinishedOrderItem() {
		LOGGER.info("Creating random orders");
		List<OrderItem> orderItems = new ArrayList<>();
		List<User> waiter = new ArrayList<User>();
		List<MenuItem> menu = menuItemRepository.findAll();

		// Only for Waiters
		users.parallelStream().forEach(user -> {
			if (user.getPermissions().contains(Permission.SERVICE)) waiter.add(user);
		});

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
		Calendar calendar = new GregorianCalendar(2016,4,20,11,0,0);

		//for one Year
		for(int j = 0; j < 365; j++ ){
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
					for (int i = 0; i < ordersPerDesk; i++) {

						ord = new OrderItem(randomUserService(waiter),desks.get(k),randomMenuItem(menu),"");
						ord.setStates(setStates(calendar,delay,billingtime));
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


					billing = new Billing(new Date(calendar.getTimeInMillis()+delay+billingtime),totalprice,billingItemList);
					billing.setId(String.valueOf(new ObjectId()));
					billingList.add(billing);


					// waiter time (walk to next Desk) max. 6 mins - min. 1 min
					delay += Math.random()*300000+60000;
				}

			}
			// set next day
			calendar.set(2016,4,(calendar.get(3)+(j+1)));
		}


		LOGGER.info("Write OrderItem in DB");
		BulkOperations bulkOrders = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,OrderItem.class);
		bulkOrders.insert(orderItems);
		bulkOrders.execute();

		LOGGER.info("Write Billing in DB");
		BulkOperations bulkBillings = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,Billing.class);
		bulkBillings.insert(billingList);
		bulkBillings.execute();


		LOGGER.info("Update OrderItem in DB");
		DBCollection collection = mongoTemplate.getDb().getCollection("orderItem");
		BulkWriteOperation bulk = collection.initializeOrderedBulkOperation();
		for(Billing b: billingList){
			for(BillingItem bi: b.getItems()){

				BasicDBObject billingOb = new BasicDBObject();
				billingOb.put("$ref","billing");
				billingOb.put("$id", new ObjectId(b.getId()));
				bulk.find(
					new BasicDBObject("_id", new ObjectId(bi.getItem().getId())))
					.update(
						new BasicDBObject("$set", new BasicDBObject("billing",billingOb)
						)
				);
			}
		}
		bulk.execute();
		LOGGER.info("Finished creating random orders");
	}


	private List<OrderItemState> setStates(Calendar calendar, long delay,int billingtime){
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
	private User randomUserService(List<User> waiter){
		return waiter.get((int) (Math.random() * waiter.size()));
	}
	private MenuItem randomMenuItem(List<MenuItem> menuItems){
		return menuItems.get((int) (Math.random() * menuItems.size()));
	}


	// ------------- START OF Semantic group: Actual orders ------------------------------------
private void generateActualOrderItem() {
	LOGGER.info("Creating actual random orders");
	List<OrderItem> orderItems = new ArrayList<>();
	List<User> waiter = new ArrayList<User>();
	List<MenuItem> menu = menuItemRepository.findAll();

	// Only for Waiters
	users.parallelStream().forEach(user -> {
		if (user.getPermissions().contains(Permission.SERVICE)) waiter.add(user);
	});

	OrderItem ord;
	// Time variable in Millis
	long delay;
	//  BI - Starttime (11:00 because in Mongo it is 09:00)
	Calendar calendar = new GregorianCalendar(2017,Calendar.MONTH,Calendar.DAY_OF_MONTH,11,0,0);

	//for one Year
	for(int j = 0; j < 1; j++ ){
		// Time warp for other desks, 7 * 2h = 14 <-- max. Opening time
		for(int b = 0; b < 7; b++){
			// delay: random time after 30 min = 1800 sec. and 90 mins fix
			delay = (int)(b * Math.random() * 1800000+5400000);
			for(int k = 0; k < desks.size(); k++){
				// For Orders
				// orders per Desk
				int ordersPerDesk = (int)(Math.random() * 4)+4;
				for (int i = 0; i < ordersPerDesk; i++) {
					ord = new OrderItem(randomActualUserService(waiter),desks.get(k),randomActualMenuItem(menu),"");
					ord.setStates(setNewStates(calendar,delay));
					orderItems.add(ord);
				}
				// waiter time (walk to next Desk) max. 6 mins - min. 1 min
				delay += Math.random()*300000+60000;
			}

		}
		// set next day
		calendar.set(2017,4,(calendar.get(3)+(j+1)));
	}


	LOGGER.info("Write New-OrderItem in DB");
	BulkOperations bulkOrders = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,OrderItem.class);
	bulkOrders.insert(orderItems);
	bulkOrders.execute();
	LOGGER.info("Finished creating random orders");
}


	private List<OrderItemState> setNewStates(Calendar calendar, long delay){
		// For BI-Group new Timestemp
		List<OrderItemState> states = new ArrayList<>();

		OrderItemState state = new OrderItemState(OrderItemState.State.NEW);
		state.setDate(new Date(calendar.getTimeInMillis()+delay));

		states.add(state);

		return states;

	}
	private User randomActualUserService(List<User> waiter){
		return waiter.get((int) (Math.random() * waiter.size()));
	}
	private MenuItem randomActualMenuItem(List<MenuItem> menuItems){
		return menuItems.get((int) (Math.random() * menuItems.size()));
	}

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

}
