package de.neoventus.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Menu Categories
 *
 * @author Julian Beck
 * @version 0.0.1 created categories for Menu
 *
 */
public class MenuItemCategoryDto implements Serializable {

	private String id;



	@NotNull
	private String name;

	private String parent;
	private boolean activeItem;

	public MenuItemCategoryDto(){}

	public MenuItemCategoryDto(String name, String parent) {
		this.name = name;
		this.parent = parent;
	}

	// Setter / Getter
	public boolean isActiveItem() {
		return activeItem;
	}

	public void setActiveItem(boolean activeItem) {
		this.activeItem = activeItem;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
}
