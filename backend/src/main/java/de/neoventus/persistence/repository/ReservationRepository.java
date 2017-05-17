package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.Reservation;
import de.neoventus.persistence.repository.advanced.NVReservationRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Dennis Thanner, Tim Heidelbach
 * @version 0.0.4 find reservations by desk
 *          0.0.3 redundancy clean up - DT
 **/
@Repository
public interface ReservationRepository extends CrudRepository<Reservation, String>, NVReservationRepository {

	/**
	 * find reservations by desk
	 *
	 * @param desk the desk id
	 * @return list of reservations
	 */
	List<Reservation> findByDesk(String desk);

}
