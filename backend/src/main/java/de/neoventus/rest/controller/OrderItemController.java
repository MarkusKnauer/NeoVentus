package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.entity.OrderItemState;
import de.neoventus.persistence.repository.DeskRepository;
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
import java.util.logging.Logger;

/**
 * REST controller for entity Order
 *
 * @author Julian Beck, Dennis Thanner
 * @version 0.0.8 corrected GET-Method /all/open - DS
 * 			0.0.7 url and method refactoring - DT
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
			Iterable<OrderDeskAggregationDto> result = this.orderRepository.getGroupedNotPayedOrdersByItemForDesk(desk);
			LOGGER.info(result.toString());
			return result;
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
	@GetMapping("/all/open")
	public Iterable<OrderItem> listAllOpenOrders(HttpServletResponse response) {
		try {
			Iterable<OrderItem> list = orderRepository.findAll();
			ArrayList<OrderItem> tmp = new ArrayList<OrderItem>();
			// get only the orderItems with the correct state
			for (OrderItem item : list) {
				if (item.getCurrentState().equals(OrderItemState.State.NEW)) {
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
	 * @param dto
	 * @param bindingResult
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void insert(@RequestBody @Valid OrderItemDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				orderRepository.save(dto);
				LOGGER.info("Saving order to database: " + dto.getId());
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
	 * @param id
	 */
	@RequestMapping(value = "/finish/{id}", method = RequestMethod.PUT)
	public void finishOrder(HttpServletResponse response, @PathVariable String id) {
		try {
			this.updateOrderState(id, OrderItemState.State.FINISHED);
		} catch (IllegalArgumentException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		}
	}

	/**
	 * controller method to mark order as canceled
	 *
	 * @param response
	 * @param id
	 */
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PUT)
	public void cancelOrder(HttpServletResponse response, @PathVariable String id) {
		try {
			this.updateOrderState(id, OrderItemState.State.CANCELED);
		} catch (IllegalArgumentException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		}
	}

	/**
	 * update order to add specific state
	 *
	 * @param orderId
	 * @param state
	 */
	private void updateOrderState(String orderId, OrderItemState.State state) throws IllegalArgumentException {
		OrderItem o = this.orderRepository.findOne(orderId);

		if (o == null) {
			throw new IllegalArgumentException("Invalid order id");
		}

		o.addState(OrderItemState.State.FINISHED);

		this.orderRepository.save(o);
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
