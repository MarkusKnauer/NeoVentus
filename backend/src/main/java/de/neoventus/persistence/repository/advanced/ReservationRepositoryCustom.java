package de.neoventus.persistence.repository.advanced;

import de.neoventus.persistence.entity.Reservation;

/**
 * Created by julian on 19.04.2017.
 */
public interface ReservationRepositoryCustom {

    //Reservation-Operations
    public void insertReservation(Reservation item);
    public void updateReservation(Reservation item);
    public void deleteReservation(Reservation item);
}
