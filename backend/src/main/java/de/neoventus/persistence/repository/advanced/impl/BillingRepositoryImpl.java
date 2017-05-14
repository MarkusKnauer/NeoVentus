package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.Billing;
import de.neoventus.persistence.entity.BillingItem;
import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.persistence.repository.advanced.NVBillingRepository;
import de.neoventus.rest.dto.BillingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author Julian Beck, Dennis Thanner
 * @version 0.0.5 multiple side dish support - DT
 *          0.0.4 added side dish to price calc - DT
 *          0.0.3 billing item refactoring - DT
 *          0.0.2 redundancy clean up - DT
 **/
@Repository
public class BillingRepositoryImpl implements NVBillingRepository {

	private MongoTemplate mongoTemplate;

	private OrderItemRepository orderItemRepository;

	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Autowired
	public void setOrderItemRepository(OrderItemRepository orderItemRepository) {
		this.orderItemRepository = orderItemRepository;
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

		for (String orderId : dto.getItems()) {
			OrderItem order = this.orderItemRepository.findOne(orderId);
			int sideDishesPrice = 0;
			for (MenuItem sideDish : order.getSideDishes()) {
				sideDishesPrice += sideDish.getPrice();
			}

			billing.getItems().add(new BillingItem(
				order,
				order.getItem().getPrice() + sideDishesPrice
			));
		}

		billing.getItems().clear();

		mongoTemplate.save(billing);
	}
}
