package testing;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.logging.Logger;

/**
 * unit test for checking on profile switch
 *
 * @author Dennis Thanner
 * @version 0.0.1
 **/
public class TestSpringProfile extends AbstractTest {

	@Value("${spring.data.mongodb.database}")
	private String databaseName;

	/**
	 * testing if testing profile is activated
	 */
	@Test
	public void testProfileDatabaseNameProperty() {
		Logger.getAnonymousLogger().info(databaseName);
		Assert.assertTrue(databaseName.equals("NeoVentus_test"));
	}

}
