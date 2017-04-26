package de.neoventus.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Tim Heidelbach
 * @version 0.0.1
 */
public class BillingItemDto implements Serializable {

	private String id;

	@NotNull
	private String item;

	private double price;

	// Getter and setter

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
