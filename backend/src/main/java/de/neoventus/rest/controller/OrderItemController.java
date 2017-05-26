package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.entity.OrderItemState;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.persistence.repository.UserRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.OrderDeskAggregationDto;
import de.neoventus.rest.auth.NVUserDetails;
import de.neoventus.rest.dto.OrderItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * REST controller for entity Order
 *
 * @author Julian Beck, Dennis Thanner
 */
@RestController
@RequestMapping("/api/order")
public class OrderItemController {

	private final static Logger LOGGER = Logger.getLogger(OrderItemController.class.getName());

	private final OrderItemRepository orderRepository;
	private DeskRepository deskRepository;
	private UserRepository userRepository;


	@Autowired
	public OrderItemController(OrderItemRepository orderRepository, DeskRepository deskRepository,
							   UserRepository userRepository) {
		this.orderRepository = orderRepository;
		this.deskRepository = deskRepository;
		this.userRepository = userRepository;
	}

	/**
	 * controller method to list order details
	 * <p>
	 * <p>
	 * What is Love? Baby, Value has no Key, has no Key, no more...
	 */
	@RequestMapping(value = "/desk/open/{deskNumber:[0-9]*}", method = RequestMethod.GET)
	public Iterable<OrderDeskAggregationDto> listOrders(HttpServletResponse response, @PathVariable Integer deskNumber) {
		try {
			Desk desk = this.deskRepository.findByNumber(deskNumber);
			if (desk == null) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				return null;
			}
			return this.orderRepository.getGroupedNotPayedOrdersByItemForDesk(desk);
		} catch (Exception e) {
			LOGGER.warning("Error searching order by deskNumber " + 1 + ": " + e.getMessage());
			//response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}

	/**
	 * get details of a specific order
	 *
	 * @param orderId
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
	public OrderItem getOrder(@PathVariable String orderId, HttpServletResponse response) {
		OrderItem o = this.orderRepository.findOne(orderId);
		if (o == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		}
		return o;
	}

	/**
	 * controller method to list all orders grouped by desk and item
	 *
	 * @param response
	 * @param forKitchen
	 * @return
	 */
	@GetMapping("/unfinished/grouped/by-desk/{forKitchen}")
	public Map<Integer, List<OrderDeskAggregationDto>> listAllOpenOrdersByDesk(HttpServletResponse response, @PathVariable Integer forKitchen) {
		try {
			return this.orderRepository.getUnfinishedOrdersForCategoriesGroupedByDeskAndOrderItem(forKitchen == 1);
		} catch (Exception e) {
			LOGGER.warning("Error getting all orders: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}

	/**
	 * controller method to list all order grouped by item
	 *
	 * @param response
	 * @param forKitchen
	 * @return
	 */
	@GetMapping("/unfinished/grouped/by-item/{forKitchen}")
	public List<OrderDeskAggregationDto> listAllOpenOrdersByItem(HttpServletResponse response, @PathVariable Integer forKitchen) {
		try {
			return this.orderRepository.getUnfinishedOrderForCategoriesGroupedByItemOrderByCount(forKitchen == 1);
		} catch (Exception e) {
			LOGGER.warning("Error getting all orders: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}

	/**
	 * controller method for inserting OrderItem
	 *
	 * @param dtos
	 * @param bindingResult
	 * @param response
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void insert(@RequestBody @Valid OrderItemDto[] dtos, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				for (OrderItemDto dto : dtos) {
					orderRepository.save(dto);
					LOGGER.info("Saving order to database: " + dto.getId());
				}
			}
		} catch (Exception e) {
			LOGGER.warning("Error inserting order to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * controller method for updating order
	 *
	 * @param dto
	 * @param bindingResult
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody @Valid OrderItemDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				orderRepository.save(dto);
				LOGGER.info("Update order to database: " + dto.getId());
			}
		} catch (Exception e) {
			LOGGER.warning("Error updating order to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * controller method to mark order as finished
	 *
	 * @param response, ids
	 * @param ids
	 */
	@RequestMapping(value = "/finish", method = RequestMethod.PUT)
	public void finishOrder(@RequestBody String ids, HttpServletResponse response) {
		try {
			this.updateOrderState(ids, OrderItemState.State.FINISHED);
		} catch (IllegalArgumentException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		}
	}

	/**
	 * controller method to mark order as canceled
	 *
	 * @param response, ids
	 * @param ids
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.PUT)
	public void cancelOrder(@RequestParam String ids, @RequestParam String reason, @AuthenticationPrincipal NVUserDetails principal, HttpServletResponse response) {
		try {
			this.updateOrderState(ids, OrderItemState.State.CANCELED, reason, principal.getUserId());
		} catch (IllegalArgumentException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		}
	}

	/**
	 * update order to add specific state
	 *
	 * @param orderIds
	 * @param state
	 * @param reason
	 * @param userId
	 * @throws IllegalArgumentException
	 */
	private void updateOrderState(String orderIds, OrderItemState.State state, String reason, String userId) throws IllegalArgumentException {
		for (String id : orderIds.split(",")) {
			OrderItem o = this.orderRepository.findOne(id);

			if (o != null) {
				// todo check if already contains state
				if (reason == null && userId == null) {
					o.addState(state);
				} else {
					OrderItemState orderState = new OrderItemState(state);
					try {
						orderState.setReason(URLDecoder.decode(reason, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// set not decoded reason
						orderState.setReason(reason);
					}
					orderState.setWaiter(this.userRepository.findOne(userId));
					o.getStates().add(orderState);
				}
				this.orderRepository.save(o);
			}
		}
	}

	/**
	 * convenience method
	 *
	 * @param orderIds
	 * @param state
	 */
	private void updateOrderState(String orderIds, OrderItemState.State state) {
		this.updateOrderState(orderIds, state, null, null);
	}


	/**
	 * controller method for deleting order
	 *
	 * @param id
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public void delete(@RequestParam String id, HttpServletResponse response) {
		try {
			orderRepository.delete(id);
		} catch (Exception e) {
			LOGGER.warning("Error updating order to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

}
