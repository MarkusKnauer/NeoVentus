package de.neoventus.init.dbOperations.insertUpdateDelete;/**
 * Created by julian on 17.04.2017.
 */

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description: Insert, Update, Delete on DB
 **/
public class IUDReservation extends IUDAbstract{
    private ReservationRepository reservationRepository;
    private Reservation reservation;

    public IUDReservation(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }
    @Override
    public void insert(AbstractDocument abs) {
        reservation = (Reservation)abs;
        setCollectionSize(getCollectionSize()+1);
        reservationRepository.save(reservation);
    }

    @Override
    public void update(AbstractDocument abs) {
        reservation = (Reservation)abs;
        reservationRepository.save(reservation);
    }
    @Override
    public void delete(AbstractDocument abs) {
        reservation = (Reservation)abs;
        setCollectionSize(getCollectionSize()-1);
        reservationRepository.delete(reservation);
    }
}
