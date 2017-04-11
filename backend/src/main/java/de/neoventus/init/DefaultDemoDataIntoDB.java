package de.neoventus.init;/**
 * Created by julian on 11.04.2017.
 */

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.MenuItem;
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
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * @author: Dennis Thanner, Julian Beck, Markus Knauer
 * @version: 0.0.1
 * @description: Refactor default Demodata in seperate class
 **/

@Component
@Profile("default")
public class DefaultDemoDataIntoDB {

    private static int MAX_DESKS = 10;


    private DeskRepository deskRepository;
    private UserRepository userRepository;
    private MenuItemRepository menuItemRepository;



    private Logger logger = Logger.getLogger(this.getClass().getName());

   //
    public DefaultDemoDataIntoDB(DeskRepository deskRepository,UserRepository userRepository,MenuItemRepository menuItemRepository){
        this.deskRepository =deskRepository;
        this.menuItemRepository =menuItemRepository;
        this.userRepository =userRepository;
        clearData();
        generateDesks();
        generateMenuItems();
        generateUser();
    }
    /**
     * add specified demo menu items to database
     */
    private void generateMenuItems() {
        logger.info("Init demo menu item data");

        MenuItem mI = new MenuItem();
        mI.setAll("kleiner Salat", 4.80, "EUR", "kalte Vorspeise", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Bärlauchcremesuppe mit Räucherlachs", 4.80, "EUR", "warme Vorspeise", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Lachsfilet", 14.60, "EUR", "mit Tagliatelle und Tomaten", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Salatteller", 13.80, "EUR", "mit gebratenem Zanderfilet", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Pasta Bolognese", 11.90, "EUR", "mit Tomaten und Parmesan", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Schweinerückensteak", 13.90, "EUR", "mit Pfefferrahmsauce und Kartoffel Wedges", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Hausgemachte Kartoffel Gnocchi", 11.80, "EUR", "Gnocchi mit mediterranem Gemüse, Fetakäse, Rucola & Parmesan", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Hausgemachte Käsespätzle", 11.60, "EUR", "mit Beilagensalat", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Drei Kugeln Eis", 6.10, "EUR", "mit Sahne", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Spezi", 2.10, "EUR", "0,20l", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Ginger Ale", 2.20, "EUR", "0,20l", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Warsteiner", 2.00, "EUR", "0.25l", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Guinness", 3.00, "EUR", "0.30l", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Cabernet Sauvignon", 3.90, "EUR", "0.20l", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Pinot Grigio", 3.90, "EUR", "0.20l", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Jägermeister", 2.00, "EUR", "5cl", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Sambuca", 3.90, "EUR", "5cl", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Tasse Kaffee", 1.50, "EUR", "mit Milch, Zucker", "", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.setAll("Heiße Schokolade", 2.10, "EUR", "mit Sahne", "", new ArrayList<>());
        menuItemRepository.save(mI);

    }

    /**
     * generate demo restaurant desks
     */
    private void generateDesks() {
        logger.info("Init demo restaurant desks");
        for(int i = 0; i < MAX_DESKS; i++) {
            Desk des = new Desk();
            des.setNumber(i + 1);
            des.setSeats((int) (Math.random() * 5) + 3);
            deskRepository.save(des);
        }
    }

    /**
     * generate demo User
     */
    private void generateUser() {
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


    /**
     * clear before regenerate to allow changes
     */
    private void clearData() {
        deskRepository.deleteAll();
        menuItemRepository.deleteAll();
        userRepository.deleteAll();
    }

}
