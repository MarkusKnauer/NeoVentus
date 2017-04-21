package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.Reservation;
import de.neoventus.persistence.repository.advanced.NVReservationRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dennis Thanner, Tim Heidelbach
 * @version 0.0.2
 **/
@Repository
public interface ReservationRepository extends CrudRepository<Reservation, String>, NVReservationRepository {

    /**
     * find reservation by ID
     *
     * @param reservationId reservationID to search for
     * @return Reservation
     */
    Reservation findByReservationId(Integer reservationId);

    /**
     * find reservation with max reservationId
     *
     * @return Reservation
     */
    Reservation findFirstByOrderByReservationIdDesc();

}
