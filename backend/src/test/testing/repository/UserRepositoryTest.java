package testing.repository;

import de.neoventus.persistence.entity.Permission;
import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.UserRepository;
import de.neoventus.rest.dto.UserDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import testing.AbstractTest;

/**
 * testing the user repository methods
 *
 * @author Dennis Thanner
 * @version 0.0.2 added testSaveByDto, test before save event
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


		User u = new User("test", "test", Permission.ADMIN);

		u = userRepository.save(u);

		Assert.assertTrue(u.getWorkerId() == 1);

	}

	/**
	 * clear the data written
	 */
	@After
	public void deleteAll() {
		userRepository.deleteAll();
	}

}
