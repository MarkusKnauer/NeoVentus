package de.neoventus.persistence.repository.advanced.impl.aggregation;

/**
 * aggregation result to get popular guest wishes
 *
 * @author Dennis Thanner
 */
public class GuestWishCount {

	private String guestWish;

	private int count;

	// constructor
	public GuestWishCount() {
	}

	// getter and setter
	public String getGuestWish() {
		return guestWish;
	}

	public void setGuestWish(String guestWish) {
		this.guestWish = guestWish;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
