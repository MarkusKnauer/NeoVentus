import de.neoventus.Application;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * basic test class
 *
 * @author Dennis Thanner
 * @version 0.0.2 added mock mvc
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public abstract class AbstractTest {

	@Autowired
	protected WebApplicationContext wac;

	protected MockMvc mockMvc;

	/**
	 * setup mock mvc to perform mocked requests
	 */
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(this.wac)
				.apply(springSecurity())
				.build();
	}


	/**
	 * set active spring profile to testing db
	 */
	@BeforeClass
	public static void setTestingDBProfile() {
		System.setProperty("spring.profiles.active", "testing");
	}
}
