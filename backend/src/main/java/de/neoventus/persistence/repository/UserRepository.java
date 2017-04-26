package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.advanced.NVUserRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dennis Thanner, Julian Beck
 * @version 0.0.4 deprecated bulk save
 *          0.0.3 added findFirstByOrOrderByUserIdDesc DT
 *          0.0.2 Add findByUserId JB
 *          0.0.1 Creation of repository DT
 **/
@Repository
public interface UserRepository extends CrudRepository<User, String>, NVUserRepository {

	/**
	 * find user by username
	 *
	 * @param username username to search for
	 * @return user
	 */
	User findByUsername(String username);

	/**
	 * find user by workerId
	 *
	 * @param workerId workerId to search for
	 * @return user
	 */
	User findByWorkerId(Integer workerId);


	/**
	 * find user with max workerId
	 *
	 * @return user
	 */
	User findFirstByOrderByWorkerIdDesc();

	/**
	 * @deprecated DANGER! lifecycle event to set worker id might not work with this method
	 */
	@Deprecated
	@Override
	<S extends User> Iterable<S> save(Iterable<S> entities);
}
