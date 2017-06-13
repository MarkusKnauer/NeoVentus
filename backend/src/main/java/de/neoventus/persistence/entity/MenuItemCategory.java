package de.neoventus.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;

/**
 * Menu Categories
 *
 * @author Julian Beck, DS
 */
public class MenuItemCategory extends AbstractDocument {

	@Indexed(unique = true)
	private String name;

	@DBRef
	private ArrayList<MenuItemCategory> subcategory;

	@JsonIgnore
	@DBRef
	private MenuItemCategory parent;

	private boolean forKitchen;

	private boolean activItem;

	public MenuItemCategory() {
		this.subcategory = new ArrayList<MenuItemCategory>();
		this.parent = null;
		setActivItem(true);
	}

	public MenuItemCategory(String name) {
		this.name = name;
		this.subcategory = new ArrayList<MenuItemCategory>();
		this.parent = null;
	}
	public MenuItemCategory(String name,boolean forKitchen) {
		this.name = name;
		this.forKitchen = forKitchen;
		this.subcategory = new ArrayList<MenuItemCategory>();
		this.parent = null;
		setActivItem(true);
	}


	public void addSubcategory(MenuItemCategory cat) {
		getSubcategory().add(cat);
	}


	// Setter / Getter

	public boolean isActivItem() {
		return activItem;
	}

	public void setActivItem(boolean activItem) {
		this.activItem = activItem;
	}

	public String getName() {
		return name;
	}

	public boolean isForKitchen() {
		return forKitchen;
	}

	public void setForKitchen(boolean forKitchen) {
		this.forKitchen = forKitchen;
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
