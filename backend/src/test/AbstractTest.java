import de.neoventus.Application;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public abstract class AbstractTest {
	
	/**
	 * set active spring profile to testing db
	 */
	@BeforeClass
	public static void setTestingDBProfile() {
		System.setProperty("spring.profiles.active", "testing");
	}
}
