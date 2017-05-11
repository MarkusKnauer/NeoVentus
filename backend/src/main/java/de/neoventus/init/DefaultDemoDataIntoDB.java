
package de.neoventus.init;

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Dennis Thanner, Julian Beck, Markus Knauer, Tim Heidelbach
 * @version 0.0.8 menu category fix - DT
 *          0.0.7 add SideDish bsp-data - MK
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


	// init documents saved for class based access
	private List<Desk> desks;
	private List<MenuItem> menuItems;
	private List<User> users;

	public DefaultDemoDataIntoDB(DeskRepository deskRepository, UserRepository userRepository,
								 MenuItemRepository menuItemRepository, MenuItemCategoryRepository menuItemCategoryRepository, OrderItemRepository orderItemRepository,
								 ReservationRepository reservationRepository, BillingRepository billingRepository, SideDishRepository sideDishRepository) {

		this.menuItemRepository = menuItemRepository;
		this.menuItemCategoryRepository = menuItemCategoryRepository;
		this.userRepository = userRepository;
		this.deskRepository = deskRepository;
		this.billingRepository = billingRepository;
		this.reservationRepository = reservationRepository;
		this.orderItemRepository = orderItemRepository;
		this.sideDishRepository = sideDishRepository;
		this.desks = new ArrayList<>();
		this.menuItems = new ArrayList<>();
		this.users = new ArrayList<>();

		generateDesks();
		generateMenuItems();
		generateUser();
		updateUserDesk();
		generateOrderItem();
		generateSideDish();
	}

	/**
	 * add specified demo menu items to database
	 */
	private void generateMenuItems() {
		LOGGER.info("Init demo menu item data");
		generateMenuCategories();
		MenuItem[] menu = {
			new MenuItem(menuItemCategoryRepository.findByName("Warme Vorspeise"), "kleiner Salat", 4.80, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Kalte Vorspeise"), "Bärlauchcremesuppe mit Räucherlachs", 4.80, "EUR", "", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Fischgerichte"), "Lachsfilet", 14.60, "EUR", "mit Tagliatelle und Tomaten", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Fischgerichte"), "Salatteller", 13.80, "EUR", "mit gebratenem Zanderfilet", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Fleischgerichte"), "Pasta Bolognese", 11.90, "EUR", "mit Tomaten und Parmesan", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Fleischgerichte"), "Schweinerückensteak", 13.90, "EUR", "mit Pfefferrahmsauce und Kartoffel Wedges", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Vegetarische Gerichte"), "Hausgemachte Kartoffel Gnocchi", 11.80, "EUR", "Gnocchi mit mediterranem Gemüse, Fetakäse, Rucola & Parmesan", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Vegetarische Gerichte"), "Hausgemachte Käsespätzle", 11.60, "EUR", "mit Beilagensalat", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Nachspeise"), "Drei Kugeln Eis", 6.10, "EUR", "mit Sahne", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Alkoholfreie Getränke"), "Spezi", 2.10, "EUR", "0,20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Alkoholfreie Getränke"), "Ginger Ale", 2.20, "EUR", "0,20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Bier"), "Warsteiner", 2.00, "EUR", "0.25l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Bier"), "Guinness", 3.00, "EUR", "0.30l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Wein"), "Cabernet Sauvignon", 3.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Wein"), "Pinot Grigio", 3.90, "EUR", "0.20l", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Spirituosen"), "Jägermeister", 2.00, "EUR", "5cl", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Spirituosen"), "Sambuca", 3.90, "EUR", "5cl", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Heiße Getränke"), "Tasse Kaffee", 1.50, "EUR", "mit Milch, Zucker", "", new ArrayList<>()),
			new MenuItem(menuItemCategoryRepository.findByName("Heiße Getränke"), "Heiße Schokolade", 2.10, "EUR", "mit Sahne", "", new ArrayList<>())
		};
		for (MenuItem m : menu) this.menuItems.add(menuItemRepository.save(m));
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
		this.users.add(userRepository.save(new User("Karl","Karl","Karlson", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Karmen", "Karmen","Kernel",bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Konsti","Konstantin","Kavos", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Kim","Kimberley","Klar", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Katha","Katherina","Keller", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Knut", "Knut","Knutovic",bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		// Teilzeit
		this.users.add(userRepository.save(new User("Kurt", "Kurt","Kordova",bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Katja","Katja","Klein", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));

		// chiefs (or chefs) with 'T' and Tibor is ONLY used for wash the dishes
		this.users.add(userRepository.save(new User("Tim","Timothy", "Totenworth", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		this.users.add(userRepository.save(new User("Tibor","Tibor", "Tarnomoglou", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		this.users.add(userRepository.save(new User("Tami","Tamara","Tanenbaum", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		this.users.add(userRepository.save(new User("Tati","Tatajana", "Tovonoski", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		this.users.add(userRepository.save(new User("Tanar","Tanar","Tenkin", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		// Teilzeit
		this.users.add(userRepository.save(new User("Tobi","Tobias", "Trottwar", bCryptPasswordEncoder.encode("tim"), Permission.CHEF)));
		//CEO
		this.users.add(userRepository.save(new User("Walter","Walter", "Wald", bCryptPasswordEncoder.encode("walter"), Permission.CEO)));

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
		users.parallelStream().forEach(user -> {if (user.getPermissions().contains(Permission.SERVICE)) waiter.add(user);});

		OrderItem ord;
		for (int i = 0; i < 50; i++) {
			ord = new OrderItem(
					waiter.get((int) (Math.random() * waiter.size())),
					this.desks.get((int) (Math.random() * this.desks.size())),
					this.menuItems.get((int) (Math.random() * this.menuItems.size())),
					"");
			// For BI-Group new Timestemp
			states = new ArrayList<>();
			state = new OrderItemState(OrderItemState.State.NEW);
			Long millitime =System.currentTimeMillis()-((long)(Math.random()* 7200000)-7200000);
			state.setDate(new Date(millitime));
			states.add(state);
			ord.setStates(states);


			orderItems.add(ord);
		}

		orderItemRepository.save(orderItems);
		LOGGER.info("Finished creating random orders");
	}


	// Generate SideDishes

	private void generateSideDish() {
		LOGGER.info("Creating Sidedishes");
//        sideDishRepository.deleteAll();
		SideDish sideDish = new SideDish("Salatbeilagen");
		this.sideDishRepository.save(sideDish);
		sideDish.addSideDish(menuItemRepository.findByName("kleiner Salat"));
		sideDish.addSideDish(menuItemRepository.findByName("Salatteller"));
		this.sideDishRepository.save(sideDish);

		MenuItem m = menuItemRepository.findByName("Lachsfilet");
		m.setSideDish(sideDish);
		menuItemRepository.save(m);
		m = menuItemRepository.findByName("Pasta Bolognese");
		m.setSideDish(sideDish);
		menuItemRepository.save(m);

	}

	// ------------- START Semantic group: MenuCategory -------------------------
	private void generateMenuCategories() {
		LOGGER.info("Generate menu Items");
		ArrayList<MenuItemCategory> tmp = new ArrayList<MenuItemCategory>();

		tmp.add(addCategory("Vorspeise", null));
		tmp.add(addCategory("Hauptspeise", null));
		tmp.add(addCategory("Nachspeise", null));
		tmp.add(addCategory("Getränke", null));
		//2nd level
		tmp.add(addCategory("Warme Vorspeise", tmp.get(0)));
		tmp.add(addCategory("Kalte Vorspeise", tmp.get(0)));
		//main Dish
		tmp.add(addCategory("Fischgerichte", tmp.get(1)));
		tmp.add(addCategory("Fleischgerichte", tmp.get(1)));
		tmp.add(addCategory("Vegetarische Gerichte", tmp.get(1)));
		//Drinks
		tmp.add(addCategory("Alkoholfreie Getränke", tmp.get(3)));
		tmp.add(addCategory("Bier", tmp.get(3)));
		tmp.add(addCategory("Wein", tmp.get(3)));
		tmp.add(addCategory("Heiße Getränke", tmp.get(3)));
		tmp.add(addCategory("Spirituosen", tmp.get(3)));

	}

	// Subfunction for generateMenuCategories
	private MenuItemCategory addCategory(String name, MenuItemCategory parent) {
		MenuItemCategory child = new MenuItemCategory(name);
		child.setParent(parent);
		return menuItemCategoryRepository.save(child);
	}
// -------------END OF Semantic group: MenuCategory -------------------------

}
