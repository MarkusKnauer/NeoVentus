package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
public class Reservation extends AbstractDocument {
	
	@DBRef
	private User reservedBy;
	
	@DBRef
	private Desk desk;
	
	private Date createdAt;
	
	private Date time;
	
	// constructor
	
	public Reservation() {
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Reservation{Id: ");
		sb.append(this.id);
		sb.append(", Time: ");
		sb.append(this.time);
		sb.append(", Desk: ");
		sb.append(this.desk);
		sb.append("}");
		return sb.toString();
	}
}
