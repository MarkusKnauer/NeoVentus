import de.neoventus.persistence.entity.BillingItem;
import de.neoventus.persistence.repository.BillingItemRepository;
import de.neoventus.rest.dto.BillingItemDto;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Testing the billing item repository methods
 *
 * @author Tim Heidelbach
 * @version 0.0.1
 */
public class BillingItemTest extends AbstractTest {

	private BillingItemRepository billingItemRepository;

	@Autowired
	void setBillingItemRepository(BillingItemRepository billingItemRepository) {
		this.billingItemRepository = billingItemRepository;
	}

	@Test
	public void testSaveByDto() {

		BillingItemDto dto = new BillingItemDto();
		dto.setItem("OrderItemTestId");
		dto.setPrice(13.37);
		billingItemRepository.save(dto);

		BillingItem billingItem = null;

		Iterable<BillingItem> items = billingItemRepository.findAll();

		for (BillingItem item : items) {
			if (item.getPrice() == 13.37) {
				billingItem = item;
				break;
			}
		}

		Assert.assertNotNull(billingItem);
	}

	/**
	 * clear the data written
	 */
	@After
	public void deleteAll() {
		billingItemRepository.deleteAll();
	}
}
