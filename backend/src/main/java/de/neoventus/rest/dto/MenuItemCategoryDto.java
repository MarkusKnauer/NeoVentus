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

	private String path;


	public MenuItemCategoryDto(){}

	public MenuItemCategoryDto(String name, String path) {
		this.name = name;
		this.path = path;
	}

	// Setter / Getter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
