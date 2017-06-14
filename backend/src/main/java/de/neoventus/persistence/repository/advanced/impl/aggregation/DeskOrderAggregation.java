package de.neoventus.persistence.repository.advanced.impl.aggregation;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Thanner
 */
public class DeskOrderAggregation {

	private Desk desk;

	private List<ObjectCountAggregation<MenuItem>> items = new ArrayList<>();

	private List<User> waiters = new ArrayList<>();
	// constructors


	// methods

	// getter and setter


	public Desk getDesk() {
		return desk;
	}

	public void setDesk(Desk desk) {
		this.desk = desk;
	}

	public List<ObjectCountAggregation<MenuItem>> getItems() {
		return items;
	}

	public void setItems(List<ObjectCountAggregation<MenuItem>> items) {
		this.items = items;
	}

	public List<User> getWaiters() {
		return waiters;
	}

	public void setWaiters(List<User> waiters) {
		this.waiters = waiters;
	}
}
