package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author Dennis Thanner, Tim Heidelbach
 * @version 0.0.2 removed local variable StringBuilder
 **/
public class BillingItem {

    @DBRef
    private OrderItem item;

    private double price;

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
        return "BillingItem{" +
                "Item: " +
                this.item +
                ", Price: " +
                this.price +
                "}";
    }
}
