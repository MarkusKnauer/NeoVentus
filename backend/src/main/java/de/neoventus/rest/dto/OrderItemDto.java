package de.neoventus.rest.dto;

import de.neoventus.persistence.entity.OrderItemState;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Julian Beck, Dominik Streif, Dennis Thanner
 * @version 0.0.5 state with enum - DS
 * 			0.0.4 multiple state conditions - DS
 * 			0.0.3 redundancy clean up - DT
 * 			0.0.2 added variable state - DS
 * 			0.0.1
 **/
public class OrderItemDto implements Serializable {

	private String id;

	@NotNull
	private Integer deskNumber;

	@NotNull
	private Integer waiter;

	@NotNull
	private Integer menuItemNumber;

	private String guestWish;

	private List<OrderItemState> state;

	private List<Long> stateTime;

	public OrderItemDto() {
		state = new ArrayList<OrderItemState>();
		stateTime = new ArrayList<Long>();
		setState(OrderItemState.NEW);
	}

	public OrderItemDto(Integer user, Integer desk, Integer menuItem, String guestwish) {
		setWaiter(user);
		setDeskNumber(desk);
		setGuestWish(guestwish);
		setMenuItemNumber(menuItem);
		this.state = new ArrayList<OrderItemState>();
		stateTime = new ArrayList<Long>();
		setState(OrderItemState.NEW);
	}

	// getter and setter

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getDeskNumber() {
		return deskNumber;
	}

	public void setDeskNumber(Integer desk) {
		this.deskNumber = desk;
	}

	public Integer getWaiter() {
		return waiter;
	}

	public void setWaiter(Integer waiter) {this.waiter = waiter;}

	public Integer getMenuItemNumber() {return menuItemNumber;}

	public void setMenuItemNumber(Integer menuItem) {
		this.menuItemNumber = menuItem;
	}

	public String getGuestWish() {
		return guestWish;
	}

	public void setGuestWish(String guestWish) {
		this.guestWish = guestWish;
	}

	public OrderItemState getState() {
		return state.size() != 0 ? state.get(state.size() - 1) : null;
	}

	public void setState(OrderItemState state) {
		if (this.state.size() == 0 || !getState().equals(state)) {
			this.state.add(state);
			Date d = new Date();
			stateTime.add(d.getTime());
		}
	}

}
