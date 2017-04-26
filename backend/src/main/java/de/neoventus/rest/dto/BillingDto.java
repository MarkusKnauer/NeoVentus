package de.neoventus.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Tim
 * @version 0.0.1
 */
public class BillingDto implements Serializable {

	private String id;

	private Date billedAt;

	private Double totalPaid;

	@NotNull
	private List<String> items;

	// Setter and getter

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getBilledAt() {
		return billedAt;
	}

	public void setBilledAt(Date billedAt) {
		this.billedAt = billedAt;
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
}
