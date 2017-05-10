package de.neoventus.persistence.event;

import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.entity.OrderItemState;
import de.neoventus.persistence.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * order lifecycle events
 *
 * @author Dennis Thanner
 * @version 0.0.1
 */
@Component
public class OrderLifecycleEvents extends AbstractMongoEventListener<OrderItem> {

	private OrderItemRepository orderItemRepository;

	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	public OrderLifecycleEvents(OrderItemRepository orderItemRepository, SimpMessagingTemplate simpMessagingTemplate) {
		this.orderItemRepository = orderItemRepository;
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	@Override
	public void onAfterSave(AfterSaveEvent<OrderItem> event) {
		this.updateSocket();
	}

	/**
	 * method to send open orders to socket
	 */
	private void updateSocket() {
		this.simpMessagingTemplate.convertAndSend("/topic/order",
			this.orderItemRepository.findByBillingIsNullAndStatesStateNotIn(
				Arrays.asList(OrderItemState.State.FINISHED, OrderItemState.State.CANCELED)
			)
		);
	}

}
