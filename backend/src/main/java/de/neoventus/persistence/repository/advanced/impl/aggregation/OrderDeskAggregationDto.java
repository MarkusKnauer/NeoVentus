package de.neoventus.persistence.repository.advanced.impl.aggregation;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * mapping class for aggregation
 *
 * @author Dennis Thanner
 * @version 0.0.4 added fields for multi aggregation usage - DT
 *          0.0.3 added support for multiple side dishes - DT
 *          0.0.2 added sideDish - DT
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDeskAggregationDto implements Serializable {

	private User waiter;

	private MenuItem item;

	private Integer count;

	private List<MenuItem> sideDishes;

	private String guestWish;

	private List<String> orderIds;

	// constructor

	public OrderDeskAggregationDto() {
		this.sideDishes = new ArrayList<>();
		this.orderIds = new ArrayList<>();
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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<MenuItem> getSideDishes() {
		return sideDishes;
	}

	public void setSideDishes(List<MenuItem> sideDishes) {
		this.sideDishes = sideDishes;
	}

	public String getGuestWish() {
		return guestWish;
	}

	public void setGuestWish(String guestWish) {
		this.guestWish = guestWish;
	}

	public List<String> getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(List<String> orderIds) {
		this.orderIds = orderIds;
	}
}
