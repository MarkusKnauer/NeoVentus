package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.Billing;
import de.neoventus.persistence.repository.BillingRepository;
import de.neoventus.rest.dto.BillingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.logging.Logger;

/**
 * REST controller for entity billing
 *
 * @author Tim Heidelbach
 * @version 0.0.1
 */
@RestController
@RequestMapping("/api/billing")
public class BillingController {

	private static final Logger LOGGER = Logger.getLogger(BillingController.class.getName());
	private final BillingRepository billingRepository;

	@Autowired
	public BillingController(BillingRepository billingRepository) {
		this.billingRepository = billingRepository;
	}


	/**
	 * Controller method to get a specific billing
	 *
	 * @param response  the response
	 * @param billingId the billingId
	 * @return the billing
	 */
	@RequestMapping(value = "/{billingId}", method = RequestMethod.GET)
	public Billing listBilling(HttpServletResponse response, @PathVariable String billingId) {
		try {
			return billingRepository.findOne(billingId);
		} catch (DataAccessException e) {
			LOGGER.warning("Error searching billing by Id " + billingId + ": " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}

	/**
	 * Controller method for inserting new billings
	 *
	 * @param dto           the billing data transfer object
	 * @param bindingResult the result
	 * @param response      the response
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void insert(@RequestBody @Valid BillingDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				LOGGER.warning("Error inserting billing to database: BindingResult has errors.");
			} else {
				billingRepository.save(dto);
				LOGGER.info("Saving billing to database: " + dto.getId());
			}
		} catch (DataAccessException e) {
			LOGGER.warning("Error inserting billing to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * Controller method for updating billings
	 *
	 * @param dto           the billing data transfer object
	 * @param bindingResult the result
	 * @param response      the response
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody @Valid BillingDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				LOGGER.warning("Error updating billing to database: BindingResult has errors.");
			} else {
				billingRepository.save(dto);
				LOGGER.info("Update billing to database: " + dto.getId());
			}
		} catch (DataAccessException e) {
			LOGGER.warning("Error updating billing to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * Controller method for deleting billings
	 *
	 * @param id       the id
	 * @param response the response
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public void delete(@RequestParam String id, HttpServletResponse response) {
		try {
			billingRepository.delete(id);
			LOGGER.info("Deleted billing from database: " + id);
		} catch (DataAccessException e) {
			LOGGER.warning("Error deleting billing from database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

}
