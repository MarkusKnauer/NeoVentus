package testing.repository;

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.persistence.repository.UserRepository;
import de.neoventus.rest.dto.OrderItemDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import testing.AbstractTest;

import java.util.List;

/**
 * testing the Order repository methods
 *
 * @author Julian Beck, Dennis Thanner
 * @version 0.0.2 redundancy clean up - DT
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
	private MenuItem menuItem = null;
	private User user = null;
	private Reservation reservation = null;


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

		OrderItemDto dto = new OrderItemDto(1, 1, 1, "");


		orderItemRepository.save(dto);

		List<OrderItem> o = (List<OrderItem>) orderItemRepository.findAll();
		Assert.assertNotNull(o);
		Assert.assertTrue(o.size() == 1);
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
