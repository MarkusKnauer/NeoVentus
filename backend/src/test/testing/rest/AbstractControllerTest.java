package testing.rest;

import de.neoventus.persistence.entity.Permission;
import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import testing.AbstractTest;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * base class for mvc controller tests
 *
 * @author Dennis Thanner
 * @version 0.0.1
 */
public abstract class AbstractControllerTest extends AbstractTest {

	protected MockHttpSession authSession;

	@Autowired
	private UserRepository userRepository;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	/**
	 * create test user and create auth session
	 *
	 * @throws Exception
	 */
	@Before
	public void login() throws Exception {

		User u = new User();
		u.setUsername("Test");
		u.setPermissions(Arrays.asList(Permission.CEO, Permission.ADMIN));
		u.setPassword(new BCryptPasswordEncoder().encode("test"));

		userRepository.save(u);

		this.authSession = (MockHttpSession) this.mockMvc.perform(formLogin().user("Test").password("test"))
			.andExpect(status().isOk()).andReturn().getRequest().getSession();
	}


	/**
	 * convert object to json string
	 *
	 * @param o
	 * @return
	 * @throws IOException
	 */
	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(
			o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

	/**
	 * clear data
	 */
	@After
	public void clear() {
		userRepository.deleteAll();
	}

	/**
	 * set converter for object to json conversion
	 *
	 * @param converters
	 */
	@Autowired
	private void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
			.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
			.findAny()
			.orElse(null);

		assertNotNull("the JSON message converter must not be null",
			this.mappingJackson2HttpMessageConverter);
	}


}
