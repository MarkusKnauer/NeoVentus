package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.UserRepository;
import de.neoventus.rest.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.logging.Logger;

/**
 * REST controller for entity User
 *
 * @author Tim Heidelbach
 * @version 0.0.2 added user dto support
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserRepository userRepository;

	private final static Logger LOGGER = Logger.getLogger(UserController.class.getName());

	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	/**
	 * controller method to list user details
	 *
	 * @param response
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public User listUser(HttpServletResponse response, @PathVariable String username) {
		try {
			return userRepository.findByUsername(username);
		} catch(Exception e) {
			LOGGER.warning("Error searching user with username " + username + ": " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
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
		// todo add on validation failure
		try {
			userRepository.save(dto);
			LOGGER.info("Saving user to database: " + dto.getUsername());
		} catch(Exception e) {
			LOGGER.warning("Error inserting user to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

	}
}
