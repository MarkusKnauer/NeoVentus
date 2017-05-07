package de.neoventus.persistence.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julian Beck
 * @version 0.0.1 JSON-Buildingclass for Order-Overview
 **/
public class OrderItemOutput {


    private String desk;

    private String waiter;

    private String menuItem;

    private String category;

    private String guestWish;

    private Integer menuItemCounter;

    private Double price;

    private List<String> orderItemIds;

//	private List<OrderItemState> state;

//	private List<Long> stateTime;









	public OrderItemOutput(){
		orderItemIds = new ArrayList<String>();
	}

	public OrderItemOutput(String desk, String waiter, String menuItem, String guestWish, Integer menuItemCounter, Double price, List<String> orderItemIds) {
		this.desk = desk;
		this.waiter = waiter;
		this.menuItem = menuItem;
		this.guestWish = guestWish;
		this.menuItemCounter = menuItemCounter;
		this.price = price;
		this.orderItemIds = orderItemIds;
	}


	public void addOrderItemIds(String id){
		orderItemIds.add(id);
	}


	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<String> getOrderItemIds() {
		return orderItemIds;
	}

	public void setOrderItemIds(List<String> orderItemIds) {
		this.orderItemIds = orderItemIds;
	}

	public String getDesk() {
		return desk;
	}

	public void setDesk(String desk) {
		this.desk = desk;
	}

	public String getWaiter() {
		return waiter;
	}

	public void setWaiter(String waiter) {
		this.waiter = waiter;
	}

	public String getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(String menuItem) {
		this.menuItem = menuItem;
	}

	public String getGuestWish() {
		return guestWish;
	}

	public void setGuestWish(String guestWish) {
		this.guestWish = guestWish;
	}

	public Integer getMenuItemCounter() {
		return menuItemCounter;
	}

	public void setMenuItemCounter(Integer menuItemCounter) {
		this.menuItemCounter = menuItemCounter;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
