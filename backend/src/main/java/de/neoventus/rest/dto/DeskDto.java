package de.neoventus.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Class description
 *
 * @author Dominik Streif
 * @version 0.0.1
 */
public class DeskDto implements Serializable {

	private String id;

	@NotNull
	private Integer seats;

	private String beaconUUID;

	private String beaconMajor;

	private String beaconMinor;

	private boolean activItem;

	// constructor

	public DeskDto() {

	}

	// getter and setter


	public boolean isActivItem() {
		return activItem;
	}

	public void setActivItem(boolean activItem) {
		this.activItem = activItem;
	}

	public String getBeaconUUID() {
		return beaconUUID;
	}

	public void setBeaconUUID(String beaconUUID) {
		this.beaconUUID = beaconUUID;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}


}
