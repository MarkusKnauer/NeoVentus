package de.neoventus.persistence.repository.advanced.impl.aggregation;

/**
 * aggregation result to get count for objects
 *
 * @author Dennis Thanner
 */
public class ObjectCountAggregation<T> {

	private T object;

	private int count;

	// constructor
	public ObjectCountAggregation() {
	}

	// getter and setter

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
