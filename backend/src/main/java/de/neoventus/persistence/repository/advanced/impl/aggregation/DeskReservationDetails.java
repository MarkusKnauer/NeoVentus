package de.neoventus.persistence.repository.advanced.impl.aggregation;

import java.util.Date;

/**
 * @author Dennis Thanner
 */
public class DeskReservationDetails {

	private String id;
	private Integer number;
	private Integer maximalSeats;
	private Date nextReservation;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getMaximalSeats() {
		return maximalSeats;
	}

	public void setMaximalSeats(Integer maximalSeats) {
		this.maximalSeats = maximalSeats;
	}

	public Date getNextReservation() {
		return nextReservation;
	}

	public void setNextReservation(Date nextReservation) {
		this.nextReservation = nextReservation;
	}
}
