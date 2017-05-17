package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.entity.OrderItemState;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.MenuItemCategoryRepository;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.OrderDeskAggregationDto;
import de.neoventus.rest.dto.OrderItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * REST controller for entity Order
 *
 * @author Julian Beck, Dennis Thanner
 * @version 0.0.91 added multi batch support for insert and order state - DT
 *          0.0.9 GetMapping /all/open/meals
 *          0.0.8 corrected GET-Method /all/open - DS
 *          0.0.7 url and method refactoring - DT
 *          0.0.6 added finish and cancel methods, removed socket update to event listener - DT
 *          0.0.5 No key-Value-pairs and refactor GET-Method
 *          0.0.4 Name-Value-Pair for GET and OrderitemOutput for specific frontend-data
 *          0.0.3 added socket support - DT
 *          0.0.2 redundancy clean up - DT
 */
@RestController
@RequestMapping("/api/order")
public class OrderItemController {

	private final static Logger LOGGER = Logger.getLogger(OrderItemController.class.getName());

	private final OrderItemRepository orderRepository;
	private DeskRepository deskRepository;
	private MenuItemCategoryRepository menuItemCategoryRepository;


	@Autowired
	public OrderItemController(OrderItemRepository orderRepository, DeskRepository deskRepository) {
		this.orderRepository = orderRepository;
		this.deskRepository = deskRepository;
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
			e.printStackTrace();
			LOGGER.warning("Error searching order by deskNumber " + 1 + ": " + e.getMessage());
			//response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}

	}

	/**
	 * controller method to list all orders
	 *
	 * @param response
	 * @param
	 * @return
	 */
	@GetMapping("/all/open/meals")
	public Iterable<OrderItem> listAllOpenOrders(HttpServletResponse response) {
		try {
			Iterable<OrderItem> list = orderRepository.findByBillingIsNullAndStatesStateNotIn(
				Arrays.asList(OrderItemState.State.FINISHED, OrderItemState.State.CANCELED)
			);

			ArrayList<OrderItem> tmp = new ArrayList<>();
			for (OrderItem item : list) {
				String cat = item.getItem().getMenuItemCategory().getName();

				switch (cat) {
					case "Warme Vorspeise":
					case "Kalte Vorspeise":
					case "Suppen":
					case "Fischgerichte":
					case "Fleischgerichte":
					case "Vegetarische Gerichte":
					case "Kinder Gerichte":
						tmp.add(item);
				}
			}

			list = tmp;


			return list;

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
	 * @param response
	 * @param ids
	 */
	@RequestMapping(value = "/finish/{ids}", method = RequestMethod.PUT)
	public void finishOrder(HttpServletResponse response, @PathVariable String ids) {
		try {
			this.updateOrderState(ids, OrderItemState.State.FINISHED);
		} catch (IllegalArgumentException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		}
	}

	/**
	 * controller method to mark order as canceled
	 *
	 * @param response
	 * @param ids
	 */
	@RequestMapping(value = "/cancel/{ids}", method = RequestMethod.PUT)
	public void cancelOrder(HttpServletResponse response, @PathVariable String ids) {
		try {
			this.updateOrderState(ids, OrderItemState.State.CANCELED);
		} catch (IllegalArgumentException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		}
	}

	/**
	 * update order to add specific state
	 *
	 * @param orderIds
	 * @param state
	 * @throws IllegalArgumentException
	 */
	private void updateOrderState(String orderIds, OrderItemState.State state) throws IllegalArgumentException {
		for (String id : orderIds.split(",")) {
			OrderItem o = this.orderRepository.findOne(id);

			if (o != null) {
				o.addState(state);

				this.orderRepository.save(o);
			}
		}
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
