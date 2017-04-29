package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.rest.dto.DeskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.logging.Logger;

/**
 * REST controller for entity Desk
 *
 * @author Dominik Streif, Markus Knauer, Tim Heidelbach
 * @version 0.0.3 added getAllDesks
 *          0.0.2 edited Logger to DeskController MK
 *          0.0.1 DS
 */


@RestController
@RequestMapping("/api/desk")
public class DeskController {

	private final static Logger LOGGER = Logger.getLogger(DeskController.class.getName());
	private final DeskRepository deskRepository;

	@Autowired
	public DeskController(DeskRepository deskRepository) {
		this.deskRepository = deskRepository;
	}

	/**
	 * controller method to list all desks
	 *
	 * @param response the response
	 * @return all desks
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Desk> getAllDesks(HttpServletResponse response) {
		try {
			return deskRepository.findAll();
		} catch (DataAccessException e) {
			LOGGER.warning("Error searching for all desks: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}

	/**
	 * controller method to list desk details
	 *
	 * @param response
	 * @param number
	 * @return
	 */
	@RequestMapping(value = "/{number}", method = RequestMethod.GET)
	public Desk listDesk(HttpServletResponse response, @PathVariable Integer number) {
		try {
			return deskRepository.findByNumber(number);
		} catch (Exception e) {
			LOGGER.warning("Error searching desk with number " + number + ": " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}

	/**
	 * controller method for inserting desk
	 *
	 * @param dto
	 * @param bindingResult
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void insert(@RequestBody @Valid DeskDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				deskRepository.save(dto);
				LOGGER.info("Saving desk to database: " + dto.getId());
			}

		} catch (Exception e) {
			LOGGER.warning("Error inserting desk to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * controller method for updating desk
	 *
	 * @param dto
	 * @param bindingResult
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody @Valid DeskDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				deskRepository.save(dto);
				LOGGER.info("Update desk to database: " + dto.getId());
			}
		} catch (Exception e) {
			LOGGER.warning("Error updating desk to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * controller method for deleting desk
	 *
	 * @param id
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public void delete(@RequestParam String id, HttpServletResponse response) {
		try {
			deskRepository.delete(id);
		} catch (Exception e) {
			LOGGER.warning("Error deleting desk to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

}
