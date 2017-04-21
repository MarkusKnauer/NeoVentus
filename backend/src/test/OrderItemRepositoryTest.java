import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;
import de.neoventus.rest.dto.OrderItemDto;
import de.neoventus.rest.dto.UserDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

/**
 * testing the Order repository methods
 *
 * @author Julian beck
 * @version 0.0.1
 **/
public class OrderItemRepositoryTest extends AbstractTest {

	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private MenuItemRepository menuItemRepository;
	@Autowired
	private DeskRepository deskRepository;
	@Autowired
	private UserRepository userRepository;

	private Desk desk = null;
	private MenuItem menuItem= null;
	private User user= null;
	private Reservation reservation= null;

	@Test
	public void testSearchByID() {

		OrderItem o = new OrderItem();
		o.setOrderID(1);
		o.setDesk(getDesk());
		o.setWaiter(getUser());
		o.setItem(getMenuItem());

		orderItemRepository.save(o);

		OrderItem o2 = orderItemRepository.findByOrderID(o.getOrderID());
		Assert.assertNotNull(o);
		Assert.assertEquals(o.getOrderID(), o2.getOrderID());
	}

	/**
	 * test the custom implementation method for saving users by dto
	 *
	 * @see de.neoventus.persistence.repository.advanced.impl.OrderItemRepositoryImpl#save(OrderItemDto)
	 */
	@Test
	public void testSaveByDto() {
		getDesk();
		getMenuItem();
		getUser();

		OrderItemDto dto = new OrderItemDto(1,1,1,"");


		orderItemRepository.save(dto);

		OrderItem o = orderItemRepository.findFirstByOrderByOrderIDDesc();
		Assert.assertNotNull(o);
		Assert.assertEquals(o.getOrderID(), dto.getOrderID());
	}

	/**
	 * test if the specified before save event works
	 *
	 * @see de.neoventus.persistence.event.UserLifecycleEvents#onBeforeSave(BeforeSaveEvent)
	 */
	@Test
	public void testBeforeSaveEvent() {
		OrderItem o2 = new OrderItem();
		Desk desk = getDesk();
		MenuItem item = getMenuItem();
		User user = getUser();

		o2.setOrderID(1);
		o2.setWaiter(user);
		o2.setDesk(desk);
		o2.setItem(item);
		orderItemRepository.save(o2);

		OrderItem o = new OrderItem();

		o.setWaiter(getUser2());
		o.setDesk(getDesk2());
		o.setItem(getMenuItem2());
		o = orderItemRepository.save(o);
		Assert.assertNotNull(o);
		Assert.assertTrue(o.getOrderID()== 2);

	}

	private Desk getDesk2() {
		if (desk == null) {
			desk = new Desk();
			desk.setNumber(1000);

			deskRepository.save(getDesk2());
		}
		return desk;
	}
	private User getUser2() {
		if (user == null) {
			user = new User();
			user.setUsername("Karl-Otto");

			userRepository.save(getUser2());
		}
		return user;
	}
	private MenuItem getMenuItem2() {
		if (menuItem == null) {
			menuItem = new MenuItem();
			menuItem.setDescription("Fanta");

			menuItemRepository.save(getMenuItem());
		}
		return menuItem;
	}

	private Desk getDesk() {
		if (desk == null) {
			desk = new Desk();
			desk.setNumber(999);

			deskRepository.save(getDesk());
		}
		return desk;
	}
	private User getUser() {
		if (user == null) {
			user = new User();
			user.setUsername("Otto");

			userRepository.save(getUser());
		}
		return user;
	}
	private MenuItem getMenuItem() {
		if (menuItem == null) {
			menuItem = new MenuItem();
			menuItem.setDescription("Spezi");

			menuItemRepository.save(getMenuItem());
		}
		return menuItem;
	}
	/**
	 * clear the data written
	 */
	@After
	public void deleteAll() {
		orderItemRepository.deleteAll();
		deskRepository.deleteAll();
		menuItemRepository.deleteAll();
		userRepository.deleteAll();
	}

}
