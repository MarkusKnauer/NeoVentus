package de.neoventus.persistence.repository.advanced;

import de.neoventus.persistence.entity.Workingplan;
import de.neoventus.persistence.entity.Workingshift;
import de.neoventus.rest.dto.WorkingPlanDto;

import java.util.List;


/**
 * @author Julian Beck
 **/
public interface NVWorkingPlanRepository {

	/**
	 *
	 * @param dto
	 */
	void save(WorkingPlanDto dto);

	List<Workingplan> findAllUsersByPeriod(String date1, String date2);
	List<Workingshift> findAllUserbyPeriod(String Username, String date1, String date2);


}
