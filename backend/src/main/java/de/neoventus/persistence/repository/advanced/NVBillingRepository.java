package de.neoventus.persistence.repository.advanced;

import de.neoventus.persistence.repository.advanced.impl.aggregation.ObjectRevenueAggregation;
import de.neoventus.rest.dto.BillingDto;

import java.util.List;


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

	/**
	 * return this years quarters revenues
	 */
	List<ObjectRevenueAggregation> getQuartersRevenue();

	/**
	 * return this months top 10 waiters
	 *
	 * @return
	 */
	List<ObjectRevenueAggregation> getTop10Waiters();

}
