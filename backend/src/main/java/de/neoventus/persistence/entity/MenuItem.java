package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Thanner, Julian Beck, Tim Heidelbach, Markus Knauer
 * @version 0.0.7 new variable sideDish - MK
 * 			0.0.6 new variable category - JB
 * 			0.0.5 changed menuItemId to number DT
 *          0.0.4 removed local variable StringBuilder
 *          0.0.3 code clean up
 *          0.0.2 added full constructor, currency and print currency method
 **/
public class MenuItem extends AbstractDocument {

	@Indexed(unique = true)
	private String name;

	private Double price;

	private String description;

	private String mediaUrl;

	private Integer number;

	private List<String> notices;

	private String currency;

	@DBRef
	private MenuItemCategory category;

	@DBRef
	private SideDish sideDish;

	// constructor
	public MenuItem() {
		this.notices = new ArrayList<>();
		sideDish = null;
	}

	public MenuItem(MenuItemCategory category, String name, Double price, String currency, String description, String mediaUrl,
					List<String> notices) {
		this.category = category;
		this.name = name;
		this.price = price;
		this.currency = currency;
		this.description = description;
		this.mediaUrl = mediaUrl;
		this.notices = notices;
		sideDish = null;

	}

	/**
	 * print currency sign
	 *
	 * @param currency currency to print sign for
	 * @return currency sign string, defaults to currency name
	 */
	public String printCurrencySign(String currency) {
		if(currency.equals("EUR")) return "€";
		if(currency.equals("USD")) return "$";
		return currency;
	}

	// getter and setter


	public MenuItemCategory getCategory() {return category;}

	public void setCategory(MenuItemCategory category) {this.category = category;}

	public Integer getMenuItemID() {
		return number;
	}

	public void setMenuItemID(Integer menuItemID) {
		this.number = menuItemID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
//		sideDish.setSideDishName(name);
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

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

    public void setSideDish(SideDish sideDish) {
        this.sideDish = sideDish;
    }

    public SideDish getSideDish(){ return sideDish;}

	@Override
	public String toString() {
		return "MenuItem{" +
				"Id: " +
				this.id +
				", Name: " +
				this.name +
				", Price: " +
				this.price +
				"}";
	}
}
