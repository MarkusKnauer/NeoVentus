package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.Desk;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
public interface DeskRepository extends CrudRepository<Desk, Integer> {
    /**
     * find desk by number
     *
     * @param number number to search for
     * @return Desk
     */
    Desk findByNumber(Integer number);
}
