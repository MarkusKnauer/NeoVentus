package testing.repository;

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.MenuItemCategory;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.MenuItemCategoryRepository;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.persistence.repository.SideDishRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.ObjectCountAggregation;
import de.neoventus.rest.dto.MenuDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import testing.AbstractTest;

import java.util.ArrayList;
import java.util.List;

/**
 * testing the menuItem repository methods
 *
 * @author Markus Knauer
 */
public class MenuItemRepositoryTest extends AbstractTest {

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Autowired
	private MenuItemCategoryRepository menuItemCategoryRepository;

	@Autowired
	private SideDishRepository sideDishRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

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

		MenuItem u = new MenuItem(getCategory(), "test", "tes",12.02, "EUR", "Testdescr", "Testmedia", new ArrayList<>());
		u = menuItemRepository.save(u);

		Assert.assertTrue(u.getMenuItemID() == 2);

	}

	private MenuItemCategory getCategory() {
		MenuItemCategory cat = new MenuItemCategory("Testcat");
		menuItemCategoryRepository.save(cat);
		return cat;
	}

	/**
	 * test popular guest wishes
	 */
	@Test
	public void testPopularGuestWishes() {
		MenuItem m = new MenuItem();
		m = this.menuItemRepository.save(m);

		OrderItem o = new OrderItem();
		o.setItem(m);
		o.setGuestWish("Test");
		this.orderItemRepository.save(o);
		o = new OrderItem();
		o.setItem(m);
		o.setGuestWish("Test");
		this.orderItemRepository.save(o);

		List<ObjectCountAggregation> result = this.menuItemRepository.getPopularGuestWishesForItem(m.getId());

		Assert.assertTrue(result.size() == 1);
		Assert.assertTrue(result.get(0).getObject().equals("Test"));


		o = new OrderItem();
		o.setGuestWish("Test 2");
		o.setItem(m);
		this.orderItemRepository.save(o);

		result = this.menuItemRepository.getPopularGuestWishesForItem(m.getId());

		Assert.assertTrue(result.size() == 2);
		Assert.assertTrue(result.get(0).getObject().equals("Test"));
		Assert.assertTrue(result.get(1).getObject().equals("Test 2"));
	}


	/**
	 * clear the data written
	 */
	@After
	public void deleteAll() {
		menuItemRepository.deleteAll();
		menuItemCategoryRepository.deleteAll();
		sideDishRepository.deleteAll();
		this.orderItemRepository.deleteAll();
	}

}

