package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.MenuItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
@Repository
public interface MenuItemRepository extends CrudRepository<MenuItem, String> {

    /**
     * find menuitem by name
     *
     * @param name name to search for
     * @return MenuItem
     */
    MenuItem findByName(String name);
}
