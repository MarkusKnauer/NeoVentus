package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.SideDishGroup;
import de.neoventus.persistence.repository.advanced.NVSideDishRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by julian on 27.04.2017.
 */
public interface SideDishRepository extends CrudRepository<SideDishGroup, String>, NVSideDishRepository {

	/**
	 * find sidDish by name
	 *
	 * @param name name to search for
	 * @return SideDishGroup
	 */
	SideDishGroup findByName(String name);

}
