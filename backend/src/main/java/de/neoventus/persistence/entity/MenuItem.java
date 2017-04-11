package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Thanner, Julian Beck
 * @version 0.0.3
 *
 * Change Log:
 * 0.0.3 code clean up
 * 0.0.2 added full constructor, currency and print currency method
 **/
public class MenuItem extends AbstractDocument {

	@Indexed(unique = true)
	private String name;

	private Double price;

	private String description;

	private String mediaUrl;

	private List<String> notices;

	private String currency;

	public MenuItem() {
		this.notices = new ArrayList<>();
	}

	// constructor
	public void MenuItem(String name, Double price, String currency, String description, String mediaUrl, List<String> notices) {
		this.name = name;
		this.price = price;
		this.currency = currency;
		this.description = description;
		this.mediaUrl = mediaUrl;
		this.notices = notices;
	}

	/**
	 * print currency sign
	 *
	 * @param currency currency to print sign for
	 * @return currency sign string, defaults to currency name
	 */
	public String printCurrencySign(String currency) {
		if (currency.equals("EUR")) return "€";
		if (currency.equals("USD")) return "$";
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

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