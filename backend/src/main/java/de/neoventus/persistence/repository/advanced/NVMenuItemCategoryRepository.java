package de.neoventus.persistence.repository.advanced;

import de.neoventus.rest.dto.MenuItemCategoryDto;

/**
 * Created by julian on 28.04.2017.
 */
public interface NVMenuItemCategoryRepository {

	void save(MenuItemCategoryDto dto);
}
