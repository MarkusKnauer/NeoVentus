package de.neoventus.persistence.entity;


import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;

/**
 * A MenuItem could have a list of possible side dishes. For example, to a hamburger you can have wedges or a salat etc.
 *
 * @author Markus Knauer
 * @version 0.0.3 undo Three SideDish refs MK
 *          0.0.2 add Three SideDish refs JB
 *          0.0.1 Creation - MK
 */

public class SideDish extends AbstractDocument {

    @Indexed
    private String name;

    private ArrayList<MenuItem> sideDish;

    //constructor

    public SideDish(){
        sideDish = new ArrayList<>();
    }

    public SideDish(String name) {
        this.name = name;
        sideDish = null;//new ArrayList<MenuItem>();
    }


    // getter, setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<MenuItem> getSideDish() {
        return sideDish;
    }

    public void addSideDish(MenuItem menuItem) {
        sideDish.add(menuItem);
    }

    //todo SideDish examples have to be inialized and linked to the menuitems - MK

}
