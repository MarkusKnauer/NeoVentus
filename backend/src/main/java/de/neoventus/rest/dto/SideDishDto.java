package de.neoventus.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Menu Categories
 *
 * @author Julian Beck
 * @version 0.0.1 created dishes for Meal
 *
 */
public class SideDishDto implements Serializable {

	private String id;

	@NotNull
	private String nameDish;

	private String menuItemName;

	private String targetMenuItem;

	private boolean activItem;
	public SideDishDto(){}

	public SideDishDto(String nameDish, String menuItemName) {
		this.nameDish = nameDish;
		this.menuItemName = menuItemName;
	}

	// Setter / Getter
	public boolean isActivItem() {
		return activItem;
	}

	public void setActivItem(boolean activItem) {
		this.activItem = activItem;
	}


	public String getTargetMenuItem() {
		return targetMenuItem;
	}

	public void setTargetMenuItem(String targetMenuItem) {
		this.targetMenuItem = targetMenuItem;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNameDish() {
		return nameDish;
	}

	public void setNameDish(String nameDish) {
		this.nameDish = nameDish;
	}

	public String getMenuItemName() {
		return menuItemName;
	}

	public void setMenuItemName(String menuItemName) {
		this.menuItemName = menuItemName;
	}
}
