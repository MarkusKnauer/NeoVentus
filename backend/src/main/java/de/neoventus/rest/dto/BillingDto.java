package de.neoventus.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author Tim Heidelbach, Dennis Thanner
 */
public class BillingDto implements Serializable {

	private String id;

	private Double totalPaid;

	@NotNull
	private String waiter;

	@NotNull
	private List<String> items;

	// Setter and getter

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(Double totalPaid) {
		this.totalPaid = totalPaid;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	public String getWaiter() {
		return waiter;
	}

	public void setWaiter(String waiter) {
		this.waiter = waiter;
	}
}
