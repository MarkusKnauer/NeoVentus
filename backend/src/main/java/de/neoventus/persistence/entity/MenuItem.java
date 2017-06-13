package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Thanner, Julian Beck, Tim Heidelbach, Markus Knauer
 * @version 0.0.7 new variable sideDishGroup - MK
 * 			0.0.6 new variable menuItemCategory - JB
 * 			0.0.5 changed menuItemId to number DT
 *          0.0.4 removed local variable StringBuilder
 *          0.0.3 code clean up
 *          0.0.2 added full constructor, currency and print currency method
 **/
public class MenuItem extends AbstractDocument {

	@Indexed(unique = true)
	private String name;

	private String shortName;

	private Double price;

	private String description;

	private String mediaUrl;

	private Integer number;

	private List<String> notices;

	private String currency;

	private boolean activItem;

	@DBRef
	private MenuItemCategory menuItemCategory;

	@DBRef
	private SideDishGroup sideDishGroup;

	// constructor
	public MenuItem() {
		this.notices = new ArrayList<>();
		sideDishGroup = null;
		setActivItem(true);
	}

	public MenuItem(MenuItemCategory menuItemCategory, String name, String shortName, Double price, String currency, String description, String mediaUrl,
					List<String> notices) {
		this.menuItemCategory = menuItemCategory;
		this.name = name;
		this.shortName = shortName;
		this.price = price;
		this.currency = currency;
		this.description = description;
		this.mediaUrl = mediaUrl;
		this.notices = notices;
		sideDishGroup = null;
		setActivItem(true);

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


	public boolean isActivItem() {
		return activItem;
	}

	public void setActivItem(boolean activItem) {
		this.activItem = activItem;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public MenuItemCategory getMenuItemCategory() {return menuItemCategory;}

	public void setMenuItemCategory(MenuItemCategory menuItemCategory) {this.menuItemCategory = menuItemCategory;}

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
//		sideDishGroup.setSideDishName(name);
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

	public void setSideDishGroup(SideDishGroup sideDishGroup) {
		this.sideDishGroup = sideDishGroup;
	}

	public SideDishGroup getSideDishGroup() {
		return sideDishGroup;
	}

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
