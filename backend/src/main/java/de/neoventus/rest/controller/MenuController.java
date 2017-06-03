package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.GuestWishCount;
import de.neoventus.persistence.repository.advanced.impl.aggregation.MenuItemProcessingDetails;
import de.neoventus.rest.dto.MenuDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

/**
 * REST controller for entity Menu
 *
 * @author Markus Knauer, Dennis Thanner
 * @version 0.0.3 added list all menu items method
 *          0.0.2 redundancy clean up - DT
 */

@RestController
@RequestMapping("/api/menu")
public class MenuController {

	private static final Logger LOGGER = Logger.getLogger(MenuController.class.getName());

	private final MenuItemRepository menuItemRepository;

	@Autowired
	public MenuController(MenuItemRepository menuItemRepository) {
		this.menuItemRepository = menuItemRepository;
	}


	/**
	 * controller method to list menu items
	 *
	 * @param response
	 * @return
	 */
	@GetMapping("/all")
	public Iterable<MenuItem> listAll(HttpServletResponse response) {
		try {
			return this.menuItemRepository.findAll();
		} catch (Exception e) {
			LOGGER.warning("Error listing menu" + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}

	/**
	 * controller method to list menuItem details
	 *
	 * @param response
	 * @param menuItem
	 * @return
	 */
	@GetMapping("/single/{menuItem}")
	public MenuItem listMenuItem(HttpServletResponse response, @PathVariable String menuItem) {
		try {
			return menuItemRepository.findByName(menuItem);
		} catch (Exception e) {
			LOGGER.warning("Error searching menuItem with name " + menuItem + ": " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}

	/**
	 * controller method to list popular guest wishes per menu
	 *
	 * @param response
	 * @param menuId
	 * @return
	 */
	@GetMapping("/popular-wishes/{menuId}")
	public List<GuestWishCount> listPopularGuestWishes(HttpServletResponse response, @PathVariable String menuId) {
		try {
			return menuItemRepository.getPopularGuestWishesForItem(menuId);
		} catch (Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}

	/**
	 * get processing details for menu item
	 *
	 * @param response
	 * @param menuId
	 * @return
	 */
	@RequestMapping(value = "/processing-details/{menuId}", method = RequestMethod.GET)
	public MenuItemProcessingDetails getAvgProcessing(HttpServletResponse response, @PathVariable String menuId) {
		MenuItemProcessingDetails result = menuItemRepository.getProcessingDetails(menuId);
		if (result == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
		}
		return result;
	}

	/**
	 * controller method for inserting menuItem
	 *
	 * @param dto
	 * @param bindingResult
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void insert(@RequestBody @Valid MenuDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				menuItemRepository.save(dto);
				LOGGER.info("Saving MenuItem to database: " + dto.getId());
			}
		} catch (Exception e) {
			LOGGER.warning("Error inserting menuItem to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

	}

	/**
	 * controller method for updating menuItem
	 *
	 * @param dto
	 * @param bindingResult
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody @Valid MenuDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				menuItemRepository.save(dto);
				LOGGER.info("Update menuItem to database: " + dto.getId());
			}
		} catch (Exception e) {
			LOGGER.warning("Error updating menuItem to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * controller method for deleting menuItem
	 *
	 * @param id
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public void delete(@RequestParam String id, HttpServletResponse response) {
		try {
			menuItemRepository.delete(id);
		} catch (Exception e) {
			LOGGER.warning("Error deleting menuItem to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

}

