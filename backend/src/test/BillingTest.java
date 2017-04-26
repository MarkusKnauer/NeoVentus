import de.neoventus.persistence.entity.Billing;
import de.neoventus.persistence.repository.BillingRepository;
import de.neoventus.rest.dto.BillingDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Testing the billing repository methods
 *
 * @author Tim Heidelbach
 * @version 0.0.1
 */
public class BillingTest extends AbstractTest {

	private BillingRepository billingRepository;

	@Autowired
	void setBillingRepository(BillingRepository billingRepository) {
		this.billingRepository = billingRepository;
	}

	@Test
	public void testSaveByDto() {


		BillingDto dto = new BillingDto();
		dto.setItems(new ArrayList<>());
		dto.setTotalPaid(13.37);
		billingRepository.save(dto);

		Billing billing = null;

		Iterable<Billing> items = billingRepository.findAll();

		for (Billing item : items) {
			if (item.getTotalPaid() == 13.37) {
				billing = item;
				break;
			}
		}

		Assert.assertNotNull(billing);
	}

	/**
	 * clear the data written
	 */
	@After
	public void deleteAll() {
		billingRepository.deleteAll();
	}

}
