package de.neoventus.persistence.entity;

import java.util.ArrayList;

/**
 * A MenuItem could have a list of possible side dishes. For example, to a hamburger you can have wedges or a salat etc.
 *
 * @author Markus Knauer
 * @version 0.0.1
 */
public class SideDish extends AbstractDocument {

    private int number;

    private ArrayList<MenuItem> beilage;


    public SideDish(int number) {
        this.number = number;
        beilage = new ArrayList<>();
    }

    public void setBeilage(MenuItem menuItem) {
        beilage.add(menuItem);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    //todo SideDish examples have to be inialized and linked to the menuitems - MK

}
