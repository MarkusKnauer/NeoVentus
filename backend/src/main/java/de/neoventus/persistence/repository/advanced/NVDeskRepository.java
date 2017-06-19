package de.neoventus.persistence.repository.advanced;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.repository.advanced.impl.aggregation.DeskOverviewDetails;
import de.neoventus.rest.dto.DeskDto;

import java.util.Date;
import java.util.List;

/**
 * @author Julian Beck, Dominik Streif, Tim Heidelbach
 **/
public interface NVDeskRepository {

	/**
	 * convenience method to update or method desk by dto
	 *
	 * @param dto
	 */
	void save(DeskDto dto);

	/**
	 * list aggregated desks with details for desk overview
	 *
	 * @return all desks with details
	 */
	List<DeskOverviewDetails> getDesksWithDetails();

	/**
	 * list all desks that are not reserved
	 *
	 * @param date
	 * @return
	 */
	List<Desk> getNotReservedDesks(Date date);

}
