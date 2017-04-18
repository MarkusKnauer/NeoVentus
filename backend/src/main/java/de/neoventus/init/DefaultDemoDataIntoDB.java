/**
 * Created by julian on 11.04.2017.
 */

package de.neoventus.init;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author: Dennis Thanner, Julian Beck, Markus Knauer, Tim Heidelbach
 * @version: 0.0.3 Indirectly insert, Update, delete on DB (Class InsertUpdateDelete) +
 * and repository access in ControlEntityObjects- JB
 * 0.0.1 Refactor default demo data in separate class
 **/

@Component
@Profile("default")
class DefaultDemoDataIntoDB {

    private static final int MAX_DESKS = 10;

    private ControlEntityObjects controlEntityObjects;

    private final static Logger LOGGER = Logger.getLogger(DefaultDemoDataIntoDB.class.getName());

    public DefaultDemoDataIntoDB(DeskRepository deskRepository, UserRepository userRepository,
                                 MenuItemRepository menuItemRepository, OrderItemRepository orderItemRepository,
                                 ReservationRepository reservationRepository, BillingRepository billingRepository) {

        deskRepository.deleteAll();
        menuItemRepository.deleteAll();
        userRepository.deleteAll();
        orderItemRepository.deleteAll();
        reservationRepository.deleteAll();
        controlEntityObjects = new ControlEntityObjects(deskRepository,
                userRepository,
                menuItemRepository,
                orderItemRepository,reservationRepository,billingRepository);

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

        controlEntityObjects.generateMenuItems("kleiner Salat", 4.80, "EUR",
                "kalte Vorspeise", "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Bärlauchcremesuppe mit Räucherlachs", 4.80, "EUR",
                "warme Vorspeise", "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Lachsfilet", 14.60, "EUR",
                "mit Tagliatelle und Tomaten", "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Salatteller", 13.80, "EUR",
                "mit gebratenem Zanderfilet", "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Pasta Bolognese", 11.90, "EUR",
                "mit Tomaten und Parmesan", "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Schweinerückensteak", 13.90, "EUR",
                "mit Pfefferrahmsauce und Kartoffel Wedges", "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Hausgemachte Kartoffel Gnocchi", 11.80, "EUR",
                "Gnocchi mit mediterranem Gemüse, Fetakäse, Rucola & Parmesan", "",
                new ArrayList<>());

        controlEntityObjects.generateMenuItems("Hausgemachte Käsespätzle", 11.60, "EUR",
                "mit Beilagensalat", "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Drei Kugeln Eis", 6.10, "EUR",
                "mit Sahne", "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Spezi", 2.10, "EUR", "0,20l",
                "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Ginger Ale", 2.20, "EUR", "0,20l",
                "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Warsteiner", 2.00, "EUR", "0.25l",
                "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Guinness", 3.00, "EUR", "0.30l",
                "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Cabernet Sauvignon", 3.90, "EUR",
                "0.20l", "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Pinot Grigio", 3.90, "EUR", "0.20l",
                "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Jägermeister", 2.00, "EUR", "5cl",
                "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Sambuca", 3.90, "EUR", "5cl",
                "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Tasse Kaffee", 1.50, "EUR",
                "mit Milch, Zucker", "", new ArrayList<>());

        controlEntityObjects.generateMenuItems("Heiße Schokolade", 2.10, "EUR",
                "mit Sahne", "", new ArrayList<>());
    }

    /**
     * generate demo restaurant desks
     */
    private void generateDesks() {
        LOGGER.info("Init demo restaurant desks");
        for (int i = 0; i < MAX_DESKS; i++) controlEntityObjects.generateDesks((int) (Math.random() * 5) + 3);
    }

    /**
     * generate demo User
     */
    private void generateUser() {
        LOGGER.info("Init demo User");

        // generate eight waiters
        controlEntityObjects.generateUser("Karl", "karlsson", Permission.SERVICE);
        controlEntityObjects.generateUser("Karmen", "karlsson", Permission.SERVICE);
        controlEntityObjects.generateUser("Konstantin", "karlsson", Permission.SERVICE);
        controlEntityObjects.generateUser("Kimberley", "karlsson", Permission.SERVICE);
        controlEntityObjects.generateUser("Katharina", "karlsson", Permission.SERVICE);
        controlEntityObjects.generateUser("Knut", "karlsson", Permission.SERVICE);
        controlEntityObjects.generateUser("Kurt", "karlsson", Permission.SERVICE);
        controlEntityObjects.generateUser("Katja", "karlsson", Permission.SERVICE);

        //CEO
        controlEntityObjects.generateUser("Walter", "walter", Permission.CEO);

    }

    // DANGER! Here must be Parametres in use for dynamic assignment
    private void updateUserDesk() {
        controlEntityObjects.updateUserDesk(1, 1);
        controlEntityObjects.updateUserDesk(1, 2);
        controlEntityObjects.updateUserDesk(1, 3);
        controlEntityObjects.updateUserDesk(1, 4);

        controlEntityObjects.updateUserDesk(2, 5);
        controlEntityObjects.updateUserDesk(2, 6);
        controlEntityObjects.updateUserDesk(2, 7);

        controlEntityObjects.updateUserDesk(5, 8);
        controlEntityObjects.updateUserDesk(5, 9);
        controlEntityObjects.updateUserDesk(5, 10);

    }

    private void generateOrderItem() {
        controlEntityObjects.generateOrder(1, 18, 2,
                "");
    }

    private void updateTest() {

    }

}
