import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.rest.dto.DeskDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

/**
 * testing the desk repository methods
 *
 * @author Dominik Streif, Dennis Thanner
 * @version 0.0.2 extended before save event test - DT
 */
public class DeskRepositoryTest extends AbstractTest {

	@Autowired
	private DeskRepository deskRepository;

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
	 * clear the data written
	 */
	@After
	public void deleteAll() {
		deskRepository.deleteAll();
	}
}
