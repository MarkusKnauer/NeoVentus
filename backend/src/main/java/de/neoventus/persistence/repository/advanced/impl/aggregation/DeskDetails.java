package de.neoventus.persistence.repository.advanced.impl.aggregation;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.Reservation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Thanner
 */
public class DeskDetails extends Desk {

	private List<OrderDeskAggregationDto> orders;

	private Reservation nextReservation;

	// constructors

	public DeskDetails(Desk desk) {
		super(desk.getId(), desk.getNumber(), desk.getSeats(), desk.getMaximalSeats());
		this.orders = new ArrayList<>();
	}


	// methods

	// getter and setter


	public List<OrderDeskAggregationDto> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDeskAggregationDto> orders) {
		this.orders = orders;
	}

	public Reservation getNextReservation() {
		return nextReservation;
	}

	public void setNextReservation(Reservation nextReservation) {
		this.nextReservation = nextReservation;
	}
}
