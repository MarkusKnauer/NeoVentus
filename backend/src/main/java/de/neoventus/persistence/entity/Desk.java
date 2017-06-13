package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @author Dennis Thanner, Tim Heidelbach, Julian Beck
 * @version 0.0.4 added new Constructor, new variable maximalSeats -  JB
 * 			0.0.3 added constructors
 *          0.0.2 removed local variable StringBuilder
 **/
public class Desk extends AbstractDocument {

	@Indexed(unique = true)
	private Integer number;

	private Integer seats;

	private Integer maximalSeats;

	// constructor
	public Desk() {
	}

	public Desk(Integer seats) {
		this.seats = seats;
	}
	public Desk(Integer number, Integer seats, Integer maximalSeats) {
		this.number = number;
		this.seats = seats;
		this.maximalSeats = maximalSeats;
	}

	public Desk(String id, Integer number, Integer seats, Integer maximalSeats) {
		super(id);
		this.number = number;
		this.seats = seats;
		this.maximalSeats = maximalSeats;
	}

	// getter and setter


	public Integer getMaximalSeats() {
		return maximalSeats;
	}

	public void setMaximalSeats(Integer maximalSeats) {
		this.maximalSeats = maximalSeats;
	}

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
