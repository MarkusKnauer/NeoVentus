package de.neoventus.persistence.repository.advanced.impl.aggregation;

/**
 * profile details
 *
 * @author Dennis Thanner
 */
public class UserProfileDetails {

	private double revenueToday;

	private double tipsToday;

	private int exp;

	private int level;

	private long expNextLevel;

	// constructors

	public UserProfileDetails() {
	}


	// methods

	// getter and setter

	public double getRevenueToday() {
		return revenueToday;
	}

	public void setRevenueToday(double revenueToday) {
		this.revenueToday = revenueToday;
	}

	public double getTipsToday() {
		return tipsToday;
	}

	public void setTipsToday(double tipsToday) {
		this.tipsToday = tipsToday;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getExpNextLevel() {
		return expNextLevel;
	}

	public void setExpNextLevel(long expNextLevel) {
		this.expNextLevel = expNextLevel;
	}
}
