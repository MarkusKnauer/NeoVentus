package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.BillingItem;
import de.neoventus.persistence.repository.BillingItemRepository;
import de.neoventus.rest.dto.BillingItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.logging.Logger;

/**
 * REST controller for entity billing item
 *
 * @author Tim Heidelbach
 * @version 0.0.1
 */

@RestController
@RequestMapping("/api/billingitem")
public class BillingItemController {

	private static final Logger LOGGER = Logger.getLogger(BillingItemController.class.getName());
	private final BillingItemRepository billingItemRepository;

	@Autowired
	public BillingItemController(BillingItemRepository billingItemRepository) {
		this.billingItemRepository = billingItemRepository;
	}


	/**
	 * Controller method to get a specific billing item
	 *
	 * @param response      the response
	 * @param billingItemId the billingItemId
	 * @return the billing item
	 */
	@RequestMapping(value = "/{billingItemId}", method = RequestMethod.GET)
	public BillingItem listBilling(HttpServletResponse response, @PathVariable String billingItemId) {
		try {
			return billingItemRepository.findOne(billingItemId);
		} catch (DataAccessException e) {
			LOGGER.warning("Error searching billing item by Id " + billingItemId + ": " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}

	/**
	 * Controller method for inserting new billing items
	 *
	 * @param dto           the billing item data transfer object
	 * @param bindingResult the result
	 * @param response      the response
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void insert(@RequestBody @Valid BillingItemDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				LOGGER.warning("Error inserting billing item to database: BindingResult has errors.");
			} else {
				billingItemRepository.save(dto);
				LOGGER.info("Saving billing item to database: " + dto.getId());
			}
		} catch (DataAccessException e) {
			LOGGER.warning("Error inserting billing item to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * Controller method for updating billing items
	 *
	 * @param dto           the billing item data transfer object
	 * @param bindingResult the result
	 * @param response      the response
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody @Valid BillingItemDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				LOGGER.warning("Error updating billing item to database: BindingResult has errors.");
			} else {
				billingItemRepository.save(dto);
				LOGGER.info("Update billing item to database: " + dto.getId());
			}
		} catch (DataAccessException e) {
			LOGGER.warning("Error updating billing item to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * Controller method for deleting billing items
	 *
	 * @param id       the id
	 * @param response the response
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public void delete(@RequestParam String id, HttpServletResponse response) {
		try {
			billingItemRepository.delete(id);
			LOGGER.info("Deleted billing item from database: " + id);
		} catch (DataAccessException e) {
			LOGGER.warning("Error deleting billing item from database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

}
