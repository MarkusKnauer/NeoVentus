package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author Dennis Thanner, Tim Heidelbach
 * @version 0.0.2 removed local variable StringBuilder
 **/
public class OrderItem extends AbstractDocument {

    @DBRef
    private Desk desk;

    @DBRef
    private User waiter;

    @DBRef
    private MenuItem item;

    private Integer orderID;

    private String guestWish;

    public OrderItem() {
    }

    public OrderItem(User user, Desk desk, MenuItem menuItem, String guestwish) {
        setWaiter(user);
        setDesk(desk);
        setGuestWish(guestwish);
        setItem(menuItem);
    }

    // getter and setter
    public Integer getOrderID() {return orderID;}

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

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
        return "OrderItem{" +
                "Id: " +
                this.id +
                ", Item: " +
                this.item +
                "}";
    }
}
