package de.neoventus.persistence.event;

import de.neoventus.persistence.entity.Reservation;
import de.neoventus.persistence.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

/**
 * class for handling entity events for reservations
 *
 * @author Tim Heidelbach
 * @version 0.0.1
 */
@Component
public class ReservationLifecycleEvents extends AbstractMongoEventListener<Reservation> {

	private ReservationRepository reservationRepository;

	/**
	 * Auto increments reservationId if none was provided
	 *
	 * @param event the event
	 */
	@Override
	public void onBeforeSave(BeforeSaveEvent<Reservation> event) {
		Reservation reservation = event.getSource();

		if (reservation.getReservationId() == null) {
			Reservation max = reservationRepository.findFirstByOrderByReservationIdDesc();
			reservation.setReservationId(max == null ? 1 : max.getReservationId() + 1);
		}
	}

	@Autowired
	public void setReservationRepository(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}
}
