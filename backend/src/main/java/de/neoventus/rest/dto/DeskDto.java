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


	// constructor

	public DeskDto() {

	}

	// getter and setter

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
