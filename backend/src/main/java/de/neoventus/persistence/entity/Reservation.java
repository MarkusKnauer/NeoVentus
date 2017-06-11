package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

/**
 * @author Dennis Thanner, Tim Heidelbach
 * @version 0.0.5 added full constructor - JB
 * 			0.0.4 redundancy clean up - DT
 * 			0.0.3 added field duration
 *          0.0.2 removed local variable StringBuilder
 **/
public class Reservation extends AbstractDocument {

	@DBRef
	private User reservedBy;

	@DBRef
	private Desk desk;

	private Date createdAt;

	private Date time;

	private String reservationName;

	private Integer duration;


	public Reservation() {
		this.createdAt = new Date();
	}

	public Reservation(User reservedBy, Desk desk, Date createdAt, Date time, Integer duration) {
		this.reservedBy = reservedBy;
		this.desk = desk;
		this.createdAt = createdAt;
		this.time = time;
		this.duration = duration;
		this.reservationName = null;
	}


	// getter and setter

	public User getReservedBy() {
		return reservedBy;
	}

	public void setReservedBy(User reservedBy) {
		this.reservedBy = reservedBy;
	}

	public Desk getDesk() {
		return desk;
	}

	public void setDesk(Desk desk) {
		this.desk = desk;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
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
		return this.reservationName;
	}

	public void setReservationName(String reservationName) {
		this.reservationName = reservationName;
	}

	@Override
	public String toString() {

		return "Reservation{Id: " +
				this.id +
				", Time: " +
				this.time +
				", Duration: " +
				this.duration +
				", Desk: " +
				this.desk +
			", Name: " +
			this.reservationName +
				"}";
	}
}
