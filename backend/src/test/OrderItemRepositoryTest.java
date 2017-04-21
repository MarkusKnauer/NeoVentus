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

/**
 * testing the user repository methods
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
	@Test
	public void testSearchByName() {
		OrderItem o = new OrderItem();
		/*BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		o.setOrderID(1);
		Desk desk = new Desk(3);
		desk.setNumber(1);
		deskRepository.save(desk);

		User waiter =new User("Karl", bCryptPasswordEncoder.encode("karlsson"), Permission.SERVICE);
		o.setWaiter(waiter);
		o.setDesk(desk);


		userRepository.save(waiter);
*/
		orderItemRepository.save(o);

		//o = orderItemRepository.findByOrderID(1);

		//Assert.assertNotNull(o);

		Assert.assertTrue(o.getOrderID()==1);
	}

	/**
	 * test the custom implementation method for saving users by dto
	 *
	 * @see de.neoventus.persistence.repository.advanced.impl.OrderItemRepositoryImpl#save(OrderItemDto)
	 */
	@Test
	public void testSaveByDto() {
		OrderItemDto dto = new OrderItemDto();
		dto.setOrderID(1);
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		dto.setOrderID(1);
		dto.setWaiter(1);
		dto.setDeskNumber(1);

		orderItemRepository.save(dto);

		OrderItem u = orderItemRepository.findByOrderID(1);

		Assert.assertNotNull(u);
	}

	/**
	 * test if the specified before save event works
	 *
	 * @see de.neoventus.persistence.event.UserLifecycleEvents#onBeforeSave(BeforeSaveEvent)
	 */
	@Test
	public void testBeforeSaveEvent() {
		OrderItem o2 = new OrderItem();
		Desk desk = new Desk(14);
		MenuItem item = new MenuItem();
//		deskRepository.save(desk);
//		menuItemRepository.setDefaultMenu();
		User user = new User();
		//BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		user.setUsername("Karl");

		o2.setOrderID(3);

		o2.setWaiter(user);
		o2.setDesk(desk);
		o2.setItem(menuItemRepository.findByNumber(1));
		orderItemRepository.save(o2);


		OrderItem o = new OrderItem();

		BCryptPasswordEncoder bCryptPasswordEncoder2 = new BCryptPasswordEncoder();
		o.setOrderID(4);
		o.setWaiter(new User("KarlTest", bCryptPasswordEncoder2.encode("testson"), Permission.SERVICE));
		o.setDesk(new Desk(24));
		o = orderItemRepository.save(o);

		Assert.assertTrue(o.getOrderID() == 4);

	}

	/**
	 * clear the data written
	 */
	@After
	public void deleteAll() {
		orderItemRepository.deleteAll();
	}

}
