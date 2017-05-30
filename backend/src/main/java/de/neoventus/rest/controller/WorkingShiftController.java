package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.Workingplan;
import de.neoventus.persistence.entity.Workingshift;
import de.neoventus.persistence.repository.WorkingPlanRepository;
import de.neoventus.rest.dto.WorkingPlanDto;
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
 * REST controller for entity Workingshift
 *
 * @author Julian Beck
**/

@RestController
@RequestMapping("/api/shift")
public class WorkingShiftController {

	private final static Logger LOGGER = Logger.getLogger(WorkingShiftController.class.getName());
	private final WorkingPlanRepository workingplanRepository;

	@Autowired
	public WorkingShiftController(WorkingPlanRepository workingplanRepository) {
		this.workingplanRepository = workingplanRepository;
	}


// ---------------------------------------------------------------------------------------------------------------------
// -------------------------------------------- find All - Methods -----------------------------------------------------
	/**
	 * controller method to list all Shifts
	 *
	 * @param response the response
	 * @return all shifts
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Workingplan> getAllShifts(HttpServletResponse response) {
		try {
			return workingplanRepository.findAll();
		} catch (DataAccessException e) {
			LOGGER.warning("Error searching for all workingplans: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}


	/**
	 * controller method to list workingplan details
	 *
	 * @param response
	 * @param date1, date2
	 * @return
	 */
	@RequestMapping(value = "/all/{date1}&{date2}", method = RequestMethod.GET)
	public List<Workingplan> periodShifts(HttpServletResponse response, @PathVariable String date1,  @PathVariable String date2) {
		try {
			return workingplanRepository.findAllUsersByPeriod(date1, date2);
		} catch (Exception e) {
			LOGGER.warning("Error searching workingplan with periodShifts"  + ": " + date1+ ": " + date2);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}


	/**
	 * controller method to list workingplan details
	 *
	 * @param response
	 * @param UserName
	 * @return
	 */
	@RequestMapping(value = "/personal/{UserName}&{date1}&{date2}", method = RequestMethod.GET)
	public List<Workingshift> userPeriodShifts(HttpServletResponse response, @PathVariable String UserName, @PathVariable String date1, @PathVariable String date2) {
		try {
			return workingplanRepository.findAllUserbyPeriod(UserName, date1, date2);
		} catch (Exception e) {
			LOGGER.warning("Error searching workingplan with username " + UserName + ": " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}
























	/**
	 * controller method for inserting workingplan
	 *
	 * @param dto
	 * @param bindingResult
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void insert(@RequestBody @Valid WorkingPlanDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				workingplanRepository.save(dto);
				LOGGER.info("Saving Workingplan to database: " + dto.getId());
			}

		} catch (Exception e) {
			LOGGER.warning("Error inserting Workingplan to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * controller method for updating workingplan
	 *
	 * @param dto
	 * @param bindingResult
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody @Valid WorkingPlanDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				workingplanRepository.save(dto);
				LOGGER.info("Update workingplan to database: " + dto.getId());
			}
		} catch (Exception e) {
			LOGGER.warning("Error updating workingplan to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * controller method for deleting workingplan
	 *
	 * @param id
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public void delete(@RequestParam String id, HttpServletResponse response) {
		try {
			workingplanRepository.delete(id);
		} catch (Exception e) {
			LOGGER.warning("Error deleting workingplan to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

}
