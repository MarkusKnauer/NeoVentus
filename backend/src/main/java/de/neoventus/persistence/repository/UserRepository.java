package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dennis Thanner
 * @version 0.0.2 Add findbyUserID
 * 			0.0.1 Creation of respository DT
 **/
@Repository
public interface UserRepository extends CrudRepository<User, String> {

	/**
	 * find user by username
	 *
	 * @param username username to search for
	 * @return user
	 */
	User findByUsername(String username);

	/**
	 * find user by userID
	 *
	 * @param userID userIDto search for
	 * @return user
	 */
	User findByUserID(Integer userID);

}
