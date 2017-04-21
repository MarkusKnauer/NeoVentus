package de.neoventus.persistence.event;

import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

/**
 * class for handling entity events for OrderItems
 *
 * @author Julian Beck
 * @version 0.0.1
 **/
@Component
public class OrderItemLifecycleEvents extends AbstractMongoEventListener<OrderItem> {

	private OrderItemRepository orderItemRepository;

	@Override
	public void onBeforeSave(BeforeSaveEvent<OrderItem> event) {
		// automatically increment set userId
		OrderItem orderItem = event.getSource();

		// only set number if not exists yet
		if(orderItem.getOrderID() == null) {
			OrderItem max = orderItemRepository.findFirstByOrderByOrderIDDesc();

			orderItem.setOrderID(max == null ? 1 : max.getOrderID() + 1);
		}
	}

	@Autowired
	public void setDeskRepository(OrderItemRepository orderItemRepository) {this.orderItemRepository=orderItemRepository;}
}
