import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.MenuItemCategory;
import de.neoventus.persistence.repository.MenuItemCategoryRepository;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.rest.dto.MenuDto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

import java.util.ArrayList;

/**
 * testing the menuItem repository methods
 *
 * @author Markus Knauer
 * @version 0.0.1
 */
public class MenuItemRepositoryTest extends AbstractTest {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MenuItemCategoryRepository menuItemCategoryRepository;

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

        dto.setCategory("L1");
        menuItemCategoryRepository.save(new MenuItemCategory("category", new ArrayList<MenuItemCategory>(), null));

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
        MenuItem u2 = new MenuItem();
        u2.setNumber(1);
        menuItemRepository.save(u2);


      //  MenuItem u = new MenuItem(MenuItemCategory.APPETIZER,"test", 12.02, "EUR", "Testdescr", "Testmedia", new ArrayList<>());

       // u = menuItemRepository.save(u);

        //Assert.assertTrue(u.getMenuItemID() == 2);

    }

    /**
     * clear the data written
     */
    //@After
    public void deleteAll() {
        menuItemRepository.deleteAll();
        menuItemCategoryRepository.deleteAll();
    }

}

