package de.neoventus.persistence.repository.advanced;

import de.neoventus.persistence.repository.advanced.impl.aggregation.UserProfileDetails;
import de.neoventus.rest.dto.UserDto;

/**
 * @author Dennis Thanner
 **/
public interface NVUserRepository {

	/**
	 * convenience method to update or create user by dto
	 *
	 * @param dto
	 */
	void save(UserDto dto);

	/**
	 * load user profile details from database
	 *
	 * @param id
	 * @return
	 */
	UserProfileDetails getUserProfileDetails(String id);

}
