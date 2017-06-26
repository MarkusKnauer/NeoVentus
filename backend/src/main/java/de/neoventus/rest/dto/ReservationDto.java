package de.neoventus.rest.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Tim Heidelbach
 * @author Dennis Thanner
 */
public class ReservationDto implements Serializable {

	private String id;

	@NotNull
	@NotEmpty
	private String[] desk;

	@NotNull
	private Date time;

	private Integer duration;

	private String reservedBy;


	private String reservationName;

	// Getter and setter

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReservedBy() {
		return reservedBy;
	}

	public void setReservedBy(String reservedBy) {
		this.reservedBy = reservedBy;
	}

	public String[] getDesk() {
		return desk;
	}

	public void setDesk(String[] desk) {
		this.desk = desk;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getReservationName() {
		return reservationName;
	}

	public void setReservationName(String reservationName) {
		this.reservationName = reservationName;
	}
}
