package testing.repository;

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;
import de.neoventus.persistence.repository.advanced.impl.aggregation.OrderDeskAggregationDto;
import de.neoventus.rest.dto.OrderItemDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import testing.AbstractTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * testing the Order repository methods
 *
 * @author Julian Beck, Dennis Thanner
 * @version 0.0.4 added testGroupedNotPayedOrdersByItemForDesk - DT
 *          0.0.3 added findByBillingIsNullAndStatesStateNotIn test - DT
 *          0.0.2 redundancy clean up - DT
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

	@Autowired
	private BillingRepository billingRepository;

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

	/**
	 * test the custom query for unpaid orders not in specific states
	 *
	 * @see OrderItemRepository#findByBillingIsNullAndStatesStateNotIn(Collection)
	 */
	@Test
	public void testListUnpaidOrders() {
		OrderItemState.State[] finshedStates = new OrderItemState.State[]{OrderItemState.State.FINISHED, OrderItemState.State.CANCELED};

		OrderItem o = new OrderItem();

		this.orderItemRepository.save(o);

		o = new OrderItem();
		o.addState(OrderItemState.State.FINISHED);

		this.orderItemRepository.save(o);

		List<OrderItem> result = this.orderItemRepository.findByBillingIsNullAndStatesStateNotIn(Arrays.asList(finshedStates));

		Assert.assertTrue(result.size() == 1);

		// check if a paid order item doesnt affect the result

		o = new OrderItem();
		o.getStates().clear();
		o = this.orderItemRepository.save(o);

		Billing billing = new Billing();
		billing.getItems().add(new BillingItem(o, 1));

		this.billingRepository.save(billing);

		result = this.orderItemRepository.findByBillingIsNullAndStatesStateNotIn(Arrays.asList(finshedStates));

		Assert.assertTrue(result.size() == 1);

	}

	/**
	 * test custom order aggregation, grouped by menu item
	 */
	@Test
	public void testGroupedNotPayedOrdersByItemForDesk() {
		MenuItem test = new MenuItem();
		test.setName("test");

		Desk d = new Desk();
		d = this.deskRepository.save(d);

		test = this.menuItemRepository.save(test);

		OrderItem o = new OrderItem();
		o.setItem(test);
		o.setDesk(d);
		this.orderItemRepository.save(o);

		o = new OrderItem();
		o.setDesk(d);
		o.setItem(test);
		this.orderItemRepository.save(o);

		List<OrderDeskAggregationDto> result = this.orderItemRepository.getGroupedNotPayedOrdersByItemForDesk(d);

		Assert.assertTrue(result.size() == 1);

		Assert.assertTrue(result.get(0).getCount() == 2);

		Logger.getAnonymousLogger().info(result.get(0).getItem().toString());

		// test that payed item doesn't affect aggregation

		OrderItem payedOrder = new OrderItem();
		payedOrder.setItem(test);
		payedOrder.setDesk(d);

		payedOrder = this.orderItemRepository.save(payedOrder);

		Billing billing = new Billing();
		billing.getItems().add(new BillingItem(payedOrder, 0));

		this.billingRepository.save(billing);

		result = this.orderItemRepository.getGroupedNotPayedOrdersByItemForDesk(d);

		Assert.assertTrue(result.size() == 1);

		Assert.assertTrue(result.get(0).getCount() == 2);

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
		this.billingRepository.deleteAll();
	}

}
