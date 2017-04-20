package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.Reservation;
import de.neoventus.persistence.repository.advanced.NVReservationRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
@Repository
public interface ReservationRepository extends CrudRepository<Reservation, String>, NVReservationRepository {
    /**
     * find reservation by ID
     *
     * @param reservationID menuItemID to search for
     * @return Reservation
     */
    Reservation findByReservationID(Integer reservationID);
}
