package de.neoventus.persistence.entity;

import java.util.Date;

/**
 * states for OrderItems
 *
 * @author dominik
 * @version 0.0.1
 */
public class OrderItemState {

	private State state;

	private Date date;

	// constructor

	public OrderItemState(State state) {
		this.state = state;
		this.date = new Date();
	}


	// getter and setter

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public enum State {
		NEW,
		CANCELED,
		FINISHED

	}
}
