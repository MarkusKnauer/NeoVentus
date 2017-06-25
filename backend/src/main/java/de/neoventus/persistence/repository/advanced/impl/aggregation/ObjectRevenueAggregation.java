package de.neoventus.persistence.repository.advanced.impl.aggregation;

/**
 * @author Dennis Thanner
 */
public class ObjectRevenueAggregation<T> {

	private T object;

	private double revenue;

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public double getRevenue() {
		return revenue;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}
}
