package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.index.Indexed;

/**
 * @author Dennis Thanner, Tim Heidelbach
 * @version 0.0.2 removed local variable StringBuilder
 **/
public class Desk extends AbstractDocument {

    @Indexed(unique = true)
    private int number;

    private int seats;

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
        return "Desk{" +
                "Id: " +
                this.id +
                ", Number: " +
                this.number +
                ", Seats: " +
                this.seats +
                "}";
    }
}
