package de.neoventus.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Julian Beck, Dominik Streif, Dennis Thanner
 * @version 0.0.7 added side dish - DT
 *          0.0.6 state refactoring - DT
 *          0.0.5 state with enum - DS
 *          0.0.4 multiple state conditions - DS
 *          0.0.3 redundancy clean up - DT
 *          0.0.2 added variable state - DS
 *          0.0.1
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

	private List<String> sideDishIds;

	// constructors
	public OrderItemDto() {
		this.sideDishIds = new ArrayList<>();
	}

	public OrderItemDto(Integer user, Integer desk, Integer menuItem, String guestwish) {
		setWaiter(user);
		setDeskNumber(desk);
		setGuestWish(guestwish);
		setMenuItemNumber(menuItem);
		this.sideDishIds = new ArrayList<>();
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

	public void setWaiter(Integer waiter) {
		this.waiter = waiter;
	}

	public Integer getMenuItemNumber() {
		return menuItemNumber;
	}

	public void setMenuItemNumber(Integer menuItem) {
		this.menuItemNumber = menuItem;
	}

	public String getGuestWish() {
		return guestWish;
	}

	public void setGuestWish(String guestWish) {
		this.guestWish = guestWish;
	}

	public List<String> getSideDishIds() {
		return sideDishIds;
	}

	public void setSideDishIds(List<String> sideDishIds) {
		this.sideDishIds = sideDishIds;
	}
}
