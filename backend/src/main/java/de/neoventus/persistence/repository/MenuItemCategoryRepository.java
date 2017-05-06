package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.MenuItemCategory;
import de.neoventus.persistence.repository.advanced.NVMenuItemCategoryRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Julian Beck, Dennis Thanner
 * @version 0.0.2 added findByChildrenContaining - DT
 */
public interface MenuItemCategoryRepository extends CrudRepository<MenuItemCategory, String>, NVMenuItemCategoryRepository {

	/**
	 * find menuitemcategory by name
	 *
	 * @param name name to search for
	 * @return MenuItemCategory
	 */
	MenuItemCategory findByName(String name);

	/**
	 * find category by child it has
	 *
	 * @param menuItemCategory
	 * @return
	 */
	MenuItemCategory findBySubcategoryContaining(MenuItemCategory menuItemCategory);

}
