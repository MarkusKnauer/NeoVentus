package de.neoventus.persistence.repository.advanced;

import de.neoventus.rest.dto.BillingDto;


/**
 * @author Julian Beck, Tim Heidelbach
 **/
public interface NVBillingRepository {

	/**
	 * convenience method to update or create user by dto
	 *
	 * @param dto the Billing data transfer object
	 */
	void save(BillingDto dto);

}
