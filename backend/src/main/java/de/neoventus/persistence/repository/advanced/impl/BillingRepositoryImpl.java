package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.Billing;
import de.neoventus.persistence.entity.BillingItem;
import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.persistence.repository.UserRepository;
import de.neoventus.persistence.repository.advanced.NVBillingRepository;
import de.neoventus.rest.dto.BillingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;


/**
 * @author Julian Beck, Dennis Thanner
 **/
@Repository
public class BillingRepositoryImpl implements NVBillingRepository {

	private MongoTemplate mongoTemplate;

	private OrderItemRepository orderItemRepository;

	private UserRepository userRepository;

	@Autowired
	public BillingRepositoryImpl(MongoTemplate mongoTemplate, OrderItemRepository orderItemRepository, UserRepository userRepository) {
		this.mongoTemplate = mongoTemplate;
		this.orderItemRepository = orderItemRepository;
		this.userRepository = userRepository;
	}

	@Override
	public void save(BillingDto dto) {

		Billing billing;
		if (dto.getId() != null) {
			billing = mongoTemplate.findById(dto.getId(), Billing.class);
		} else {
			billing = new Billing();
		}

		billing.setTotalPaid(dto.getTotalPaid());

		if (dto.getWaiter() != null)
			billing.setWaiter(this.userRepository.findOne(dto.getWaiter()));

		billing.getItems().clear();
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


		mongoTemplate.save(billing);
	}

}
