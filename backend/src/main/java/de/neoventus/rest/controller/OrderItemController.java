package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.entity.OrderItemOutput;
import de.neoventus.persistence.entity.OrderItemState;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.rest.dto.OrderItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * REST controller for entity Order
 *
 * @author Julian Beck, Dennis Thanner
 * @version 0.0.4 Name-Value-Pair for GET and OrderitemOutput for specific frontend-data
 * 			0.0.3 added socket support - DT
 *          0.0.2 redundancy clean up - DT
 */
@RestController
@RequestMapping("/api/order")
public class OrderItemController {

	private final static Logger LOGGER = Logger.getLogger(OrderItemController.class.getName());

	private final OrderItemRepository orderRepository;
	private DeskRepository deskRepository;
	private SimpMessagingTemplate simpMessagingTemplate;


	@Autowired
	public OrderItemController(OrderItemRepository orderRepository, SimpMessagingTemplate simpMessagingTemplate, DeskRepository deskRepository) {
		this.orderRepository = orderRepository;
		this.simpMessagingTemplate = simpMessagingTemplate;
		this.deskRepository = deskRepository;
	}

	/**
	 * controller method to list order details
	 *
	 *
	 * Ready for "Namen-Wert-Paar"
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<?> listOrders(@RequestParam Map<String, String> queryParameters,
											 @RequestParam MultiValueMap<String, String> multiMap) {
		try {
			LOGGER.info("THIS IS BACKEND - OrderController: Params " + String.valueOf(queryParameters));
			List<OrderItem> list = null;
			List<OrderItemOutput> output = new ArrayList<OrderItemOutput>();
			Desk desk =null;

			// Check if Params are available
			if (!multiMap.isEmpty()){

				//Check "Namen-Wert-Paar"
				if (multiMap.containsKey("deskNumber") && multiMap.size()==1){
					LOGGER.info("THIS IS BACKEND - OrderControll: Key deskNumber: "+String.valueOf(queryParameters.get("deskNumber")));
					// pre-Selection
					// Select OrderItems by Desk,Item and Category
					desk = deskRepository.findByNumber(Integer.parseInt(multiMap.getFirst("deskNumber")));
					list = orderRepository.findAllOrderItemByDeskIdOrderByItemMenuItemCategoryId(desk.getId());

					// todo Refactoring - Build Object structure for JSON
					OrderItemOutput tmp;

					Integer counter = 0;
					for(int i = 0; i < list.size();i++){
						counter = 1;
						tmp = new OrderItemOutput();
						tmp.addOrderItemIds(list.get(i).getId());
						tmp.setDesk(multiMap.getFirst("deskNumber"));
						tmp.setWaiter(list.get(i).getWaiter().getUsername());
						tmp.setCategory(list.get(i).getItem().getMenuItemCategory().getName());
						tmp.setGuestWish(list.get(i).getGuestWish());
						tmp.setMenuItem(list.get(i).getItem().getName());
						tmp.setMenuItemCounter(counter);
						tmp.setPrice(list.get(i).getItem().getPrice());

						int j = i+1;
						while( j < list.size() && (list.get(i).getItem().getMenuItemCategory().getName()).equals((list.get(j).getItem().getMenuItemCategory().getName())) )	{
							counter++;
							tmp.setPrice(tmp.getPrice()+list.get(j).getItem().getPrice());
							tmp.setMenuItemCounter(counter);
							tmp.addOrderItemIds(list.get(j).getId());
							j++;
						}
						i = j-1;
						output.add(tmp);
					}
					// Spring is great, you can write all what you want in Json automaticly
					return output;
				}
				// You could return something else, look over the rainbow (List<?>)
				return null;
			} else {
				throw new Exception();
			}


		} catch (Exception e) {
			LOGGER.warning("Error searching order by deskNumber " + 1 + ": " + e.getMessage());
			//response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
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
				updateSocket();
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
				updateSocket();
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

	/**
	 * method to send open orders to socket
	 */
	private void updateSocket() {
		this.simpMessagingTemplate.convertAndSend("/topic/order", this.orderRepository.findByState(OrderItemState.NEW));
	}

}
