package de.neoventus.init;/**
 * Created by julian on 11.04.2017.
 */
import de.neoventus.init.dbOperations.InsertUpdateDelete;
import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.persistence.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * @author: Dennis Thanner, Julian Beck, Markus Knauer
 * @version:    0.0.2 Indirectly insert, Update, delete on DB (Class InsertUpdateDelete) +
 *                    and respository access in ControllEntityObjects- JB
 *              0.0.1 Refactor default Demodata in seperate class
 *
 **/

@Component
@Profile("default")
public class DefaultDemoDataIntoDB {

    private static int MAX_DESKS = 10;

    private ControllEntityObjects controllEntityObjects;

    private Logger logger = Logger.getLogger(this.getClass().getName());

   //
    public DefaultDemoDataIntoDB(DeskRepository deskRepository,UserRepository userRepository,MenuItemRepository menuItemRepository,OrderItemRepository orderItemRepository){

        controllEntityObjects = new ControllEntityObjects(deskRepository,
                                    userRepository,
                                    menuItemRepository,
                                    orderItemRepository);

        generateDesks();
        generateMenuItems();
        generateUser();
        generateOrderItem();
        updateTest();
    }
    /**
     * add specified demo menu items to database
     */
    private void generateMenuItems() {
        logger.info("Init demo menu item data");

        controllEntityObjects.generateMenuItems("kleiner Salat", 4.80, "EUR", "kalte Vorspeise", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Bärlauchcremesuppe mit Räucherlachs", 4.80, "EUR", "warme Vorspeise", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Lachsfilet", 14.60, "EUR", "mit Tagliatelle und Tomaten", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Salatteller", 13.80, "EUR", "mit gebratenem Zanderfilet", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Pasta Bolognese", 11.90, "EUR", "mit Tomaten und Parmesan", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Schweinerückensteak", 13.90, "EUR", "mit Pfefferrahmsauce und Kartoffel Wedges", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Hausgemachte Kartoffel Gnocchi", 11.80, "EUR", "Gnocchi mit mediterranem Gemüse, Fetakäse, Rucola & Parmesan", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Hausgemachte Käsespätzle", 11.60, "EUR", "mit Beilagensalat", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Drei Kugeln Eis", 6.10, "EUR", "mit Sahne", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Spezi", 2.10, "EUR", "0,20l", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Ginger Ale", 2.20, "EUR", "0,20l", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Warsteiner", 2.00, "EUR", "0.25l", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Guinness", 3.00, "EUR", "0.30l", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Cabernet Sauvignon", 3.90, "EUR", "0.20l", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Pinot Grigio", 3.90, "EUR", "0.20l", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Jägermeister", 2.00, "EUR", "5cl", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Sambuca", 3.90, "EUR", "5cl", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Tasse Kaffee", 1.50, "EUR", "mit Milch, Zucker", "", new ArrayList<>());

        controllEntityObjects.generateMenuItems("Heiße Schokolade", 2.10, "EUR", "mit Sahne", "", new ArrayList<>());
    }

    /**
     * generate demo restaurant desks
     */
    private void generateDesks() {
        logger.info("Init demo restaurant desks");
        for(int i = 0; i < MAX_DESKS; i++) controllEntityObjects.generateDesks((int) (Math.random() * 5) + 3);
    }

    /**
     * generate demo User
     */
    private void generateUser() {
        logger.info("Init demo User");
        // geneeate eight waiter
        controllEntityObjects.generateUser("Karl", "karlsson",Permission.SERVICE);

        controllEntityObjects.generateUser("Karmen", "karlsson",Permission.SERVICE);

        controllEntityObjects.generateUser("Konstantin", "karlsson",Permission.SERVICE);

        controllEntityObjects.generateUser("Kimberley", "karlsson",Permission.SERVICE);

        controllEntityObjects.generateUser("Katharina", "karlsson",Permission.SERVICE);

        controllEntityObjects.generateUser("Knut", "karlsson",Permission.SERVICE);

        controllEntityObjects.generateUser("Kurt", "karlsson",Permission.SERVICE);

        controllEntityObjects.generateUser("Katja", "karlsson",Permission.SERVICE);

        //CEO
        controllEntityObjects.generateUser("Walter","walter",Permission.CEO);

    }
    private void generateOrderItem(){
        controllEntityObjects.generateOrder(1,18,2,"");
    }




    private void updateTest(){

    }


}
