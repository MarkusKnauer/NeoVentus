package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.MenuItemCategory;
import de.neoventus.persistence.repository.MenuItemCategoryRepository;
import de.neoventus.rest.dto.MenuItemCategoryDto;
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
@RequestMapping("/api/menuItemCategory")
public class MenuItemCategoryController {

	private static final Logger LOGGER = Logger.getLogger(MenuItemCategoryController.class.getName());

	private final MenuItemCategoryRepository menuItemCategoryRepository;

	@Autowired
	public MenuItemCategoryController (MenuItemCategoryRepository menuItemCategoryRepository){ this.menuItemCategoryRepository = menuItemCategoryRepository;}

	/**
	 * controller method to list menuItemCategory details
	 *
	 * @param response
	 * @param menuItemCategory
	 * @return
	 */
	@RequestMapping(value = "/{menuItemCategory}", method = RequestMethod.GET)
	public MenuItemCategory listMenuItem(HttpServletResponse response, @PathVariable String menuItemCategory) {
		try {
			return menuItemCategoryRepository.findByName(menuItemCategory);
		} catch(Exception e) {
			LOGGER.warning("Error searching menuItemCategory with name " + menuItemCategory + ": " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return null;
		}
	}


	/**
	 * controller method for inserting menuItemCategory
	 *
	 * @param dto
	 * @param bindingResult
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void insert(@RequestBody @Valid MenuItemCategoryDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				menuItemCategoryRepository.save(dto);
				LOGGER.info("Saving MenuItemCategory to database: " + dto.getId());
			}
		} catch(Exception e) {
			LOGGER.warning("Error inserting menuItemCategory to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

	}

	/**
	 * controller method for updating menuItemCategory
	 *
	 * @param dto
	 * @param bindingResult
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody @Valid MenuItemCategoryDto dto, BindingResult bindingResult, HttpServletResponse response) {
		try {
			if (bindingResult.hasErrors()) {
				response.setStatus(HttpStatus.BAD_REQUEST.value());
			} else {
				menuItemCategoryRepository.save(dto);
				LOGGER.info("Update menuItemCategory to database: " + dto.getId());
			}
		} catch (Exception e) {
			LOGGER.warning("Error updating menuItemCategory to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	/**
	 * controller method for deleting menuItemCategory
	 *
	 * @param id
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public void delete(@RequestParam String id, HttpServletResponse response) {
		try {
			menuItemCategoryRepository.delete(id);
		} catch (Exception e) {
			LOGGER.warning("Error deleting menuItemCategory to database: " + e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}
}
