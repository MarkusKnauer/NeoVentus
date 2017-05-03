package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;

/**
 * Menu Categories
 *
 * @author Julian Beck
 * @version 0.0.1 created categories for Menu
 */
public class MenuItemCategory extends AbstractDocument {

	@Indexed(unique = true)
	private String name;

	@DBRef
	private ArrayList<MenuItemCategory> subcategory;

	@DBRef
	private MenuItemCategory parent;


	public MenuItemCategory() {
		this.subcategory = new ArrayList<MenuItemCategory>();
		this.parent = null;
	}

	public MenuItemCategory(String name) {
		this.name = name;
		this.subcategory = new ArrayList<MenuItemCategory>();
		this.parent = null;
	}


	public void addSubcategory(MenuItemCategory cat) {
		getSubcategory().add(cat);
	}


	// Setter / Getter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<MenuItemCategory> getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(ArrayList<MenuItemCategory> subcategory) {
		this.subcategory = subcategory;
	}

	public MenuItemCategory getParent() {
		return parent;
	}

	public void setParent(MenuItemCategory parent) {
		this.parent = parent;

	}
}
