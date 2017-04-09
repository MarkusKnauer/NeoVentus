package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 * * ------------------------------------------------------------------
 * Change Date  : 09.04.2017
 * Change-Author: Julian Beck
 * Changes      : Add a full constructor, Variable: currency + print Sign
 * ------------------------------------------------------------------
 **/

public class MenuItem extends AbstractDocument {

	@Indexed(unique = true)
	private String name;

	private Double price;

	private String description;

	private String mediaUrl;

	private List<String> notices;

	private String currency;



	// constructor
	public void allSet(String name, Double price,String currency, String description, String mediaUrl, List<String> notices){
		this.name = name;
		this.price = price;
		this.currency = currency;
		this.description = description;
		this.mediaUrl = mediaUrl;
		this.notices = notices;
	}
	public MenuItem() {
		this.notices = new ArrayList<>();
	}

	// Transform Text to Sign
	public String printCurrencySign(String currency){
		if(currency.equals("EUR")) return"€";
		if(currency.equals("USD")) return"$";
		return currency;
	}

	// getter and setter

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getCurrency(){ return currency; }

	public void setCurrency(String currency) { this.currency = currency; }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public List<String> getNotices() {
		return notices;
	}

	public void setNotices(List<String> notices) {
		this.notices = notices;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MenuItem{");
		sb.append("Id: ");
		sb.append(this.id);
		sb.append(", Name: ");
		sb.append(this.name);
		sb.append(", Price: ");
		sb.append(this.price);
		sb.append("}");
		return sb.toString();
	}
}
