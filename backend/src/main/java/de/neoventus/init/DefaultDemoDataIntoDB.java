package de.neoventus.init;/**
 * Created by julian on 11.04.2017.
 */
import de.neoventus.init.dbOperations.InsertUpdateDelete;
import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * @author: Dennis Thanner, Julian Beck, Markus Knauer
 * @version:    0.0.2 Indirectly insert, Update, delete on DB (Class InsertUpdateDelete) - JB
 *              0.0.1 Refactor default Demodata in seperate class
 *
 **/

@Component
@Profile("default")
public class DefaultDemoDataIntoDB {

    private static int MAX_DESKS = 10;


    private DeskRepository deskRepository;
    private UserRepository userRepository;
    private MenuItemRepository menuItemRepository;
    private InsertUpdateDelete insUpdDel;


    private Logger logger = Logger.getLogger(this.getClass().getName());

   //
    public DefaultDemoDataIntoDB(DeskRepository deskRepository,UserRepository userRepository,MenuItemRepository menuItemRepository){
        this.deskRepository =deskRepository;
        this.menuItemRepository =menuItemRepository;
        this.userRepository =userRepository;
        clearData();
        //Danger!! AC/DC with variable in the IUD-Classes (Collectionssize)!!
        insUpdDel = new InsertUpdateDelete(deskRepository, userRepository,menuItemRepository,null,null,null );


        generateDesks();
        generateMenuItems();
        generateUser();
        updateTest();
    }
    /**
     * add specified demo menu items to database
     */
    private void generateMenuItems() {
        logger.info("Init demo menu item data");

        MenuItem mI [] = new MenuItem[19];
        int zähler = 0;
        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("kleiner Salat", 4.80, "EUR", "kalte Vorspeise", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Bärlauchcremesuppe mit Räucherlachs", 4.80, "EUR", "warme Vorspeise", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Lachsfilet", 14.60, "EUR", "mit Tagliatelle und Tomaten", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Salatteller", 13.80, "EUR", "mit gebratenem Zanderfilet", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Pasta Bolognese", 11.90, "EUR", "mit Tomaten und Parmesan", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Schweinerückensteak", 13.90, "EUR", "mit Pfefferrahmsauce und Kartoffel Wedges", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Hausgemachte Kartoffel Gnocchi", 11.80, "EUR", "Gnocchi mit mediterranem Gemüse, Fetakäse, Rucola & Parmesan", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Hausgemachte Käsespätzle", 11.60, "EUR", "mit Beilagensalat", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Drei Kugeln Eis", 6.10, "EUR", "mit Sahne", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Spezi", 2.10, "EUR", "0,20l", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Ginger Ale", 2.20, "EUR", "0,20l", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Warsteiner", 2.00, "EUR", "0.25l", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Guinness", 3.00, "EUR", "0.30l", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Cabernet Sauvignon", 3.90, "EUR", "0.20l", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Pinot Grigio", 3.90, "EUR", "0.20l", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Jägermeister", 2.00, "EUR", "5cl", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Sambuca", 3.90, "EUR", "5cl", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Tasse Kaffee", 1.50, "EUR", "mit Milch, Zucker", "", new ArrayList<>());

        mI[zähler]= new MenuItem();
        mI[zähler++].setAll("Heiße Schokolade", 2.10, "EUR", "mit Sahne", "", new ArrayList<>());

        for(int i= 0; i< mI.length; i++){
            insUpdDel.insertMenuItem(mI[i]);
        }

    }

    /**
     * generate demo restaurant desks
     */
    private void generateDesks() {
        logger.info("Init demo restaurant desks");
        for(int i = 0; i < MAX_DESKS; i++) {
            Desk des = new Desk();
            des.setSeats((int) (Math.random() * 5) + 3);
            insUpdDel.insertDesk(des);
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
        insUpdDel.insertUser(user);

        user = new User();
        user.setUsername("Karmen");
        user.setPermissions(Arrays.asList(Permission.SERVICE));
        insUpdDel.insertUser(user);

        user = new User();
        user.setPermissions(Arrays.asList(Permission.SERVICE));
        user.setUsername("Konstantin");
        insUpdDel.insertUser(user);

        user = new User();
        user.setUsername("Kimberley");
        user.setPermissions(Arrays.asList(Permission.SERVICE));
        insUpdDel.insertUser(user);

        user = new User();
        user.setUsername("Katharina");
        user.setPermissions(Arrays.asList(Permission.SERVICE));
        insUpdDel.insertUser(user);

        user = new User();
        user.setUsername("Knut");
        user.setPermissions(Arrays.asList(Permission.SERVICE));
        insUpdDel.insertUser(user);

        user = new User();
        user.setUsername("Kurt");
        user.setPermissions(Arrays.asList(Permission.SERVICE));
        insUpdDel.insertUser(user);

        user = new User();
        user.setUsername("Katja");
        user.setPermissions(Arrays.asList(Permission.SERVICE));
        insUpdDel.insertUser(user);
        // CEO
        user = new User();
        user.setUsername("Walter");
        user.setPermissions(Arrays.asList(Permission.CEO));
        insUpdDel.insertUser(user);

    }
    private void updateTest(){
        Desk des = deskRepository.findByNumber(1);
        des.setNumber(12);
        insUpdDel.updateDesk(des);

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
