package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @author Dennis Thanner, Tim Heidelbach
 * @version 0.0.3 added constructors
 *          0.0.2 removed local variable StringBuilder
 **/
public class Desk extends AbstractDocument {

	@Indexed(unique = true)
	private Integer number;

	private Integer seats;

	// constructor
	public Desk() {
	}

	public Desk(Integer seats) {
		this.seats = seats;
	}

	// getter and setter

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}

	@Override
	public String toString() {
		return "Desk{" +
				"Id: " +
				this.id +
				", Number: " +
				this.number +
				", Seats: " +
				this.seats +
				"}";
	}
}
