package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.MenuItem;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
public interface MenuItemRepository extends CrudRepository<MenuItem, String> {

    /**
     * find menuitem by name
     *
     * @param name name to search for
     * @return MenuItem
     */
    MenuItem findByName(String name);
}
