package de.neoventus.persistence.entity;


import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;

/**
 * A MenuItem could have a list of possible side dishes. For example, to a hamburger you can have wedges or a salat etc.
 *
 * @author Markus Knauer, Julian Beck, Dennis Thanner
 * @version 0.0.4 added DBRef - DT
 *          0.0.3 undo Three SideDishGroup refs MK
 *          0.0.2 add Three SideDishGroup refs JB
 *          0.0.1 Creation - MK
 */

public class SideDishGroup extends AbstractDocument {

	@Indexed(unique = true)
	private String name;

	@DBRef
	private ArrayList<MenuItem> sideDishes;

	//constructor

	public SideDishGroup() {
		sideDishes = new ArrayList<>();
	}

	public SideDishGroup(String name) {
		this.name = name;
		sideDishes = new ArrayList<>();
	}


	// getter, setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<MenuItem> getSideDishes() {
		return sideDishes;
	}

	public void addSideDish(MenuItem menuItem) {
		sideDishes.add(menuItem);
	}

	//todo SideDishGroup examples have to be inialized and linked to the menuitems - MK

}
