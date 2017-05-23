package de.neoventus.persistence.event;

import de.neoventus.persistence.entity.*;
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
 */
@Component
public class OrderLifecycleEvents extends AbstractMongoEventListener<OrderItem> {

	private OrderItemRepository orderItemRepository;

	private SimpMessagingTemplate simpMessagingTemplate;

	private EventDebounce eventDebounce;

	@Autowired
	public OrderLifecycleEvents(OrderItemRepository orderItemRepository, SimpMessagingTemplate simpMessagingTemplate,
								EventDebounce eventDebounce) {
		this.orderItemRepository = orderItemRepository;
		this.simpMessagingTemplate = simpMessagingTemplate;
		this.eventDebounce = eventDebounce;
	}

	@Override
	public void onAfterSave(AfterSaveEvent<OrderItem> event) {
		this.updateOrderSocket(event);
		this.updateUserNotificationSocket(event);
	}

	/**
	 * method to send open orders to socket but debounce to execute
	 * only once if the event is triggered many times in a short period
	 * @param event
	 */
	private void updateOrderSocket(MongoMappingEvent<OrderItem> event) {
		if (event.getSource().getItem() != null && event.getSource().getItem().getMenuItemCategory() != null) {
			String dest;
			boolean forKitchen = event.getSource().getItem().getMenuItemCategory().isForKitchen();
			if (forKitchen) {
				dest = "/topic/order/kitchen";
				eventDebounce.debounce("kitchen", 500, integer -> sendKitchenBarData(forKitchen, dest));
			} else {
				dest = "/topic/order/bar";
				eventDebounce.debounce("bar", 500, integer -> sendKitchenBarData(forKitchen, dest));
			}

		}
	}

	/**
	 * send actual kitchen bar data to socket
	 *
	 * @param forKitchen
	 * @param dest
	 * @return
	 */
	private Void sendKitchenBarData(boolean forKitchen, String dest) {
		Map<String, Object> data = new HashMap<>();
		data.put("desks", this.orderItemRepository.getUnfinishedOrdersForCategoriesGroupedByDeskAndOrderItem(forKitchen));
		data.put("items", this.orderItemRepository.getUnfinishedOrderForCategoriesGroupedByItem(forKitchen));

		this.simpMessagingTemplate.convertAndSend(dest, data);
		return null;
	}

	/**
	 * method to send notification to user
	 *
	 * @param event
	 */
	private void updateUserNotificationSocket(MongoMappingEvent<OrderItem> event) {
		OrderItem o = event.getSource();
		User waiter = o.getWaiter();
		Desk d = o.getDesk();

		if (waiter != null & o.getCurrentState() != OrderItemState.State.NEW) {

			switch (o.getCurrentState()) {
				case FINISHED:
					// debounce finish events to get a single notification if more orders are finished
					// make sure to debunce for every waiter different -> use waiter id in debounce key
					// also different debounce if event comes from kitchebn or from bar
					String prefix = "";
					final MenuItemCategory category = o.getItem().getMenuItemCategory();
					if (category != null) {
						prefix = category.isForKitchen() ? "kitchen" : "bar";
					}
					eventDebounce.debounce(prefix + "-finished-" + waiter.getId(), 2000, count -> {
						String notification = "";

						if (category.isForKitchen()) {
							notification = "Küche: ";
						} else {
							notification = "Bar: ";
						}

						// different messages depending on count
						if (count > 1) {
							notification += "Mehrere Bestellungen für Tisch " + d.getNumber() + " fertig";
						} else {
							notification += "Bestellung für Tisch " + d.getNumber() + " fertig";
						}

						this.simpMessagingTemplate.convertAndSend("/topic/user/" + waiter.getId(), notification);
						return null;
					});
					break;
				case CANCELED:
					// on cancel send single notification for every team
					// todo send notification only if waiter is different than who sent the cancel request
					String notification = o.getItem().getName() + " für Tisch " + d.getNumber() + " wurde storniert";
					this.simpMessagingTemplate.convertAndSend("/topic/user/" + waiter.getId(), notification);
					break;
			}
		}
	}
}
