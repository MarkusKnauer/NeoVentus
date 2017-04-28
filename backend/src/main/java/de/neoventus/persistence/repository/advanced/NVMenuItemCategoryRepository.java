package de.neoventus.persistence.repository.advanced;

import de.neoventus.rest.dto.MenuItemCategoryDto;

/**
 * Created by julian on 28.04.2017.
 */
public interface NVMenuItemCategoryRepository {
	/**
	 * convenience method to update or method MenuItemCategory by dto
	 *
	 * @param dto
	 */

	void save(MenuItemCategoryDto dto);
}
