
package de.neoventus.init;

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Dennis Thanner, Julian Beck, Markus Knauer, Tim Heidelbach
 * @version 0.0.5 added random order items init - DT
 *          0.0.4 changed to new repositories
 *          0.0.3 Indirectly insert, Update, delete on DB (Class InsertUpdateDelete) +
 *          and repository access in ControlEntityObjects- JB
 *          0.0.2 Refactor default demo data in separate class
 **/

class DefaultDemoDataIntoDB {

	private static final int MAX_DESKS = 10;
	private final static Logger LOGGER = Logger.getLogger(DefaultDemoDataIntoDB.class.getName());
	private MenuItemRepository menuItemRepository;
	private MenuItemCategoryRepository menuItemCategoryRepository;
	private UserRepository userRepository;
	private DeskRepository deskRepository;
	private OrderItemRepository orderItemRepository;
	private BillingRepository billingRepository;
	private ReservationRepository reservationRepository;

	// init documents saved for class based access
	private List<Desk> desks;
	private List<MenuItem> menuItems;
	private List<User> users;

	public DefaultDemoDataIntoDB(DeskRepository deskRepository, UserRepository userRepository,
								 MenuItemRepository menuItemRepository, MenuItemCategoryRepository menuItemCategoryRepository, OrderItemRepository orderItemRepository,
								 ReservationRepository reservationRepository, BillingRepository billingRepository) {

		this.menuItemRepository = menuItemRepository;
		this.menuItemCategoryRepository = menuItemCategoryRepository;
		this.userRepository = userRepository;
		this.deskRepository = deskRepository;
		this.billingRepository = billingRepository;
		this.reservationRepository = reservationRepository;
		this.orderItemRepository = orderItemRepository;
		this.desks = new ArrayList<>();
		this.menuItems = new ArrayList<>();
		this.users = new ArrayList<>();

		generateDesks();
		generateMenuItems();
		generateUser();
		updateUserDesk();
		generateOrderItem();
	}

	/**
	 * add specified demo menu items to database
	 */
	private void generateMenuItems() {
		LOGGER.info("Init demo menu item data");
		MenuItemCategory category = menuItemCategoryRepository.findByName("categories");
		MenuItem[] menu = {
				new MenuItem(category.getSubcategory().get(1).getSubcategory().get(1), "kleiner Salat", 4.80, "EUR", "kalte Vorspeise", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(1).getSubcategory().get(2), "Bärlauchcremesuppe mit Räucherlachs", 4.80, "EUR", "warme Vorspeise", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(2).getSubcategory().get(1), "Lachsfilet", 14.60, "EUR", "mit Tagliatelle und Tomaten", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(2).getSubcategory().get(1), "Salatteller", 13.80, "EUR", "mit gebratenem Zanderfilet", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(2).getSubcategory().get(2), "Pasta Bolognese", 11.90, "EUR", "mit Tomaten und Parmesan", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(2).getSubcategory().get(2), "Schweinerückensteak", 13.90, "EUR", "mit Pfefferrahmsauce und Kartoffel Wedges", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(2).getSubcategory().get(3), "Hausgemachte Kartoffel Gnocchi", 11.80, "EUR", "Gnocchi mit mediterranem Gemüse, Fetakäse, Rucola & Parmesan", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(2).getSubcategory().get(3), "Hausgemachte Käsespätzle", 11.60, "EUR", "mit Beilagensalat", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(3).getSubcategory().get(1), "Drei Kugeln Eis", 6.10, "EUR", "mit Sahne", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(4).getSubcategory().get(1), "Spezi", 2.10, "EUR", "0,20l", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(4).getSubcategory().get(1), "Ginger Ale", 2.20, "EUR", "0,20l", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(4).getSubcategory().get(2), "Warsteiner", 2.00, "EUR", "0.25l", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(4).getSubcategory().get(2), "Guinness", 3.00, "EUR", "0.30l", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(4).getSubcategory().get(3), "Cabernet Sauvignon", 3.90, "EUR", "0.20l", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(4).getSubcategory().get(3), "Pinot Grigio", 3.90, "EUR", "0.20l", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(4).getSubcategory().get(4), "Jägermeister", 2.00, "EUR", "5cl", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(4).getSubcategory().get(4), "Sambuca", 3.90, "EUR", "5cl", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(4).getSubcategory().get(5), "Tasse Kaffee", 1.50, "EUR", "mit Milch, Zucker", "", new ArrayList<>()),
				new MenuItem(category.getSubcategory().get(4).getSubcategory().get(5), "Heiße Schokolade", 2.10, "EUR", "mit Sahne", "", new ArrayList<>())
		};
		for(MenuItem m : menu) this.menuItems.add(menuItemRepository.save(m));
	}

	/**
	 * generate demo restaurant desks
	 */
	private void generateDesks() {
		LOGGER.info("Init demo restaurant desks");
		for(int i = 0; i < MAX_DESKS; i++) this.desks.add(deskRepository.save(new Desk((int) (Math.random() * 5) + 3)));
	}

	/**
	 * generate demo User
	 */
	private void generateUser() {
		LOGGER.info("Init demo User");
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		// generate eight waiters
		this.users.add(userRepository.save(new User("Karl", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Karmen", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Konstantin", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Kimberley", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Katharina", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Knut", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Kurt", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));
		this.users.add(userRepository.save(new User("Katja", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE)));

		//CEO
		this.users.add(userRepository.save(new User("Walter", bCryptPasswordEncoder.encode("walter"), Permission.CEO)));

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
		for(int i = 0; i < 1000; i++) {
			orderItems.add(new OrderItem(this.users.get((int) (Math.random() * this.users.size())),
					this.desks.get((int) (Math.random() * this.desks.size())),
					this.menuItems.get((int) (Math.random() * this.menuItems.size())), "", "aufgegeben"));
		}

		orderItemRepository.save(orderItems);
		LOGGER.info("Finished creating random orders");
	}


	private void generateMenuCategories(){
		LOGGER.info("Generate menu Items");
		MenuItemCategory category = new MenuItemCategory("Categories", new ArrayList<MenuItemCategory>(), null);
		MenuItemCategory parent;
		ArrayList<MenuItemCategory> tmp = new ArrayList<MenuItemCategory>();

		// Appetizer
		parent = category;
		category = new MenuItemCategory("Appetizer", new ArrayList<MenuItemCategory>(), parent );
		tmp = parent.getSubcategory();
		tmp.add(category);
		parent.setSubcategory(tmp);
			//Warm-Appetizer
			parent = category;
			category = new MenuItemCategory("Warm_Appetizer", new ArrayList<MenuItemCategory>(), parent );
			tmp = parent.getSubcategory();
			tmp.add(category);
			parent.setSubcategory(tmp);

			//Cold-Appetizer
			category = new MenuItemCategory("Cold_Appetizer", new ArrayList<MenuItemCategory>(), parent );
			tmp = parent.getSubcategory();
			tmp.add(category);
			parent.setSubcategory(tmp);

		// Main-Dish
		parent = category.getParent().getParent();
		category = new MenuItemCategory("Main_Dish", new ArrayList<MenuItemCategory>(), parent );
		tmp = parent.getSubcategory();
		tmp.add(category);
		parent.setSubcategory(tmp);
			//Fish-Dish
			parent = category;
			category = new MenuItemCategory("Fish", new ArrayList<MenuItemCategory>(), parent );
			tmp = parent.getSubcategory();
			tmp.add(category);
			parent.setSubcategory(tmp);

			//Meat
			category = new MenuItemCategory("Meat", new ArrayList<MenuItemCategory>(), parent );
			tmp = parent.getSubcategory();
			tmp.add(category);
			parent.setSubcategory(tmp);

			//Plants
			category = new MenuItemCategory("Vegetarian", new ArrayList<MenuItemCategory>(), parent );
			tmp = parent.getSubcategory();
			tmp.add(category);
			parent.setSubcategory(tmp);
		// Dessert
		parent = category.getParent().getParent();
		category = new MenuItemCategory("Dessert", new ArrayList<MenuItemCategory>(), parent );
		tmp = parent.getSubcategory();
		tmp.add(category);
		parent.setSubcategory(tmp);
		// Drinks
		parent = category.getParent();
		category = new MenuItemCategory("Drinks", new ArrayList<MenuItemCategory>(), parent );
		tmp = parent.getSubcategory();
		tmp.add(category);
		parent.setSubcategory(tmp);
			//Non-alk
			parent = category;
			category = new MenuItemCategory("Alc-free", new ArrayList<MenuItemCategory>(), parent );
			tmp = parent.getSubcategory();
			tmp.add(category);
			parent.setSubcategory(tmp);

			//Beer
			category = new MenuItemCategory("Beer", new ArrayList<MenuItemCategory>(), parent );
			tmp = parent.getSubcategory();
			tmp.add(category);
			parent.setSubcategory(tmp);

			//Firewater
			category = new MenuItemCategory("Highpercentage", new ArrayList<MenuItemCategory>(), parent );
			tmp = parent.getSubcategory();
			tmp.add(category);
			parent.setSubcategory(tmp);

			//Hot Drinks
			category = new MenuItemCategory("HotDrinks", new ArrayList<MenuItemCategory>(), parent );
			tmp = parent.getSubcategory();
			tmp.add(category);
			parent.setSubcategory(tmp);
			while(parent.getParent() != null) parent = parent.getParent();

		menuItemCategoryRepository.save(parent);

	}


}
