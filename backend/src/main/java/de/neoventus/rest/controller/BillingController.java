package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.Billing;
import de.neoventus.persistence.repository.BillingRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.ObjectRevenueAggregation;
import de.neoventus.rest.auth.NVUserDetails;
import de.neoventus.rest.dto.BillingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * REST controller for entity billing
 *
 * @author Tim Heidelbach
 * @version 0.0.2
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
	@RequestMapping(value = "/single/{billingId}", method = RequestMethod.GET)
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
	 * controller method to get this years revenue by quarter
	 *
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/stats/revenue/quarter")
	public List<ObjectRevenueAggregation> getQuartersRevenue(HttpServletResponse response) {
		try {
			return billingRepository.getQuartersRevenue();
		} catch (DataAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}

	/**
	 * controller method to expose top 10 waiters
	 *
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/stats/top10/waiters")
	public List<ObjectRevenueAggregation> getTop10Waiter(HttpServletResponse response) {
		try {
			return billingRepository.getTop10Waiters();
		} catch (DataAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}

	/**
	 * Controller method to get a all billings by a specific waiter
	 *
	 * @param response the response
	 * @param waiterId the waiterId
	 * @return the billings
	 */
	@RequestMapping(value = "/waiter/{waiterId}", method = RequestMethod.GET)
	public List<Billing> listBillingsByWaiter(HttpServletResponse response, @PathVariable String waiterId) {
		try {
			return billingRepository.findByWaiter(waiterId);
		} catch (DataAccessException e) {
			LOGGER.warning("Error searching billings by waiter: " + waiterId + ": " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}

	/**
	 * find todays billings
	 *
	 * @param response
	 * @param userDetails
	 * @return
	 */
	@RequestMapping(value = "/today", method = RequestMethod.GET)
	public List<Billing> listTodaysBilling(HttpServletResponse response, @AuthenticationPrincipal NVUserDetails userDetails) {
		try {
			Calendar c = new GregorianCalendar();
			c.set(Calendar.MILLISECOND, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.HOUR, 0);
			return billingRepository.findByWaiterIdAndBilledAtGreaterThanOrderByBilledAtDesc(userDetails.getUserId(), c.getTime());
		} catch (DataAccessException e) {
			LOGGER.warning("Error searching billings by waiter: " + userDetails.getUserId() + ": " + e.getMessage());
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
