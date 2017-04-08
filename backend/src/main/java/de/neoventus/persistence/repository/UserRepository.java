package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
public interface UserRepository extends CrudRepository<User, String> {
	
	/**
	 * find user by username
	 *
	 * @param username username to search for
	 * @return user
	 */
	User findByUsername(String username);
	
}
