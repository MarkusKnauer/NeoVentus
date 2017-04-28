package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.MenuItemCategory;
import de.neoventus.persistence.repository.advanced.NVMenuItemCategoryRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by julian on 27.04.2017.
 */
public interface MenuItemCategoryRepository extends CrudRepository<MenuItemCategory, String>, NVMenuItemCategoryRepository {

	/**
	 * find menuitemcategory by name
	 *
	 * @param name name to search for
	 * @return MenuItemCategory
	 */
	MenuItemCategory findByName(String name);

}
