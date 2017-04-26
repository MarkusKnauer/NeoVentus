package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.BillingItem;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.advanced.NVBillingItemRepository;
import de.neoventus.rest.dto.BillingItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Tim Heidelbach
 * @version 0.0.1
 */
@Repository
public class BillingItemRepositoryImpl implements NVBillingItemRepository {

	private MongoTemplate mongoTemplate;

	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void save(BillingItemDto dto) {

		BillingItem billingItem;
		if (dto.getId() != null) {
			billingItem = mongoTemplate.findById(dto.getId(), BillingItem.class);
		} else {
			billingItem = new BillingItem();
		}

		billingItem.setItem(mongoTemplate.findById(dto.getId(), OrderItem.class));
		billingItem.setPrice(dto.getPrice());

		mongoTemplate.save(billingItem);

	}
}
