/**
 * Created by julian on 09.04.2017.
 */


import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.repository.MenuItemRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.After;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description: Creats the Menu
 **/
public class Menu_Creator extends AbstractTest{

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    public void testCreateMenuCard(){
        menuItemRepository.deleteAll();
        MenuItem mI = new MenuItem();
        mI.allSet("kleiner Salat",4.80,"EUR","kalte Vorspeise","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Bärlauchcremesuppe mit Räucherlachs",4.80,"EUR","warme Vorspeise","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Lachsfilet",14.60,"EUR","mit Tagliatelle und Tomaten","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Salatteller",13.80,"EUR","mit gebratenem Zanderfilet","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Pasta Bolognese",11.90,"EUR","mit Tomaten und Parmesan","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Schweinerückensteak",13.90,"EUR","mit Pfefferrahmsauce und Kartoffel Wedges","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Hausgemachte Kartoffel Gnocchi",11.80,"EUR","Gnocchi mit mediterranem Gemüse, Fetakäse, Rucola & Parmesan","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Hausgemachte Käsespätzle",11.60,"EUR","mit Beilagensalat","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Drei Kugeln Eis",6.10,"EUR","mit Sahne","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Spezi",2.10,"EUR","0,20l","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Ginger Ale",2.20,"EUR","0,20l","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Warsteiner",2.00,"EUR","0.25l","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Guinness",3.00,"EUR","0.30l","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Cabernet Sauvignon",3.90,"EUR","0.20l","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Pinot Grigio",3.90,"EUR","0.20l","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Jägermeister",2.00,"EUR","5cl","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Sambuca",3.90,"EUR","5cl","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Tasse Kaffee",1.50,"EUR","mit Milch, Zucker","", new ArrayList<>());
        menuItemRepository.save(mI);
        mI = new MenuItem();
        mI.allSet("Heiße Schokolade",2.10,"EUR","mit Sahne","", new ArrayList<>());
        menuItemRepository.save(mI);

        //order = orderRepository.findByUsername("Test 1");

        //Assert.assertNotNull(order);

        //Assert.assertTrue(order.getUsername().equals("Test 1"));
    }

    /**
     * clear the data written
     */
    //@After
    public void deleteAll() {

        menuItemRepository.deleteAll();
    }

}

