package de.neoventus.persistence.repository.advanced;

import de.neoventus.rest.dto.ReservationDto;

/**
 * @author Tim Heidelbach
 * @version 0.0.1
 */
public interface NVReservationRepository {

	/**
	 * Convenient method to create or update reservation by dto
	 *
	 * @param dto The reservation data transfer object
	 */
	void save(ReservationDto dto);

}
