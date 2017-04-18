package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

/**
 * @author Dennis Thanner, Tim Heidelbach
 * @version 0.0.2 removed local variable StringBuilder
 **/
public class Reservation extends AbstractDocument {

	@DBRef
	private User reservedBy;

	@DBRef
	private Desk desk;

	private Date createdAt;

	private Date time;

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

	@Override
	public String toString() {
		return "Reservation{Id: " +
				this.id +
				", Time: " +
				this.time +
				", Desk: " +
				this.desk +
				"}";
	}
}
