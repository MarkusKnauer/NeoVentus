package de.neoventus.persistence.repository.advanced;

import de.neoventus.rest.dto.DeskDto;

/**
 * @author Julian Beck, Dominik Streif
 * @version 0.0.1
 **/
public interface NVDeskRepository {

	/**
	 * convenience method to update or method desk by dto
	 *
	 * @param dto
	 */

	void save(DeskDto dto);

}
