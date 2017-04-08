package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
public class OrderItem extends AbstractDocument {
	
	@DBRef
	private Desk desk;
	
	@DBRef
	private User waiter;
	
	@DBRef
	private MenuItem item;
	
	private String guestWish;
	
	// constructor
	
	public OrderItem() {
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("OrderItem{");
		sb.append("Id: ");
		sb.append(this.id);
		sb.append(", Item: ");
		sb.append(this.item);
		sb.append("}");
		return sb.toString();
	}
}
