package de.neoventus.persistence.repository.advanced.impl.aggregation;

import de.neoventus.persistence.entity.Reservation;
import de.neoventus.persistence.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * mapping class for aggregation
 *
 * @author Tim Heidelbach
 */
public class DeskOverviewDetails {

	private int deskNumber;
	//	private Desk desk;
	private List<User> waiters;
	private double totalPaid;
	private Reservation nextReservation;

	public DeskOverviewDetails() {
		waiters = new ArrayList<>();
	}

	public int getDeskNumber() {
		return deskNumber;
	}

	public void setDeskNumber(int deskNumber) {
		this.deskNumber = deskNumber;
	}

	public List<User> getWaiters() {
		return waiters;
	}

	public void setWaiters(List<User> waiters) {
		this.waiters = waiters;
	}

	public double getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(double totalPaid) {
		this.totalPaid = totalPaid;
	}

	public Reservation getNextReservation() {
		return nextReservation;
	}

	public void setNextReservation(Reservation nextReservation) {
		this.nextReservation = nextReservation;
	}

//	public Desk getDesk() {
//		return desk;
//	}
//
//	public void setDesk(Desk desk) {
//		this.desk = desk;
//	}
}
