package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.repository.advanced.NVDeskRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
@Repository
public interface DeskRepository extends CrudRepository<Desk, String>, NVDeskRepository {
	/**
     * find desk by number
     *
     * @param number number to search for
     * @return Desk
     */
    Desk findByNumber(Integer number);

	/**
	 * find with max desk number
	 *
	 * @return desk with max desk number
	 */
	Desk findFirstByOrderByNumberDesc();

}
