package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Dennis Thanner, Tim Heidelbach
 **/
public class Billing extends AbstractDocument {

    private Date billedAt;

    private Double totalPaid;

    private List<BillingItem> items;

	@DBRef
	private User waiter;

    // constructor
    public Billing() {
        this.billedAt = new Date();
        this.items = new ArrayList<>();
    }

	public Billing(Date billedAt, Double totalPaid, List<BillingItem> items, User waiter) {
		this.billedAt = billedAt;
		this.totalPaid = totalPaid;
		this.items = items;
		this.waiter = waiter;
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

	public User getWaiter() {
		return waiter;
	}

	public void setWaiter(User waiter) {
		this.waiter = waiter;
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
