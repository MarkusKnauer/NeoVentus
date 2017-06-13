package de.neoventus.persistence.entity;


import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;

/**
 * A MenuItem could have a list of possible side dishes. For example, to a hamburger you can have wedges or a salat etc.
 *
 * @author Markus Knauer, Julian Beck, Dennis Thanner
 */

public class SideDishGroup extends AbstractDocument {

	@Indexed(unique = true)
	private String name;
	private boolean selectionRequired;
	private boolean activItem;

	@DBRef
	private ArrayList<MenuItem> sideDishes;

	//constructor

	public SideDishGroup() {
		setActivItem(true);
		sideDishes = new ArrayList<>();
		setActivItem(true);
	}

	public SideDishGroup(String name, Boolean selectionRequired) {
		this.name = name;
		this.selectionRequired = selectionRequired;
		sideDishes = new ArrayList<>();
		setActivItem(true);
	}


	// getter, setter

	public boolean isActivItem() {
		return activItem;
	}

	public void setActivItem(boolean activItem) {
		this.activItem = activItem;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelectionRequired() {
		return selectionRequired;
	}

	public void setSelectionRequired(boolean selectionRequired) {
		this.selectionRequired = selectionRequired;
	}

	public ArrayList<MenuItem> getSideDishes() {
		return sideDishes;
	}

	public void addSideDish(MenuItem menuItem) {
		sideDishes.add(menuItem);
	}

	//todo SideDishGroup examples have to be inialized and linked to the menuitems - MK

}
