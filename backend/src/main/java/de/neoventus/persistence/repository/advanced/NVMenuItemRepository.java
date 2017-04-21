package de.neoventus.persistence.repository.advanced;

import de.neoventus.rest.dto.MenuDto;


/**
 * @author Julian Beck, Markus Knauer
 * @version 0.0.2 insert save method MK
 * 0.0.1 created JB
 */
public interface NVMenuItemRepository {

	void setDefaultMenu();

	void save(MenuDto dto);

}
