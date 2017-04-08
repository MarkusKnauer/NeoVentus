package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.Reservation;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
public interface ReservationRepository extends CrudRepository<Reservation, String> {
}
