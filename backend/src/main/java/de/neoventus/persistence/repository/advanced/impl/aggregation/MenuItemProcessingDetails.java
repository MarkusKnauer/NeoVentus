package de.neoventus.persistence.repository.advanced.impl.aggregation;

/**
 * aggregation object for kitchen / bar processing details for a menu item
 *
 * @author Dennis Thanner
 */
public class MenuItemProcessingDetails {

	private long timeSec;

	// getter and setter
	public long getTimeSec() {
		return timeSec;
	}

	public void setTimeSec(long timeSec) {
		this.timeSec = timeSec;
	}
}
