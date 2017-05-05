package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.rest.dto.OrderItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

/**
 * REST controller for entity Order
 *
 * @author Julian Beck, Dennis Thanner
 * @version 0.0.2 redundancy clean up - DT
 */
@RestController
@RequestMapping("/api/order")
public class OrderItemController {

	private final static Logger LOGGER = Logger.getLogger(OrderItemController.class.getName());

	private final OrderItemRepository orderRepository;
	private DeskRepository deskRepository;


	@Autowired
	public OrderItemController(OrderItemRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	/**
	 * controller method to list all orderItems
	 *
	 * @param response the response
	 * @return all orders
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<OrderItem> getAllOrders(HttpServletResponse response) {
		try {
			return orderRepository.findAll();
		} catch (DataAccessException e) {
			LOGGER.warning("Error searching for all orders: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}

	/**
	 * controller method to list order details
	 *
	 * @param response
	 * @param deskNumber
	 * @return
	 */
	@RequestMapping(value = "/{deskNumber}", method = RequestMethod.GET)
	public Iterable<OrderItem> listOrderDesk(HttpServletResponse response, @PathVariable String deskNumber) {
		try {
			LOGGER.info("THIS IS BACKEND - OrderController: DeskNumber: "+deskNumber);
			Desk desk =deskRepository.findByNumber(Integer.parseInt(deskNumber));
			List<OrderItem> list =orderRepository.findAllOrderItemByDeskIdOrderByItemId(desk.getId());

			for(OrderItem o: list) LOGGER.info("THIS IS BACKEND - OrderController: List: "+ o.getId());
			return list;

		} catch(Exception e) {
			LOGGER.warning("Error searching order by deskNumber " + deskNumber + ": " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}






	/**
	 * controller method to list order details
	 *
	 * @param response
	 * @param orderNumber
	 * @return
	 */
	/*
	@RequestMapping(value = "/{orderNumber}", method = RequestMethod.GET)
	public OrderItem listOrder(HttpServletResponse response, @PathVariable String orderNumber) {
		try {
			return orderRepository.findOne(orderNumber);
		} catch(Exception e) {
			LOGGER.warning("Error searching order by orderNumber " + orderNumber + ": " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}
	*/
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
		} catch(Exception e) {
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

	@Autowired
	public void setDeskRepository(DeskRepository deskRepository) {
		this.deskRepository = deskRepository;
	}


}
