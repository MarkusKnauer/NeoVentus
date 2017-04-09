import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * testing the user repository methods
 *
 * @author Dennis Thanner
 * @version 0.0.1
 **/
public class UserRepositoryTest extends AbstractTest {

	@Autowired
	private UserRepository userRepository;

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
	 * clear the data written
	 */
	@After
	public void deleteAll() {
		userRepository.deleteAll();
	}

}
