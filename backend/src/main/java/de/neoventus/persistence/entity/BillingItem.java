package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
public class BillingItem {
	
	@DBRef
	private OrderItem item;
	
	private double price;
	
	// constructor
	
	public BillingItem() {
	}
	
	public BillingItem(OrderItem item, double price) {
		this.item = item;
		this.price = price;
	}
	
	// getter and setter
	
	public OrderItem getItem() {
		return item;
	}
	
	public void setItem(OrderItem item) {
		this.item = item;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("BillingItem{");
		sb.append("Item: ");
		sb.append(this.item);
		sb.append(", Price: ");
		sb.append(this.price);
		sb.append("}");
		return sb.toString();
	}
}
