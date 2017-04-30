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


	public MenuItemCategoryDto(){}

	public MenuItemCategoryDto(String name, String parent) {
		this.name = name;
		this.parent = parent;
	}

	// Setter / Getter
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
