package de.neoventus.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Thanner, Tim Heidelbach, Dominik Streif
 * @version 0.0.9 added support for multiple side dishes - DT
 * 0.0.8 added side dish - DT
 *          0.0.7 refactored state, added billing item - DT
 *          0.0.6 state with enum - DS
 *          0.0.5 multiple state conditions - DS
 *          0.0.4 redundancy clean up - DT
 *          0.0.3 added variable state - DS
 *          0.0.2 removed local variable StringBuilder
 **/
public class OrderItem extends AbstractDocument {

	@DBRef
	private Desk desk;

	@DBRef
	private User waiter;

	@DBRef
	private MenuItem item;

	@DBRef
	private List<MenuItem> sideDishes;

	private String guestWish;

	private List<OrderItemState> states;

	@DBRef
	@JsonIgnore
	private Billing billing;

	// constructor
	public OrderItem() {
		this.states = new ArrayList<>();
		addState(OrderItemState.State.NEW);
		this.sideDishes = new ArrayList<>();
	}

	public OrderItem(User user, Desk desk, MenuItem menuItem, String guestwish) {
		setWaiter(user);
		setDesk(desk);
		setGuestWish(guestwish);
		setItem(menuItem);
		this.states = new ArrayList<>();
		addState(OrderItemState.State.NEW);
		this.sideDishes = new ArrayList<>();
	}

	/**
	 * method to add new state
	 *
	 * @param state
	 */
	public void addState(OrderItemState.State state) {
		if (this.states.size() == 0 || !getCurrentState().equals(state)) {
			this.states.add(new OrderItemState(state));
		}
	}

	/**
	 * get current state
	 *
	 * @return state
	 */
	public OrderItemState.State getCurrentState() {
		return this.states.size() != 0 ? this.states.get(this.states.size() - 1).getState() : null;
	}

	// getter and setter

	public Desk getDesk() {
		return desk;
	}

	public void setDesk(Desk desk) {
		this.desk = desk;
	}

	public User getWaiter() {
		return waiter;
	}

	public void setWaiter(User waiter) {
		this.waiter = waiter;
	}

	public MenuItem getItem() {
		return item;
	}

	public void setItem(MenuItem item) {
		this.item = item;
	}

	public String getGuestWish() {
		return guestWish;
	}

	public void setGuestWish(String guestWish) {
		this.guestWish = guestWish;
	}

	public List<OrderItemState> getStates() {
		return states;
	}

	public void setStates(List<OrderItemState> states) {
		this.states = states;
	}

	public Billing getBilling() {
		return billing;
	}

	/**
	 * is not explicit set, instead it is set by billingItem event
	 * only use in
	 */
	@Deprecated
	public void setBilling(Billing billing) {
		this.billing = billing;
	}

	public List<MenuItem> getSideDishes() {
		return sideDishes;
	}

	public void setSideDishes(List<MenuItem> sideDishes) {
		this.sideDishes = sideDishes;
	}

	@Override
	public String toString() {
		return "OrderItem{" +
			"Id: " +
			this.id +
			", Item: " +
			this.item +
			"}";
	}
}
