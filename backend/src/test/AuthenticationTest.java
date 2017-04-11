import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * testing for the configured authentication
 *
 * @author Dennis Thanner
 * @version 0.0.1
 **/
public class AuthenticationTest extends AbstractTest {

	@Autowired
	private UserRepository userRepository;

	/**
	 * test if user login returns http status 200
	 */
	@Test
	public void testReturnLoginSuccess() throws Exception {
		User u = new User();
		u.setUsername("Test");
		u.setPassword(new BCryptPasswordEncoder().encode("test"));

		userRepository.save(u);

		this.mockMvc.perform(formLogin().user("Test").password("test"))
				.andExpect(status().isOk());

	}

	/**
	 * test if bad user login returns http status 401
	 */
	@Test
	public void testReturnLoginError() throws Exception {
		this.mockMvc.perform(formLogin().password("invalid")).andExpect(status().isUnauthorized());
	}


	@After
	public void clear() {
		userRepository.deleteAll();
	}
}
