package de.neoventus.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Dennis Thanner, Tim Heidelbach
 * @version 0.0.3 removed member billingID
 *          0.0.2 removed local variable StringBuilder
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

	public Billing(Date billedAt, Double totalPaid, List<BillingItem> items) {
		this.billedAt = billedAt;
		this.totalPaid = totalPaid;
		this.items = items;
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
        return "Billing{Id:" +
                this.id +
                ", TotalPaid: " +
                this.totalPaid +
                ", CountItems: " +
                this.items.size() +
                "}";
    }
}
