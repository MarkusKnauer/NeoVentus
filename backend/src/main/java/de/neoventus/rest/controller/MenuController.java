package de.neoventus.rest.controller;

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.rest.dto.MenuDto;
import de.neoventus.rest.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.logging.Logger;

/**
 * REST controller for entity Menu
 *
 * @author Markus Knauer
 * @version 0.0.1
 */

@RestController
@RequestMapping("/api/menuItem")
public class MenuController {

    private static final Logger LOGGER = Logger.getLogger(MenuController.class.getName());

    private final MenuItemRepository menuItemRepository;

    @Autowired
    public MenuController (MenuItemRepository menuItemRepository){ this.menuItemRepository = menuItemRepository;}

    /**
     * controller method to list menuItem details
     *
     * @param response
     * @param menuItem
     * @return
     */
    @RequestMapping(value = "/{menuItem}", method = RequestMethod.GET)
    public MenuItem listMenuItem(HttpServletResponse response, @PathVariable String menuItem) {
        try {
            return menuItemRepository.findByName(menuItem);
        } catch(Exception e) {
            LOGGER.warning("Error searching menuItem with name " + menuItem + ": " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return null;
        }
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
                LOGGER.info("Saving MenuItem to database: " + dto.getMenuItemID());
            }
        } catch(Exception e) {
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
                LOGGER.info("Update menuItem to database: " + dto.getMenuItemID());
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

