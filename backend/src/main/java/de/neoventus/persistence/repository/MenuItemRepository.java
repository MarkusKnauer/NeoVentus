package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.repository.advanced.NVMenuItemRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dennis Thanner, Julian Beck
 * @version 0.0.3 changed menuItemID to number, added findFirstByOrderByNumberDesc
 *          0.0.2 added find by menuItemID - JB
 **/
@Repository
public interface MenuItemRepository extends CrudRepository<MenuItem, String>, NVMenuItemRepository {

	/**
	 * find menuitem by name
	 *
	 * @param name name to search for
	 * @return MenuItem
	 */
	MenuItem findByName(String name);

	/**
	 * find menuItem by number
	 *
	 * @param number to search for
	 * @return MenuItem
	 */
	MenuItem findByNumber(Integer number);

	/**
	 * find menuItem with max number
	 *
	 * @return menuItem
	 */
	MenuItem findFirstByOrderByNumberDesc();

	/**
	 * @deprecated DANGER! lifecycle event to set number id might not work with this method
	 */
	@Deprecated
	@Override
	<S extends MenuItem> Iterable<S> save(Iterable<S> entities);
}
