package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.SideDish;
import de.neoventus.persistence.repository.SideDishRepository;
import de.neoventus.rest.dto.SideDishDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.logging.Logger;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/

@RestController
@RequestMapping("/api/sideDish")
public class SideDishController {

	private static final Logger LOGGER = Logger.getLogger(SideDishController.class.getName());

	private final SideDishRepository sideDishRepository;

	@Autowired
	public SideDishController(SideDishRepository sideDishRepository){ this.sideDishRepository = sideDishRepository;}

	/**
	 * controller method to list sideDish details
	 *
	 * @param response
	 * @param sideDish
	 * @return
	 */
	@RequestMapping(value = "/{sideDish}", method = RequestMethod.GET)
	public SideDish listMenuItem(HttpServletResponse response, @PathVariable String sideDish) {
		try {
			return sideDishRepository.findBySideDishName(sideDish);
		} catch(Exception e) {
			LOGGER.warning("Error searching sideDish with name " + sideDish + ": " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}


	/**
	 * controller method for inserting sideDish
	 *
	 * @param dto
	 * @param bindingResult
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void insert(@RequestBody @Valid SideDishDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				sideDishRepository.save(dto);
				LOGGER.info("Saving sideDish to database: " + dto.getId());
			}
		} catch(Exception e) {
			LOGGER.warning("Error inserting sideDish to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

	}

	/**
	 * controller method for updating sideDish
	 *
	 * @param dto
	 * @param bindingResult
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody @Valid SideDishDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				sideDishRepository.save(dto);
				LOGGER.info("Update sideDish to database: " + dto.getId());
			}
		} catch (Exception e) {
			LOGGER.warning("Error updating sideDish to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * controller method for deleting sideDish
	 *
	 * @param id
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public void delete(@RequestParam String id, HttpServletResponse response) {
		try {
			sideDishRepository.delete(id);
		} catch (Exception e) {
			LOGGER.warning("Error deleting sideDish to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}
}
