package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

/**
 * states for OrderItems
 *
 * @author dominik
 */
public class OrderItemState {

	private State state;

	private Date date;

	private String reason;

	@DBRef
	private User waiter;

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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public User getWaiter() {
		return waiter;
	}

	public void setWaiter(User waiter) {
		this.waiter = waiter;
	}

	public enum State {
		NEW,
		CANCELED,
		FINISHED

	}
}
