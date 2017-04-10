package de.neoventus.persistence.entity;
import org.springframework.data.mongodb.core.index.Indexed;
/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
public class Desk extends AbstractDocument {
	@Indexed(unique = true)
	private int number;

	private int seats;

	// constructor

	public Desk() {
	}

	// getter and setter

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Desk{");
		sb.append("Id: ");
		sb.append(this.id);
		sb.append(", Number: ");
		sb.append(this.number);
		sb.append(", Seats: ");
		sb.append(this.seats);
		sb.append("}");
		return sb.toString();
	}
}
