package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.MenuItemCategory;
import de.neoventus.persistence.repository.advanced.NVMenuItemCategoryRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Julian Beck, Dennis Thanner
 * @version 0.0.4 added findByForKitchen - DT
 *          0.0.3 added getRootElements - DT
 *          0.0.2 added findByChildrenContaining - DT
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

	/**
	 * find all root categories to display tree
	 *
	 * @return
	 */
	@Query("{parent: null}")
	List<MenuItemCategory> getRootElements();

	/**
	 * find by boolean
	 *
	 * @param forKitchen
	 * @return
	 */
	List<MenuItemCategory> findByForKitchen(boolean forKitchen);

}
