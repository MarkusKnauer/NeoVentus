/**
 * Created by julian on 11.04.2017.
 */

package de.neoventus.init;

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Dennis Thanner, Julian Beck, Markus Knauer, Tim Heidelbach
 * @version 0.0.4 changed to new repositories
 * 0.0.3 Indirectly insert, Update, delete on DB (Class InsertUpdateDelete) +
 *          and repository access in ControlEntityObjects- JB
 *          0.0.2 Refactor default demo data in separate class
 **/

class DefaultDemoDataIntoDB {

	private static final int MAX_DESKS = 10;
	private final static Logger LOGGER = Logger.getLogger(DefaultDemoDataIntoDB.class.getName());
	private MenuItemRepository menuItemRepository;
	private UserRepository userRepository;
	private DeskRepository deskRepository;
	private OrderItemRepository orderItemRepository;
	private BillingRepository billingRepository;
	private ReservationRepository reservationRepository;

	public DefaultDemoDataIntoDB(DeskRepository deskRepository, UserRepository userRepository,
								 MenuItemRepository menuItemRepository, OrderItemRepository orderItemRepository,
								 ReservationRepository reservationRepository, BillingRepository billingRepository) {

		this.menuItemRepository = menuItemRepository;
		this.userRepository = userRepository;
		this.deskRepository = deskRepository;
		this.billingRepository = billingRepository;
		this.reservationRepository = reservationRepository;
		this.orderItemRepository = orderItemRepository;

		generateDesks();
		generateMenuItems();
		generateUser();
		updateUserDesk();
		generateOrderItem();
		updateTest();
	}

	/**
	 * add specified demo menu items to database
	 */
	private void generateMenuItems() {
		LOGGER.info("Init demo menu item data");

		List<MenuItem> items = new ArrayList<>();
		items.add(new MenuItem("kleiner Salat", 4.80, "EUR",
				"kalte Vorspeise", "", new ArrayList<>()));

		items.add(new MenuItem("Bärlauchcremesuppe mit Räucherlachs", 4.80, "EUR",
				"warme Vorspeise", "", new ArrayList<>()));

		items.add(new MenuItem("Lachsfilet", 14.60, "EUR",
				"mit Tagliatelle und Tomaten", "", new ArrayList<>()));

		items.add(new MenuItem("Salatteller", 13.80, "EUR",
				"mit gebratenem Zanderfilet", "", new ArrayList<>()));

		items.add(new MenuItem("Pasta Bolognese", 11.90, "EUR",
				"mit Tomaten und Parmesan", "", new ArrayList<>()));

		items.add(new MenuItem("Schweinerückensteak", 13.90, "EUR",
				"mit Pfefferrahmsauce und Kartoffel Wedges", "", new ArrayList<>()));

		items.add(new MenuItem("Hausgemachte Kartoffel Gnocchi", 11.80, "EUR",
				"Gnocchi mit mediterranem Gemüse, Fetakäse, Rucola & Parmesan", "",
				new ArrayList<>()));

		items.add(new MenuItem("Hausgemachte Käsespätzle", 11.60, "EUR",
				"mit Beilagensalat", "", new ArrayList<>()));

		items.add(new MenuItem("Drei Kugeln Eis", 6.10, "EUR",
				"mit Sahne", "", new ArrayList<>()));

		items.add(new MenuItem("Spezi", 2.10, "EUR", "0,20l",
				"", new ArrayList<>()));

		items.add(new MenuItem("Ginger Ale", 2.20, "EUR", "0,20l",
				"", new ArrayList<>()));

		items.add(new MenuItem("Warsteiner", 2.00, "EUR", "0.25l",
				"", new ArrayList<>()));

		items.add(new MenuItem("Guinness", 3.00, "EUR", "0.30l",
				"", new ArrayList<>()));

		items.add(new MenuItem("Cabernet Sauvignon", 3.90, "EUR",
				"0.20l", "", new ArrayList<>()));

		items.add(new MenuItem("Pinot Grigio", 3.90, "EUR", "0.20l",
				"", new ArrayList<>()));

		items.add(new MenuItem("Jägermeister", 2.00, "EUR", "5cl",
				"", new ArrayList<>()));

		items.add(new MenuItem("Sambuca", 3.90, "EUR", "5cl",
				"", new ArrayList<>()));

		items.add(new MenuItem("Tasse Kaffee", 1.50, "EUR",
				"mit Milch, Zucker", "", new ArrayList<>()));

		items.add(new MenuItem("Heiße Schokolade", 2.10, "EUR",
				"mit Sahne", "", new ArrayList<>()));

		menuItemRepository.save(items);
	}

	/**
	 * generate demo restaurant desks
	 */
	private void generateDesks() {
		LOGGER.info("Init demo restaurant desks");
		for(int i = 0; i < MAX_DESKS; i++) deskRepository.save(new Desk((int) (Math.random() * 5) + 3));
	}

	/**
	 * generate demo User
	 */
	private void generateUser() {
		LOGGER.info("Init demo User");
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		List<User> users = new ArrayList<>();
		// generate eight waiters
		users.add(new User("Karl", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE));
		users.add(new User("Karmen", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE));
		users.add(new User("Konstantin", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE));
		users.add(new User("Kimberley", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE));
		users.add(new User("Katharina", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE));
		users.add(new User("Knut", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE));
		users.add(new User("Kurt", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE));
		users.add(new User("Katja", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE));

		//CEO
		users.add(new User("Walter", bCryptPasswordEncoder.encode("walter"), Permission.CEO));

		userRepository.save(users);

	}

	// DANGER! Here must be Parametres in use for dynamic assignment
	private void updateUserDesk() {
		User u = userRepository.findByUserId(1);
		u.getDesks().add(deskRepository.findByNumber(1));
		u.getDesks().add(deskRepository.findByNumber(2));
		u.getDesks().add(deskRepository.findByNumber(3));
		u.getDesks().add(deskRepository.findByNumber(4));
		userRepository.save(u);

		u = userRepository.findByUserId(2);
		u.getDesks().add(deskRepository.findByNumber(5));
		u.getDesks().add(deskRepository.findByNumber(6));
		u.getDesks().add(deskRepository.findByNumber(7));
		userRepository.save(u);

		u = userRepository.findByUserId(5);
		u.getDesks().add(deskRepository.findByNumber(8));
		u.getDesks().add(deskRepository.findByNumber(9));
		u.getDesks().add(deskRepository.findByNumber(10));
		userRepository.save(u);

	}

	private void generateOrderItem() {
		orderItemRepository.save(new OrderItem(userRepository.findByUserId(1),
				deskRepository.findByNumber(2), menuItemRepository.findByNumber(18), ""));

	}

	private void updateTest() {

	}

}
