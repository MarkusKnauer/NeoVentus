package de.neoventus.persistence.repository.advanced.impl.aggregation;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * mapping class for aggregation
 *
 * @author Tim Heidelbach
 */
public class DeskOverviewDetails {

	private Integer deskNumber;
	private Set<String> waiters;
	private Double totalPaid;
	private Date nextReservation;

	private String beaconUID;
	private String beaconMajor;
	private String beaconMinor;

	public DeskOverviewDetails() {
		waiters = new HashSet<>();
		nextReservation = null;
		totalPaid = 0.0;
	}

	public int getDeskNumber() {
		return deskNumber;
	}

	public void setDeskNumber(int deskNumber) {
		this.deskNumber = deskNumber;
	}

	public Set<String> getWaiters() {
		return waiters;
	}

	public void setWaiters(Set<String> waiters) {
		this.waiters = waiters;
	}

	public double getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(double totalPaid) {
		this.totalPaid = totalPaid;
	}

	public Date getNextReservation() {
		return nextReservation;
	}

	public void setNextReservation(Date nextReservation) {
		this.nextReservation = nextReservation;
	}

	public String getBeaconUID() {
		return beaconUID;
	}

	public void setBeaconUID(String beaconUID) {
		this.beaconUID = beaconUID;
	}

	public String getBeaconMajor() {
		return beaconMajor;
	}

	public void setBeaconMajor(String beaconMajor) {
		this.beaconMajor = beaconMajor;
	}

	public String getBeaconMinor() {
		return beaconMinor;
	}

	public void setBeaconMinor(String beaconMinor) {
		this.beaconMinor = beaconMinor;
	}

}
