package de.neoventus.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Julian
 * @version 0.0.1
 **/
public class OrderItemDto implements Serializable {


	private Integer deskNumber;

	@NotNull
	private Integer waiter;

	@NotNull
	private Integer menuItemNumber;

	private Integer reservationNumber;

	private Integer orderID;

	private String guestWish;

	public OrderItemDto() {
	}

	public OrderItemDto(Integer user, Integer desk, Integer menuItem, String guestwish) {
		setWaiter(user);
		setDeskNumber(desk);
		setGuestWish(guestwish);
		setMenuItemNumber(menuItem);
	}

	// getter and setter
	public Integer getReservation() {return reservationNumber;}

	public void setReservation(Integer reservation) {this.reservationNumber = reservation;}

	public Integer getOrderID() {return orderID;}

	public void setOrderID(Integer orderID) {
		this.orderID = orderID;
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

}
