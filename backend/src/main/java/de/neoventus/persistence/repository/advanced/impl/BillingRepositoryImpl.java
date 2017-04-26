package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.Billing;
import de.neoventus.persistence.repository.BillingItemRepository;
import de.neoventus.persistence.repository.advanced.NVBillingRepository;
import de.neoventus.rest.dto.BillingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Julian Beck, Dennis Thanner
 * @version 0.0.2 redundancy clean up - DT
 **/
@Repository
public class BillingRepositoryImpl implements NVBillingRepository {

	private MongoTemplate mongoTemplate;

	private BillingItemRepository billingItemRepository;

	@Autowired
	private void setBillingItemRepository(BillingItemRepository billingItemRepository) {
		this.billingItemRepository = billingItemRepository;
	}

	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void save(BillingDto dto) {

		Billing billing;
		if (dto.getId() != null) {
			billing = mongoTemplate.findById(dto.getId(), Billing.class);
		} else {
			billing = new Billing();
		}

		billing.setBilledAt(dto.getBilledAt());
		billing.setTotalPaid(dto.getTotalPaid());

		for (String itemId : dto.getItems()) {
			billing.getItems().add(billingItemRepository.findOne(itemId));
		}

		billing.getItems().clear();

		mongoTemplate.save(billing);
	}
}
