package de.neoventus.init;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * initialize the demo data for the project
 *
 * @autor Dennis Thanner, Julian Beck, Markus Knauer
 * @version 0.0.4 added persissions as enum
 * 			0.0.3 user status clean up - DT
 * 			0.0.2 added users - JB
 *
 */
@Component
public class RestaurantDemoInit {

	private static int MAX_DESKS = 10;

	@Autowired
	private DeskRepository deskRepository;

	@Autowired
	private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
	private MenuItemRepository menuItemRepository;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * generate data method executed in application startup
	 *
	 * @see PostConstruct
	 */
	@PostConstruct
	public void initialize() {
		clearData();
		generateDesks();
		generateMenuItems();
		generateUser();
		setDeskToWaiter();
		setOrder();
	}

	/**
	 * add specified demo menu items to database
	 */
	private void generateMenuItems() {
		logger.info("Init demo menu item data");

		MenuItem mI = new MenuItem();
		mI.MenuItem("kleiner Salat", 4.80, "EUR", "kalte Vorspeise", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Bärlauchcremesuppe mit Räucherlachs", 4.80, "EUR", "warme Vorspeise", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Lachsfilet", 14.60, "EUR", "mit Tagliatelle und Tomaten", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Salatteller", 13.80, "EUR", "mit gebratenem Zanderfilet", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Pasta Bolognese", 11.90, "EUR", "mit Tomaten und Parmesan", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Schweinerückensteak", 13.90, "EUR", "mit Pfefferrahmsauce und Kartoffel Wedges", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Hausgemachte Kartoffel Gnocchi", 11.80, "EUR", "Gnocchi mit mediterranem Gemüse, Fetakäse, Rucola & Parmesan", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Hausgemachte Käsespätzle", 11.60, "EUR", "mit Beilagensalat", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Drei Kugeln Eis", 6.10, "EUR", "mit Sahne", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Spezi", 2.10, "EUR", "0,20l", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Ginger Ale", 2.20, "EUR", "0,20l", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Warsteiner", 2.00, "EUR", "0.25l", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Guinness", 3.00, "EUR", "0.30l", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Cabernet Sauvignon", 3.90, "EUR", "0.20l", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Pinot Grigio", 3.90, "EUR", "0.20l", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Jägermeister", 2.00, "EUR", "5cl", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Sambuca", 3.90, "EUR", "5cl", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Tasse Kaffee", 1.50, "EUR", "mit Milch, Zucker", "", new ArrayList<>());
		menuItemRepository.save(mI);
		mI = new MenuItem();
		mI.MenuItem("Heiße Schokolade", 2.10, "EUR", "mit Sahne", "", new ArrayList<>());
		menuItemRepository.save(mI);

	}

	/**
	 * generate demo restaurant desks
	 */
	private void generateDesks() {
		logger.info("Init demo restaurant desks");
		for (int i = 0; i < MAX_DESKS; i++) {
			Desk des = new Desk();
			des.setNumber(i + 1);
			des.setSeats((int) (Math.random() * 5) + 3);
			deskRepository.save(des);
		}
	}

	/**
	 * generate demo User
	 */
	private void generateUser(){
		logger.info("Init demo User");
		// geneeate eight waiter
		User user = new User();
		user.setUsername("Karl");
		user.setPermissions(Arrays.asList(Permission.SERVICE));
		userRepository.save(user);

		user = new User();
		user.setUsername("Karmen");
		user.setPermissions(Arrays.asList(Permission.SERVICE));
		userRepository.save(user);

		user = new User();
		user.setPermissions(Arrays.asList(Permission.SERVICE));
		user.setUsername("Konstantin");
		userRepository.save(user);

		user = new User();
		user.setUsername("Kimberley");
		user.setPermissions(Arrays.asList(Permission.SERVICE));
		userRepository.save(user);

		user = new User();
		user.setUsername("Katharina");
		user.setPermissions(Arrays.asList(Permission.SERVICE));
		userRepository.save(user);

		user = new User();
		user.setUsername("Knut");
		user.setPermissions(Arrays.asList(Permission.SERVICE));
		userRepository.save(user);

		user = new User();
		user.setUsername("Kurt");
		user.setPermissions(Arrays.asList(Permission.SERVICE));
		userRepository.save(user);

		user = new User();
		user.setUsername("Katja");
		user.setPermissions(Arrays.asList(Permission.SERVICE));
		userRepository.save(user);
// CEO
		user = new User();
		user.setUsername("Walter");
		user.setPermissions(Arrays.asList(Permission.CEO));
		userRepository.save(user);

	}
// DANGER! Here must be Parametres in use for dynamic assignment
	private void setDeskToWaiter(){
        User waiter = userRepository.findByUsername("Karl");
        ArrayList<Desk> desks = new ArrayList<>();
        desks.add(deskRepository.findByNumber(1));
        desks.add(deskRepository.findByNumber(2));
        desks.add(deskRepository.findByNumber(3));
        desks.add(deskRepository.findByNumber(4));
        waiter.setDesks(desks);
        userRepository.save(waiter);
        // Second Waiter
        waiter = userRepository.findByUsername("Katja");
        desks = new ArrayList<>();
        desks.add(deskRepository.findByNumber(5));
        desks.add(deskRepository.findByNumber(6));
        desks.add(deskRepository.findByNumber(7));
        waiter.setDesks(desks);
        userRepository.save(waiter);
        // Third Waiter
        waiter = userRepository.findByUsername("Knut");
        desks = new ArrayList<>();
        desks.add(deskRepository.findByNumber(8));
        desks.add(deskRepository.findByNumber(9));
        desks.add(deskRepository.findByNumber(10));
        waiter.setDesks(desks);
        userRepository.save(waiter);
    }
// Demo-Order with Waiter: Katja, Desk 2 from Katja (Desk 6) and the Meal "kleiner Salat"
    private void setOrder(){
        logger.info("Init demo Order");
        User waiter;
        OrderItem order = new OrderItem();
        order.setWaiter((waiter = userRepository.findByUsername("Katja")));
        order.setDesk(waiter.getDesks().get(1));
        order.setItem(menuItemRepository.findByName("kleiner Salat"));
        order.setGuestWish("Ohne Zwiebeln");
        orderItemRepository.save(order);

    }



	/**
	 * clear before regenerate to allow changes
	 */
	private void clearData() {
		deskRepository.deleteAll();
		menuItemRepository.deleteAll();
		userRepository.deleteAll();
		orderItemRepository.deleteAll();
	}

}
