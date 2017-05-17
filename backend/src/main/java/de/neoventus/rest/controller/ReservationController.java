package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.Reservation;
import de.neoventus.persistence.repository.ReservationRepository;
import de.neoventus.rest.dto.ReservationDto;
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
 * REST controller for entity reservation
 *
 * @author Tim Heidelbach, Dennis Thanner
 * @version 0.0.2 redundancy clean up - DT
 */

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

	private static final Logger LOGGER = Logger.getLogger(ReservationController.class.getName());
	private final ReservationRepository reservationRepository;

	@Autowired
	public ReservationController(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	/**
	 * controller method to list reservation details by reservationId
	 *
	 * @param response      the response
	 * @param reservationId the reservation Id
	 * @return the reservation entity
	 */
	@RequestMapping(value = "/{reservationId}", method = RequestMethod.GET)
	public Reservation getReservationById(HttpServletResponse response, @PathVariable String reservationId) {
		try {
			return reservationRepository.findOne(reservationId);
		} catch (DataAccessException e) {
			LOGGER.warning("Error searching reservation with id " + reservationId + ": " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}

	/**
	 * controller method to list reservations desk id
	 *
	 * @param response the response
	 * @param desk     the desk id
	 * @return the reservation entities
	 */
	@RequestMapping(value = "desk/{desk}", method = RequestMethod.GET)
	public List<Reservation> getReservationsByDesk(HttpServletResponse response, @PathVariable String desk) {
		try {
			return reservationRepository.findByDesk(desk);
		} catch (DataAccessException e) {
			LOGGER.warning("Error searching reservations for desk " + desk + ": " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}

	/**
	 * controller method for inserting reservation
	 *
	 * @param dto           the reservation dto
	 * @param bindingResult the binding result
	 * @param response      the response
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void insert(@RequestBody @Valid ReservationDto dto, BindingResult bindingResult,
	                   HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				reservationRepository.save(dto);
				LOGGER.info("Saving reservation to database: " + dto.getId());
			}
		} catch (DataAccessException e) {
			LOGGER.warning("Error inserting reservation to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * controller method for updating reservation
	 *
	 * @param dto           the reservation dto
	 * @param bindingResult the binding result
	 * @param response      the response
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody @Valid ReservationDto dto, BindingResult bindingResult,
	                   HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				reservationRepository.save(dto);
				LOGGER.info("Updating reservation to database: " + dto.getId());
			}
		} catch (DataAccessException e) {
			LOGGER.warning("Error updating reservation to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * controller method for deleting reservation
	 *
	 * @param id       the id
	 * @param response the response
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public void delete(@RequestParam String id, HttpServletResponse response) {
		try {
			reservationRepository.delete(id);
		} catch (DataAccessException e) {
			LOGGER.warning("Error deleting reservation from database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}
}
