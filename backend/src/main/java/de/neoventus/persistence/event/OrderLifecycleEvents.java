package de.neoventus.persistence.event;

import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.MongoMappingEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
		this.updateSocket(event);
	}

	/**
	 * method to send open orders to socket
	 */
	private void updateSocket(MongoMappingEvent<OrderItem> event) {
		if (event.getSource().getItem() != null && event.getSource().getItem().getMenuItemCategory() != null) {
			String dest;
			Map<String, Object> data = new HashMap<>();
			boolean forKitchen = event.getSource().getItem().getMenuItemCategory().isForKitchen();
			if (forKitchen) {
				dest = "/topic/order/kitchen";
			} else {
				dest = "/topic/order/bar";
			}
			data.put("desks", this.orderItemRepository.getUnfinishedOrdersForCategoriesGroupedByDeskAndOrderItem(forKitchen));
			data.put("items", this.orderItemRepository.getUnfinishedOrderForCategoriesGroupedByItem(forKitchen));

			this.simpMessagingTemplate.convertAndSend(dest, data);
		}
	}

}
