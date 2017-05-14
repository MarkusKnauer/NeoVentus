package de.neoventus.persistence.repository.advanced.impl.aggregation;

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * mapping class for aggregation
 *
 * @author Dennis Thanner
 * @version 0.0.3 added support for multiple side dishes - DT
 *          0.0.2 added sideDish - DT
 */
public class OrderDeskAggregationDto implements Serializable {

	private User waiter;

	private MenuItem item;

	private int count;

	private List<MenuItem> sideDishes;

	// constructor

	public OrderDeskAggregationDto() {
		this.sideDishes = new ArrayList<>();
	}


	// getter and setter

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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<MenuItem> getSideDishes() {
		return sideDishes;
	}

	public void setSideDishes(List<MenuItem> sideDishes) {
		this.sideDishes = sideDishes;
	}

}
