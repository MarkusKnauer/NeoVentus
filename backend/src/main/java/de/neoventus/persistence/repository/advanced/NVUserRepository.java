package de.neoventus.persistence.repository.advanced;

import de.neoventus.rest.dto.UserDto;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
public interface NVUserRepository {

	/**
	 * convenience method to update or create user by dto
	 *
	 * @param dto
	 */
	void save(UserDto dto);

}
