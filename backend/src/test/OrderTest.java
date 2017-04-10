/**
 * Created by julian on 09.04.2017.
 */

import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.OrderItemRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * class for test-data
 *
 * @author Julian Beck
 * @version 0.0.1
 **/
public class OrderTest extends AbstractTest {

	@Autowired
	private OrderItemRepository orderRepository;
	private DeskRepository deskRepository;

	@Test
	public void testSearchByName() {
		OrderItem order = new OrderItem();
		// Desk des = deskRepository.findByDeskNumber(1);
		//order.setDesk(new Desk());
		orderRepository.save(order);

		//order = orderRepository.findByUsername("Test 1");

		Assert.assertNotNull(order);

		//Assert.assertTrue(order.getUsername().equals("Test 1"));
	}

	/**
	 * clear the data written
	 */
	@After
	public void deleteAll() {

		orderRepository.deleteAll();
	}

}

