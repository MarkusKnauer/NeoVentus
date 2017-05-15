
package de.neoventus.init;

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Dennis Thanner, Julian Beck, Markus Knauer, Tim Heidelbach
 * @version 0.0.B File import for MenuItem and MenuCategory (Code-reduction) - JB
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

	private static final int MAX_DESKS = 20;
	private static final int MAX_SEATS = 96;

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
		this.desks = new ArrayList<>();
		this.menuItems = new ArrayList<>();
		this.users = new ArrayList<>();
		clearData();
		clearIndexes();
		generateDesks(); // NoDBREF
		generateUser(); //No DBREF
		generateReservation(); // DBRef: User, Desk
		generateMenuItems(); // DBRef: Category, SideDishGroup
		generateSideDish(); // DbRef: MenuItem
		updateUserDesk();
		generateOrderItem(); //DBREF: All

	}

	/**
	 * add specified demo menu items to database
	 */
	private void generateMenuItems(){
		LOGGER.info("Init demo menu item data");
		try{
			// Get File
			URI uri = DefaultDemoDataIntoDB.class.getResource("menu.txt").toURI();
			Path p = Paths.get( uri );
			// builder[] for splitting Csv-divider result
			String builder[];
			MenuItemCategory child = null;
			MenuItem menuItem;
			// First Lines is without any information (Content: Menukarte)
			boolean menuCardDataLines = false;

			for(String line : Files.readAllLines(p, StandardCharsets.UTF_8)){

				if(menuCardDataLines) {
					// CSV-divider
					builder = line.split(";");

					if (builder[0].equals("Category")) {
						child = new MenuItemCategory(builder[2]);
						// if Parent-Category exists
						if (builder[1] != null) {
							child.setParent(menuItemCategoryRepository.findByName(builder[1]));
						}
						menuItemCategoryRepository.save(child);
					} else {
						// German decimal-sign to english
						if (builder[2].contains(",")){
							builder[2] = builder[2].replace(",", ".");
						}
						// Check if description is not available
						if(builder.length < 5 ){
							menuItem = new MenuItem(child,
								builder[0], // Name
								builder[1], // Short name
								Double.parseDouble(builder[2]), //Price
								builder[3], // Currency
								"", // Description
								"",
								new ArrayList<>());
						}else{
							menuItem = new MenuItem(child,
								builder[0], // Name
								builder[1], // Short name
								Double.parseDouble(builder[2]), //Price
								builder[3], // Currency
								builder[4], // Description
								"",
								new ArrayList<>());
						}

						menuItemRepository.save(menuItem);
					}
				} else{
					menuCardDataLines = true;
				}
			}

		} catch (IOException ioe){

		} catch (URISyntaxException urie){

		}


		// for (MenuItem m : menu) this.menuItems.add(menuItemRepository.save(m));
	}

	/**
	 * generate demo restaurant desks
	 */
	private void generateDesks() {
		LOGGER.info("Init demo restaurant desks");
		// NeoVentus Restaurant graphic
		this.desks.add(deskRepository.save(new Desk(10)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(4)));
		this.desks.add(deskRepository.save(new Desk(4)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(2)));
		this.desks.add(deskRepository.save(new Desk(10)));
		this.desks.add(deskRepository.save(new Desk(4)));
		this.desks.add(deskRepository.save(new Desk(4)));
		this.desks.add(deskRepository.save(new Desk(4)));
		this.desks.add(deskRepository.save(new Desk(4)));
		this.desks.add(deskRepository.save(new Desk(4)));
		this.desks.add(deskRepository.save(new Desk(4)));
		// Bar
		this.desks.add(deskRepository.save(new Desk(10)));
	}

	/**
	 * generate demo User
	 */
	private void generateUser() {
		LOGGER.info("Init demo User");
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		// todo: Teilzeit/Vollzeit Markierung
		// generate eight waiters
		this.users.add(userRepository.save(new User("Karl", "Karl", "Karlson", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Karmen", "Karmen", "Kernel", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Konsti", "Konstantin", "Kavos", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Kim", "Kimberley", "Klar", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Katha", "Katherina", "Keller", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Knut", "Knut", "Knutovic", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		// todo Teilzeit
		this.users.add(userRepository.save(new User("Kurt", "Kurt", "Kordova", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Katja", "Katja", "Klein", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));

		// chiefs (or chefs) with 'T' and todo Tibor is ONLY used for wash the dishes
		this.users.add(userRepository.save(new User("Tim", "Timothy", "Totenworth", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		this.users.add(userRepository.save(new User("Tibor", "Tibor", "Tarnomoglou", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		this.users.add(userRepository.save(new User("Tami", "Tamara", "Tanenbaum", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		this.users.add(userRepository.save(new User("Tati", "Tatajana", "Tovonoski", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		this.users.add(userRepository.save(new User("Tanar", "Tanar", "Tenkin", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		// Teilzeit
		this.users.add(userRepository.save(new User("Tobi", "Tobias", "Trottwar", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		//CEO
		this.users.add(userRepository.save(new User("Walter", "Walter", "Wald", bCryptPasswordEncoder.encode("walter"), Permission.CEO)));

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
		List<MenuItem> menuItem = new ArrayList<>();

		List<OrderItemState> states;
		OrderItemState state;
		// Only for Waiters
		users.parallelStream().forEach(user -> {
			if (user.getPermissions().contains(Permission.SERVICE)) waiter.add(user);
		});

		// Only selectable Categories
		MenuItemCategory category = menuItemCategoryRepository.findByName("Beilage - Std");
		menuItem = menuItemRepository.findAllByMenuItemCategoryIsNot(category);


		OrderItem ord;
		List<MenuItem> sideDishSelektion;
		MenuItem menu;
		Desk desk;
		User user;
		for (int i = 0; i < 50; i++) {
			sideDishSelektion = new ArrayList<MenuItem>();
			menu = menuItem.get((int) (Math.random() * menuItem.size()));
			user = waiter.get((int) (Math.random() * waiter.size()));
			desk = this.desks.get((int) (Math.random() * this.desks.size()));
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
		MenuItem menuItem;

		// ----------------------------- Pommes ------------------------------------------------
		sideDishGroup = saveSideDish("Pommesbeilage", "Mayonaisse", "Ketchup");
		saveMenuSideDishItem(sideDishGroup, "Chicken Nuggets", "Paniertes Schnitzel", "Großer Pommes-Teller", "Pommes frites", "Schnitzel “Wiener Art”");
		// ----------------------------- Eissorten ---------------------------------------------
		sideDishGroup = saveSideDish("Eissorten", "Vanilleeis", "Schokoladeneis", "Erdbeereis", "Walnußeis", "Zitroneneis");
		saveMenuSideDishItem(sideDishGroup, "Drei Kugeln Eis", "Chicken Nuggets", "Paniertes Schnitzel", "Großer Pommes-Teller");
		// ----------------------------- Kaffee ------------------------------------------------
		sideDishGroup = saveSideDish("Kaffee", "Kaffeesahne", "Milch");
		saveMenuSideDishItem(sideDishGroup, "Tasse Kaffee");
		// ----------------------------- Sprudel ------------------------------------------------
		sideDishGroup = saveSideDish("Sprudelwahl", "Saurer Sprudel", "Süßer Sprudel");
		saveMenuSideDishItem(sideDishGroup, "Rotwein", "Weißwein");
		// ----------------------------- Weinwahl -----------------------------------------------
		sideDishGroup = saveSideDish("Weinwahl", "Rotwein", "Weißwein");
		saveMenuSideDishItem(sideDishGroup, "Weinschorle");
		// ----------------------------- Apfelstrudel -------------------------------------------
		sideDishGroup = saveSideDish("Apfelstrudel", "Vanilleeis", "Schlagsahne");
		saveMenuSideDishItem(sideDishGroup, "warmer hausgemachter Apfelstrudel");
		// ----------------------------- Tee ----------------------------------------------------
		sideDishGroup = saveSideDish("Teesorten", "Schwarztee", "Grüntee", "Waldfruchttee", "Pfefferminztee");
		saveMenuSideDishItem(sideDishGroup, "Glas Tee");
		// ----------------------------- Spare-Rib ---------------------------------------------
		sideDishGroup = saveSideDish("Spare Ribs", "Baked Potato", "Pommes frites", "Rösti", "Krokettten", "Country-Kartoffeln", "Butterreis", "Red Beans", "Maiskolben vom Grill", "Frische Champignons", "Frische Sour Cream");
		saveMenuSideDishItem(sideDishGroup, "Spare Ribs 300g", "Spare Ribs 550g");
		// -------------------------------------------------------------------------------------

	}

	private SideDishGroup saveSideDish(String sidename, String... items) {
		SideDishGroup sideDishGroup = new SideDishGroup(sidename);
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
				desks.get((int) (Math.random() * desks.size())),
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
		deskRepository.deleteAll();
		menuItemRepository.deleteAll();
		userRepository.deleteAll();
		orderItemRepository.deleteAll();
		reservationRepository.deleteAll();
		menuItemCategoryRepository.deleteAll();
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
