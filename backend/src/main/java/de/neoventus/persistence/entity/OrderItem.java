package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Dennis Thanner, Tim Heidelbach
 * @version 0.0.6 state with enum - DS
 * 			0.0.5 multiple state conditions - DS
 *          0.0.4 redundancy clean up - DT
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

	private List<OrderItemState> state;

	private List<Long> stateTime;

    public OrderItem() {
		state = new ArrayList<OrderItemState>();
		stateTime = new ArrayList<Long>();
		setState(OrderItemState.NEW);
	}

	public OrderItem(User user, Desk desk, MenuItem menuItem, String guestwish) {
		setWaiter(user);
        setDesk(desk);
        setGuestWish(guestwish);
        setItem(menuItem);
		this.state = new ArrayList<OrderItemState>();
		stateTime = new ArrayList<Long>();
		setState(OrderItemState.NEW);
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

	public List<OrderItemState> getState() {
		return state;
	}

	public List<Long> getStateTime() {
		return stateTime;
	}

	public void setState(OrderItemState state) {
		if (this.state.size() == 0 || !getCurrentState().equals(state)) {
			this.state.add(state);
			Date d = new Date();
			stateTime.add(d.getTime());
		}
	}

	public OrderItemState getCurrentState() {
		return state.size() != 0 ? state.get(state.size() - 1) : null;
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
