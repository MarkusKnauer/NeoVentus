package de.neoventus.rest.controller;

import de.neoventus.persistence.repository.UserRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.UserProfileDetails;
import de.neoventus.rest.auth.NVUserDetails;
import de.neoventus.rest.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.logging.Logger;

/**
 * REST controller for entity User
 *
 * @author Tim Heidelbach, Dennis Thanner
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

	private final static Logger LOGGER = Logger.getLogger(UserController.class.getName());
	private final UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	/**
	 * controller method to expose user principal for frontend role based access management
	 *
	 * @param response
	 * @param principal
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Principal getUserPrincipal(HttpServletResponse response, Principal principal) {
		return principal;
	}


	/**
	 * user profile details with daily revenue and level
	 *
	 * @param userDetails
	 * @return
	 */
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public UserProfileDetails getProfileDetails(@AuthenticationPrincipal NVUserDetails userDetails) {
		return this.userRepository.getUserProfileDetails(userDetails.getUserId());
	}

	@RequestMapping(value = "/tips", method = RequestMethod.GET)
	public Map<Long, Double> getUserTips(@AuthenticationPrincipal NVUserDetails userDetails) {
		return this.userRepository.getLastWeekTips(userDetails.getUserId());
	}

	/**
	 * controller method for inserting user
	 *
	 * @param dto
	 * @param bindingResult
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void insert(@RequestBody @Valid UserDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				userRepository.save(dto);
				LOGGER.info("Saving user to database: " + dto.getUsername());
			}
		} catch (Exception e) {
			LOGGER.warning("Error inserting user to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

	}

	/**
	 * controller method for updating user
	 *
	 * @param dto
	 * @param bindingResult
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody @Valid UserDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				userRepository.save(dto);
				LOGGER.info("Update user to database: " + dto.getUsername());
			}
		} catch (Exception e) {
			LOGGER.warning("Error updating user to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

	}


	/**
	 * controller method for deleting user
	 *
	 * @param id
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public void delete(@RequestParam String id, HttpServletResponse response) {
		try {
			userRepository.delete(id);
		} catch (Exception e) {
			LOGGER.warning("Error updating user to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

}
