package de.neoventus.rest.dto;

import de.neoventus.persistence.entity.AbstractDocument;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Menu Categories
 *
 * @author Julian Beck
 * @version 0.0.1 created categories for Menu
 *
 */
public class MenuItemCategoryDto extends AbstractDocument {

	@Indexed(unique = true)
	private String name;

	private String parent;


	public MenuItemCategoryDto(){}

	public MenuItemCategoryDto(String name, String parent) {
		this.name = name;
		this.parent = parent;
	}

	// Setter / Getter
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
