import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.MenuItemCategory;
import de.neoventus.persistence.entity.SideDish;
import de.neoventus.persistence.repository.MenuItemCategoryRepository;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.SideDishRepository;
import de.neoventus.rest.dto.MenuDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

import java.util.ArrayList;

/**
 * testing the menuItem repository methods
 *
 * @author Markus Knauer
 * @version 0.0.3 add sideDish - JB
 *          0.0.2 add menuItemCategory -JB
 *          0.0.1 created by MK
 */
public class MenuItemRepositoryTest extends AbstractTest {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MenuItemCategoryRepository menuItemCategoryRepository;

    @Autowired
    private SideDishRepository sideDishRepository;

    @Test
    public void testSearchByName() {
        MenuItem u = new MenuItem();
        u.setName("Test 1");
        menuItemRepository.save(u);

        u = menuItemRepository.findByName("Test 1");

        Assert.assertNotNull(u);

        Assert.assertTrue(u.getName().equals("Test 1"));
    }

    /**
     * test the custom implementation method for saving menuItems by dto
     *
     * @see de.neoventus.persistence.repository.advanced.impl.MenuItemRepositoryImpl#save(MenuDto)
     */
    @Test
    public void testSaveByDto() {
        deleteAll();
        MenuDto dto = new MenuDto();
        dto.setName("test");

        menuItemRepository.save(dto);

        MenuItem u = menuItemRepository.findByName("test");

        Assert.assertNotNull(u);
    }

    /**
     * test if the specified before save event works
     *
     * @see de.neoventus.persistence.event.MenuItemLifecycleEvents#onBeforeSave(BeforeSaveEvent)
     */
    @Test
    public void testBeforeSaveEvent() {
        deleteAll();
        MenuItem u2 = new MenuItem();

        u2.setName("askd");
        u2.setNumber(1);
        menuItemRepository.save(u2);

        MenuItem u = new MenuItem(getCategory(),"test", 12.02, "EUR", "Testdescr", "Testmedia", new ArrayList<>());
        u = menuItemRepository.save(u);

        Assert.assertTrue(u.getMenuItemID() == 2);

    }
    private MenuItemCategory getCategory(){
        MenuItemCategory cat = new MenuItemCategory("Testcat");
        menuItemCategoryRepository.save(cat);
        return cat;
    }

    @Test
    public void testSideDish(){

        // Unit Test for old- Version of SideDish

        deleteAll();
        MenuItem u2 = new MenuItem();

        u2.setName("Menu");
        u2.setNumber(1);
        menuItemRepository.save(u2);
        MenuItem u = new MenuItem(getCategory(),"test", 12.02, "EUR", "Testdescr", "Testmedia", new ArrayList<>());
        menuItemRepository.save(u);
        SideDish sideDish = u.getSideDish();
       // sideDish.addParentalMeal(u2);
        sideDishRepository.save(sideDish);
        MenuItem u3 = new MenuItem();
        u3.setName("Kuchen");
        menuItemRepository.save(u3);
        SideDish d = u3.getSideDish();
        sideDishRepository.save(d);


    }





    /**
     * clear the data written
     */
    @After
    public void deleteAll() {
        menuItemRepository.deleteAll();
        menuItemCategoryRepository.deleteAll();
        sideDishRepository.deleteAll();
    }

}

