package testing.repository;

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.entity.Permission;
import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.BillingRepository;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.persistence.repository.UserRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.UserProfileDetails;
import de.neoventus.rest.dto.BillingDto;
import de.neoventus.rest.dto.UserDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import testing.AbstractTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * testing the user repository methods
 *
 * @author Dennis Thanner
 **/
public class UserRepositoryTest extends AbstractTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BillingRepository billingRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Test
	public void testSearchByName() {
		User u = new User();
		u.setUsername("Test 1");
		userRepository.save(u);

		u = userRepository.findByUsername("Test 1");

		Assert.assertNotNull(u);

		Assert.assertTrue(u.getUsername().equals("Test 1"));
	}

	/**
	 * test the custom implementation method for saving users by dto
	 *
	 * @see de.neoventus.persistence.repository.advanced.impl.UserRepositoryImpl#save(UserDto)
	 */
	@Test
	public void testSaveByDto() {
		UserDto dto = new UserDto();
		dto.setUsername("test");

		userRepository.save(dto);

		User u = userRepository.findByUsername("test");

		Assert.assertNotNull(u);
	}

	/**
	 * test if the specified before save event works
	 *
	 * @see de.neoventus.persistence.event.UserLifecycleEvents#onBeforeSave(BeforeSaveEvent)
	 */
	@Test
	public void testBeforeSaveEvent() {
		User u2 = new User();
		userRepository.save(u2);


		User u = new User("test", "test","testson","test","Fulltime" ,Permission.ADMIN);

		u = userRepository.save(u);

		Assert.assertTrue(u.getWorkerId() == 1);

	}

	/**
	 * test user details calculation
	 */
	@Test
	public void testProfileDetails() {
		// create test user
		User test = new User();
		test = this.userRepository.save(test);

		// create test menu item
		MenuItem m = new MenuItem();
		m.setPrice(2.50);
		m = this.menuItemRepository.save(m);

		// create orders
		OrderItem or = new OrderItem();
		or.setItem(m);
		or.setWaiter(test);
		or = this.orderItemRepository.save(or);

		OrderItem or1 = new OrderItem();
		or1.setItem(m);
		or1.setWaiter(test);
		or1 = this.orderItemRepository.save(or1);

		// create billing
		BillingDto billingDto = new BillingDto();
		billingDto.setItems(Arrays.asList(or.getId(), or1.getId()));
		billingDto.setWaiter(test.getId());
		billingDto.setTotalPaid(5.50);
		this.billingRepository.save(billingDto);

		UserProfileDetails details = this.userRepository.getUserProfileDetails(test.getId());

		Assert.assertTrue(details.getRevenueToday() == 5.5);
		Assert.assertTrue(details.getTipsToday() == .5);
		Assert.assertTrue(details.getLevel() == 0);
		Assert.assertTrue(details.getExpNextLevel() == 50);
		Assert.assertTrue(details.getExp() == 2);

	}

	/**
	 * test level calculation
	 */
	@Test
	public void testUserLevel() {
		// create test user
		User test = new User();
		test = this.userRepository.save(test);


		List<OrderItem> orders = new ArrayList<>();
		// create orders
		for (int i = 0; i < 49; i++) {

			OrderItem or1 = new OrderItem();
			or1.setWaiter(test);
			orders.add(or1);

		}
		this.orderItemRepository.save(orders);

		UserProfileDetails userProfileDetails = this.userRepository.getUserProfileDetails(test.getId());

		Assert.assertTrue(userProfileDetails.getLevel() == 0);

		Assert.assertTrue(userProfileDetails.getExpLevelStart() == 0);
		Assert.assertTrue(userProfileDetails.getExpNextLevel() == 50);

		orders = new ArrayList<>();
		// create orders
		for (int i = 0; i < 2; i++) {

			OrderItem or1 = new OrderItem();
			or1.setWaiter(test);
			orders.add(or1);

		}
		this.orderItemRepository.save(orders);

		userProfileDetails = this.userRepository.getUserProfileDetails(test.getId());

		Logger.getAnonymousLogger().info(userProfileDetails.getLevel() + "");

		Assert.assertTrue(userProfileDetails.getLevel() == 1);
		Assert.assertTrue(userProfileDetails.getExpNextLevel() == 60);
		Assert.assertTrue(userProfileDetails.getExpLevelStart() == 50);


	}

	/**
	 * clear the data written
	 */
	@After
	public void deleteAll() {
		userRepository.deleteAll();
		menuItemRepository.deleteAll();
		billingRepository.deleteAll();
		orderItemRepository.deleteAll();
	}

}
