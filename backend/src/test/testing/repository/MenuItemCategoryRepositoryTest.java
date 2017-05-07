package testing.repository; /**
 * Created by julian on 28.04.2017.
 */

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.MenuItemCategory;
import de.neoventus.persistence.repository.MenuItemCategoryRepository;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.rest.dto.MenuItemCategoryDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import testing.AbstractTest;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/
public class MenuItemCategoryRepositoryTest extends AbstractTest {
	@Autowired
	private MenuItemCategoryRepository menuItemCategoryRepository;

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Test
	public void testTier() {
		menuItemCategoryRepository.deleteAll();
		MenuItemCategory category1 = new MenuItemCategory();
		MenuItemCategory category2 = new MenuItemCategory();
		MenuItemCategory category3 = new MenuItemCategory();

		category1.setParent(null);
		category1.setName("root");
		menuItemCategoryRepository.save(category1);
		category2.setParent(category1);
		category2.setName("Layer1");
		menuItemCategoryRepository.save(category2);
		category2 = new MenuItemCategory();
		category2.setParent(category1);
		category2.setName("Layer11");
		menuItemCategoryRepository.save(category2);
		category3.setParent(category2);
		category3.setName("Layer2");
		menuItemCategoryRepository.save(category3);


	}

	@Test
	public void testSearchByName() {
		MenuItemCategory u = new MenuItemCategory();
		u.setName("Test 1");
		menuItemCategoryRepository.save(u);

		u = menuItemCategoryRepository.findByName("Test 1");

		Assert.assertNotNull(u);

		Assert.assertTrue(u.getName().equals("Test 1"));
	}

	/**
	 * test the custom implementation method for saving menuItems by dto
	 *
	 * @see de.neoventus.persistence.repository.advanced.impl.MenuItemCategoryRepositoryImpl#save(MenuItemCategoryDto)
	 */
	@Test
	public void testSaveByDto() {
		MenuItemCategoryDto dto = new MenuItemCategoryDto();
		dto.setName("test");

		menuItemCategoryRepository.save(dto);

		MenuItemCategory u = menuItemCategoryRepository.findByName("test");

		Assert.assertNotNull(u);
	}

	/**
	 * test if the specified before save event works
	 *
	 * @see de.neoventus.persistence.event.MenuItemCategoryLifecycleEvents#onAfterSave(AfterSaveEvent)
	 */
	@Test
	public void testAfterSaveEvent() {

		MenuItemCategory u2 = new MenuItemCategory();

		u2.setName("root1");

		menuItemCategoryRepository.save(u2);

		MenuItemCategory u = new MenuItemCategory();

		u.setName("Layer1");
		u.setParent(u2);
		u = menuItemCategoryRepository.save(u);

		Assert.assertTrue(u2.getSubcategory().contains(u));

	}

	@Test
	public void testAnchorCategoryInMenuItem() {
		MenuItemCategory u2 = new MenuItemCategory();

		u2.setName("root1");

		menuItemCategoryRepository.save(u2);

		MenuItem u = new MenuItem();
		u.setName("Test 1");
		u.setMenuItemCategory(u2);
		menuItemRepository.save(u);

		u = menuItemRepository.findByName("Test 1");

		Assert.assertNotNull(u);

		Assert.assertTrue(u.getMenuItemCategory().getName().equals("root1"));
	}


	@After
	public void deleteAll() {
		menuItemCategoryRepository.deleteAll();
		menuItemRepository.deleteAll();
	}
}
