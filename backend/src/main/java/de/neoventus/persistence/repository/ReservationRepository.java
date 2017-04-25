package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.Reservation;
import de.neoventus.persistence.repository.advanced.NVReservationRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dennis Thanner, Tim Heidelbach
 * @version 0.0.3 redundancy clean up - DT
 **/
@Repository
public interface ReservationRepository extends CrudRepository<Reservation, String>, NVReservationRepository {


}
