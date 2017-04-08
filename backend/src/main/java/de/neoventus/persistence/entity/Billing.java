package de.neoventus.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
public class Billing extends AbstractDocument {
	
	private Date billedAt;
	
	private Double totalPaid;
	
	private List<BillingItem> items;
	
	// constructor
	
	public Billing() {
		this.billedAt = new Date();
		this.items = new ArrayList<>();
	}
	
	// getter and setter
	
	public Date getBilledAt() {
		return billedAt;
	}
	
	public void setBilledAt(Date billedAt) {
		this.billedAt = billedAt;
	}
	
	public Double getTotalPaid() {
		return totalPaid;
	}
	
	public void setTotalPaid(Double totalPaid) {
		this.totalPaid = totalPaid;
	}
	
	public List<BillingItem> getItems() {
		return items;
	}
	
	public void setItems(List<BillingItem> items) {
		this.items = items;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Billing{Id:");
		sb.append(this.id);
		sb.append(", TotalPaid: ");
		sb.append(this.totalPaid);
		sb.append(", CountItems: ");
		sb.append(this.items.size());
		sb.append("}");
		return sb.toString();
	}
}
