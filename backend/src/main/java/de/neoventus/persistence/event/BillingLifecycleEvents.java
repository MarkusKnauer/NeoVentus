package de.neoventus.persistence.event;

import de.neoventus.persistence.entity.Billing;
import de.neoventus.persistence.entity.BillingItem;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.BillingRepository;
import de.neoventus.persistence.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.MongoMappingEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * life cycle events for billings
 *
 * @author Dennis Thanner
 * @version 0.0.1
 */
@Component
@SuppressWarnings("deprecation")
public class BillingLifecycleEvents extends AbstractMongoEventListener<Billing> {

	private OrderItemRepository orderItemRepository;

	private BillingRepository billingRepository;

	@Autowired
	public BillingLifecycleEvents(OrderItemRepository orderItemRepository, BillingRepository billingRepository) {
		this.orderItemRepository = orderItemRepository;
		this.billingRepository = billingRepository;
	}

	/**
	 * after save event listener
	 *
	 * @param event
	 */
	@Override
	public void onAfterSave(AfterSaveEvent<Billing> event) {
		this.ensureBidirectional(event);
	}

	/**
	 * set bi-directional connection
	 *
	 * @param event
	 */
	private void ensureBidirectional(MongoMappingEvent<Billing> event) {
		Billing src = event.getSource();

		List<OrderItem> srcOrders = new ArrayList<>();
		for (BillingItem item : src.getItems()) {
			srcOrders.add(item.getItem());
		}

		List<OrderItem> oldSrcOrders = this.orderItemRepository.findByBilling(src);

		// update old
		for (OrderItem oldOrderItem : oldSrcOrders) {
			if (!srcOrders.contains(oldOrderItem)) {
				oldOrderItem.setBilling(null);
				this.orderItemRepository.save(oldOrderItem);
			}
		}

		// update new
		for (OrderItem newOrderItem : srcOrders) {
			if (newOrderItem.getBilling() == null || !newOrderItem.getBilling().equals(src)) {
				newOrderItem.setBilling(src);
				this.orderItemRepository.save(newOrderItem);
			}
		}

	}

}
