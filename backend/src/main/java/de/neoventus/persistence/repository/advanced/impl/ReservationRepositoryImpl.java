package de.neoventus.persistence.repository.advanced.impl;/**
 * Created by julian on 19.04.2017.
 */

import de.neoventus.persistence.entity.Reservation;
import de.neoventus.persistence.repository.ReservationRepository;
import de.neoventus.persistence.repository.advanced.ReservationRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {
    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public void insertReservation(Reservation item) {
        item.setReservationID((int)reservationRepository.count()+1);
        reservationRepository.save(item);
    }

    @Override
    public void updateReservation(Reservation item) {
        reservationRepository.save(item);
    }

    @Override
    public void deleteReservation(Reservation item) {
        reservationRepository.delete(item);
    }
}
