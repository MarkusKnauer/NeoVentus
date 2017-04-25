package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author Dennis Thanner, Tim Heidelbach
 * @version 0.0.4 redundancy clean up - DT
 * 			0.0.3 added variable state - DS
 *          0.0.2 removed local variable StringBuilder
 **/
public class OrderItem extends AbstractDocument {

    @DBRef
    private Desk desk;

    @DBRef
    private User waiter;

    @DBRef
    private MenuItem item;

    private String guestWish;

    private String state;

    public OrderItem() {
    }

    public OrderItem(User user, Desk desk, MenuItem menuItem, String guestwish, String state) {
        setWaiter(user);
        setDesk(desk);
        setGuestWish(guestwish);
        setItem(menuItem);
        setState(state);
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
