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
 * @author Dominik Streif
 * @version 0.0.1
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
	//???? to do

	}

	/**
	 * test if the specified before save event works
	 *
	 * @see de.neoventus.persistence.event.UserLifecycleEvents#onBeforeSave(BeforeSaveEvent)
	 */
	@Test
	public void testBeforeSaveEvent() {
		Desk d2 = new Desk();
		d2.setNumber(new Integer(4711));

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
