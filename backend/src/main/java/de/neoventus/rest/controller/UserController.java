package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

/**
 * REST controller for entity User
 *
 * @author Tim
 * @version 0.0.1
 */

@RestController
public class UserController {

	private final UserRepository userRepository;

	private final static Logger LOGGER = Logger.getLogger(UserController.class.getName());

	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@RequestMapping(value = "/adduser", method = RequestMethod.POST)
	public void addUser(@RequestParam List<String> requestParams) {

		if (requestParams.size() >= 2) {
			String name = requestParams.get(0);
			String pass = requestParams.get(1);

			User user = new User();
			user.setUsername(name);
			user.setPassword(pass); // TODO: save hash instead

			LOGGER.info("Saving user " + name + " in database");
			userRepository.save(user);

		} else {
			LOGGER.warning("Cannot save user to database: illegal number of params");
		}

	}

	@RequestMapping(value = "/getuser", method = RequestMethod.GET)
	public User getUser(@RequestParam(value = "name") String name) {
		return userRepository.findByUsername(name);
	}
}
