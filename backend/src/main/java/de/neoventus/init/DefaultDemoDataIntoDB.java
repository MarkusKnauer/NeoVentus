
package de.neoventus.init;

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Dennis Thanner, Julian Beck, Markus Knauer, Tim Heidelbach
 * @version 0.0.C Write Default-Data Menu, Menucategories and user are written in 'WriteExcelInDB' from an Excelsheet - JB
 * 			0.0.B File import for MenuItem and MenuCategory (Code-reduction) - JB
 * 			0.0.A Add Desk-reservation and SideDishGroup Controll by not Selectable - JB
 * 			0.0.9 corrected spelling mistakes - DS
 * 			0.0.8 menu category fix - DT
 *          0.0.7 add SideDishGroup bsp-data - MK
 *          0.0.6 add menuItem Category bsp-data-generator -JB
 *          0.0.5 added random order items init - DT
 *          0.0.4 changed to new repositories
 *          0.0.3 Indirectly insert, Update, delete on DB (Class InsertUpdateDelete) +
 *          and repository access in ControlEntityObjects- JB
 *          0.0.2 Refactor default demo data in separate class
 **/

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
		//generateUser(); //No DBREF
		generateReservation(); // DBRef: User, Desk
		generateSideDish(); // DbRef: MenuItem
		updateUserDesk();
		generateOrderItem(); //DBREF: All

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
		List<User> waiter = new ArrayList<User>();
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
			sideDishSelektion = new ArrayList<MenuItem>();
			menu = menuItems.get((int) (Math.random() * menuItems.size()));
			user = waiter.get((int) (Math.random() * waiter.size()));
			desk = desks.get((int) (Math.random() * desks.size()));

			// For BI-Group new Timestemp
			states = new ArrayList<>();
			state = new OrderItemState(OrderItemState.State.NEW);
			Long millitime = System.currentTimeMillis() - ((long) (Math.random() * 7200000) - 7200000);
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

	/**
	 * clear before regenerate to allow changes
	 */
	private void clearData() {
		orderItemRepository.deleteAll();
		reservationRepository.deleteAll();
		sideDishRepository.deleteAll();
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
