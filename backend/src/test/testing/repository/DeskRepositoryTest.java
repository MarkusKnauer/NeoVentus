package testing.repository;

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;
import de.neoventus.persistence.repository.advanced.impl.aggregation.DeskOverviewDetails;
import de.neoventus.rest.dto.DeskDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import testing.AbstractTest;

import java.util.Date;
import java.util.List;

/**
 * testing the desk repository methods
 *
 * @author Dominik Streif, Dennis Thanner
 * @version 0.0.3 added find by number test
 *          0.0.2 extended before save event test - DT
 */
public class DeskRepositoryTest extends AbstractTest {

	@Autowired
	private DeskRepository deskRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@Test
	public void testSearchByNumber() {
		Desk d = new Desk();
		Integer testNumber = new Integer(4711);

		d.setNumber(testNumber);
		deskRepository.save(d);

		d = deskRepository.findByNumber(testNumber);

		Assert.assertNotNull(d);

		Assert.assertTrue(d.getNumber().equals(testNumber));
	}

	/**
	 * test the custom implementation method for saving desks by dto
	 *
	 * @see de.neoventus.persistence.repository.advanced.impl.DeskRepositoryImpl#save(DeskDto)
	 */
	@Test
	public void testSaveByDto() {

		DeskDto dto = new DeskDto();
		dto.setSeats(new Integer(4711));

		deskRepository.save(dto);

		Desk d = deskRepository.findFirstByOrderByNumberDesc();
		Assert.assertEquals(dto.getSeats(), d.getSeats());

	}

	/**
	 * test if the specified before save event works
	 *
	 * @see de.neoventus.persistence.event.DeskLifecycleEvents#onBeforeSave(BeforeSaveEvent)
	 */
	@Test
	public void testBeforeSaveEvent() {
		Desk d3 = new Desk();

		d3 = deskRepository.save(d3);

		Assert.assertTrue(d3.getNumber() == 1);

		Desk d2 = new Desk();
		d2.setNumber(4711);

		deskRepository.save(d2);


		Desk d = new Desk();

		d = deskRepository.save(d);

		Assert.assertTrue(d.getNumber() == 4712);

	}

	/**
	 * testing findByNumber method
	 */
	@Test
	public void findByNumber() {
		Desk d = new Desk();
		deskRepository.save(d);

		Desk r = deskRepository.findByNumber(1);
		Assert.assertNotNull(r);

		d = new Desk();
		deskRepository.save(d);

		r = deskRepository.findByNumber(2);
		Assert.assertNotNull(r);
	}

	@Test
	public void testDeskOverview() {
		Desk d = new Desk();
		d = this.deskRepository.save(d);

		MenuItem m = new MenuItem();
		m.setPrice(2.5);
		m = this.menuItemRepository.save(m);

		User u = new User();
		u.setUsername("Test");
		u = this.userRepository.save(u);

		OrderItem o = new OrderItem();
		o.setDesk(d);
		o.setWaiter(u);
		o.setItem(m);
		this.orderItemRepository.save(o);

		Date now = new Date();

		Reservation r = new Reservation();
		r.setDesk(d);
		r.setTime(new Date(now.getTime() + 2000));
		this.reservationRepository.save(r);

		r = new Reservation();
		r.setDesk(d);
		r.setTime(new Date(now.getTime() + 3000));
		this.reservationRepository.save(r);

		List<DeskOverviewDetails> result = this.deskRepository.getDesksWithDetails();

		Assert.assertTrue(result.size() == 1);
		Assert.assertTrue(result.get(0).getTotalPaid() == 2.5);
		Assert.assertTrue(result.get(0).getWaiters().size() == 1);
		Assert.assertTrue(result.get(0).getWaiters().contains("Test"));
		Assert.assertTrue(result.get(0).getNextReservation().getTime() == now.getTime() + 2000);

	}

	/**
	 * clear the data written
	 */
	@After
	public void deleteAll() {
		this.deskRepository.deleteAll();
		this.userRepository.deleteAll();
		this.orderItemRepository.deleteAll();
		this.menuItemRepository.deleteAll();
		this.reservationRepository.deleteAll();
	}
}
