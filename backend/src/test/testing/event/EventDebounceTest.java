package testing.event;

import de.neoventus.persistence.event.EventDebounce;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import testing.AbstractTest;

import java.util.function.Function;
import java.util.logging.Logger;

/**
 * unit test for
 *
 * @see EventDebounce
 */
public class EventDebounceTest extends AbstractTest {

	@Autowired
	private EventDebounce eventDebounce;

	private int eventCount = 0;

	/**
	 * test if debounce functions properly
	 */
	@Test
	public void testDebounceExecution() {
		methodToDebounce();
		methodToDebounce();
		methodToDebounce();

		// sleep to make sure cb is activated
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Assert.assertTrue(this.eventCount == 3);
	}

	/**
	 * test method to debounce
	 */
	private void methodToDebounce() {
		eventDebounce.debounce("test", 500, new Function<Integer, Void>() {
			@Override
			public Void apply(Integer integer) {
				Logger.getAnonymousLogger().info("Debounce cb execution");
				eventCount = integer;
				return null;
			}
		});
	}

}
