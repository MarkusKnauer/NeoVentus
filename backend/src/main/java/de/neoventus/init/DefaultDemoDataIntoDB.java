
package de.neoventus.init;

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Dennis Thanner, Julian Beck, Markus Knauer, Tim Heidelbach
 * @version 0.0.7 add SideDish bsp-data - MK
 * 			0.0.6 add menuItem Category bsp-data-generator -JB
 * 			0.0.5 added random order items init - DT
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
	private SideDishRepository sideDishRepository;



	// init documents saved for class based access
	private List<Desk> desks;
	private List<MenuItem> menuItems;
	private List<User> users;

	public DefaultDemoDataIntoDB(DeskRepository deskRepository, UserRepository userRepository,
								 MenuItemRepository menuItemRepository, MenuItemCategoryRepository menuItemCategoryRepository, OrderItemRepository orderItemRepository,
								 ReservationRepository reservationRepository, BillingRepository billingRepository,SideDishRepository sideDishRepository) {

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
				new MenuItem(menuItemCategoryRepository.findByName("Warm_Appetizer"), "kleiner Salat", 4.80, "EUR", "kalte Vorspeise", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Cold_Appetizer"), "Bärlauchcremesuppe mit Räucherlachs", 4.80, "EUR", "warme Vorspeise", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Fish"), "Lachsfilet", 14.60, "EUR", "mit Tagliatelle und Tomaten", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Fish"), "Salatteller", 13.80, "EUR", "mit gebratenem Zanderfilet", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Meat"), "Pasta Bolognese", 11.90, "EUR", "mit Tomaten und Parmesan", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Meat"), "Schweinerückensteak", 13.90, "EUR", "mit Pfefferrahmsauce und Kartoffel Wedges", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Vegetarian"), "Hausgemachte Kartoffel Gnocchi", 11.80, "EUR", "Gnocchi mit mediterranem Gemüse, Fetakäse, Rucola & Parmesan", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Vegetarian"), "Hausgemachte Käsespätzle", 11.60, "EUR", "mit Beilagensalat", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Dessert"), "Drei Kugeln Eis", 6.10, "EUR", "mit Sahne", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Alc-free"), "Spezi", 2.10, "EUR", "0,20l", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Alc-free"), "Ginger Ale", 2.20, "EUR", "0,20l", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Beer"), "Warsteiner", 2.00, "EUR", "0.25l", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Beer"), "Guinness", 3.00, "EUR", "0.30l", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Wine"), "Cabernet Sauvignon", 3.90, "EUR", "0.20l", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Wine"), "Pinot Grigio", 3.90, "EUR", "0.20l", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Fire-Water"), "Jägermeister", 2.00, "EUR", "5cl", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Fire-Water"), "Sambuca", 3.90, "EUR", "5cl", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Hot Drinks"), "Tasse Kaffee", 1.50, "EUR", "mit Milch, Zucker", "", new ArrayList<>()),
				new MenuItem(menuItemCategoryRepository.findByName("Hot Drinks"), "Heiße Schokolade", 2.10, "EUR", "mit Sahne", "", new ArrayList<>())
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
		for (int i = 0; i < 50; i++) {
			orderItems.add(new OrderItem(this.users.get((int) (Math.random() * this.users.size())),
					this.desks.get((int) (Math.random() * this.desks.size())),
					this.menuItems.get((int) (Math.random() * this.menuItems.size())), "", "aufgegeben"));
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
	private void generateMenuCategories(){
		LOGGER.info("Generate menu Items");
		ArrayList<MenuItemCategory> tmp = new ArrayList<MenuItemCategory>();

		tmp.add(addCategory("root",null));
			tmp.add(addCategory("Appetizer",tmp.get(0)));
			tmp.add(addCategory("Main_Dish",tmp.get(0)));
			tmp.add(addCategory("Dessert",tmp.get(0)));
			tmp.add(addCategory("Drinks",tmp.get(0)));
				//2nd level
				tmp.add(addCategory("Warm_Appetizer",tmp.get(1)));
				tmp.add(addCategory("Cold_Appetizer",tmp.get(1)));
				//main Dish
				tmp.add(addCategory("Fish",tmp.get(2)));
				tmp.add(addCategory("Meat",tmp.get(2)));
				tmp.add(addCategory("Vegetarian",tmp.get(2)));
				//Drinks
				tmp.add(addCategory("Alc-free",tmp.get(4)));
				tmp.add(addCategory("Beer",tmp.get(4)));
				tmp.add(addCategory("Wine",tmp.get(4)));
				tmp.add(addCategory("HotDrinks",tmp.get(4)));
				tmp.add(addCategory("Firewater",tmp.get(4)));


		for(MenuItemCategory parent: tmp)menuItemCategoryRepository.save(parent);

	}
	// Subfunction for generateMenuCategories
	private MenuItemCategory addCategory(String name, MenuItemCategory parent){
		MenuItemCategory child = new MenuItemCategory(name);
		child.setParent(parent);
		return child;
	}
// -------------END OF Semantic group: MenuCategory -------------------------

}
