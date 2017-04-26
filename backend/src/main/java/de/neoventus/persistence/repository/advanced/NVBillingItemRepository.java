package de.neoventus.persistence.repository.advanced;

import de.neoventus.rest.dto.BillingItemDto;

/**
 * @author Tim Heidelbach
 * @version 0.0.1
 */
public interface NVBillingItemRepository {

	/**
	 * convenience method to update or create billingItems by dto
	 *
	 * @param dto the BillingItem data transfer object
	 */
	void save(BillingItemDto dto);
}
