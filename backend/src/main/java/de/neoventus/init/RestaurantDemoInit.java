package de.neoventus.init;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * initialize the demo data for the project
 *
 * @author Dennis Thanner, Julian Beck, Markus Knauer, Tim Heidelbach
 * @version 0.0.6 Using constructor injection - TH
 *          0.0.5 Refactor default Data in separate Class - JB
 *          0.0.4 added permissions as enum - MK
 *          0.0.3 user status clean up - DT
 *          0.0.2 added users - JB
 */
@Component
@Profile("default")
public class RestaurantDemoInit {

    private final DeskRepository deskRepository;

    private final UserRepository userRepository;

    private final OrderItemRepository orderItemRepository;

    private final MenuItemRepository menuItemRepository;

    private final static Logger LOGGER = Logger.getLogger(RestaurantDemoInit.class.getName());

    @Autowired
    public RestaurantDemoInit(DeskRepository deskRepository, UserRepository userRepository,
                              OrderItemRepository orderItemRepository, MenuItemRepository menuItemRepository) {
        this.deskRepository = deskRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.menuItemRepository = menuItemRepository;
    }

    /**
     * generate data method executed in application startup
     *
     * @see PostConstruct
     */
    @PostConstruct
    public void initialize() {
        new DefaultDemoDataIntoDB(deskRepository, userRepository, menuItemRepository, orderItemRepository);
        findWaiterForSetDesk();
        setOrder();
    }

    // DANGER! Here must be Parametres in use for dynamic assignment
    private void findWaiterForSetDesk() {
        User waiter = userRepository.findByUsername("Karl");
        moveDeskToWaiter(waiter, 1, 2, 3, 4);
        // Second Waiter
        waiter = userRepository.findByUsername("Katja");
        moveDeskToWaiter(waiter, 5, 6, 7);
        // Third Waiter
        waiter = userRepository.findByUsername("Knut");
        moveDeskToWaiter(waiter, 8, 9, 10);

    }

    // Method for set Tables to User (Waiter)  - JB
    private void moveDeskToWaiter(User waiter, int... table) {
        ArrayList<Desk> desks = new ArrayList<>();

        for (int i : table) {
            desks.add(deskRepository.findByNumber(table[i]));
        }
        waiter.setDesks(desks);
        userRepository.save(waiter);
    }

    // Demo-Order with Waiter: Katja, Desk 2 from Katja (Desk 6) and the Meal "kleiner Salat"
    private void setOrder() {
        LOGGER.info("Init demo Order");
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
