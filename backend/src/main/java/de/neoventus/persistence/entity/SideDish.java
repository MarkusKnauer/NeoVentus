package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;

/**
 * A MenuItem could have a list of possible side dishes. For example, to a hamburger you can have wedges or a salat etc.
 *
 * @author Markus Knauer
 * @version 0.0.2 add Three SideDish refs,
 *          0.0.1 Creation - MK
 */

// Sides set: Sides could be directly connected to MenuItem OR Semantic Sides could be build e.q. salad-group (Ceasar, tomatato, potato,
// Danger !! Use method addParentalMeal NOT addSubMeal before save()
// Logic:   Schnitzel with french frites (ParentMeal with SubMeal) --> ParentMeal set automaticly his sub
//          Insert french frites is part of Schnitzel doesn't work --> Sub can't set dom


public class SideDish extends AbstractDocument {

    private int number;

    @Indexed
    private String sideDishName;

    @DBRef
    private MenuItem actualMenuItem;

    @DBRef
    private ArrayList<SideDish> subMeal;

    @DBRef
    private ArrayList<SideDish> parentMeal;


    public SideDish(){
        subMeal = null;
        parentMeal = null;
    }

    public SideDish(String name){
        sideDishName = name;
        subMeal =null;
        parentMeal = null;
    }
    public SideDish(MenuItem menuItem){
        actualMenuItem = menuItem;
        subMeal = null;
        parentMeal = null;
    }

    public SideDish(int number) {
        this.number = number;
        subMeal = new ArrayList<SideDish>();
        parentMeal = new ArrayList<SideDish>();
    }


    //Danger!! Doesn't work for adding new sides (BUT used as a subroutine in other cases)
   public void addsubMeal(SideDish sideDish){
        if(getSubMeal() == null) setSubMeal(new ArrayList<SideDish>());
        getSubMeal().add(sideDish);
   }

    public void addParentalMeal(SideDish sidedish){
        if(getParentMeal() == null) setParentMeal(new ArrayList<SideDish>());
        getParentMeal().add(sidedish);
    }


    public SideDish getLastSubMeal(){
        return getSubMeal().get(getSubMeal().size()-1);
    }

    public SideDish getLastParentMeal(){
       return getParentMeal().get(getParentMeal().size()-1);
    }


    public SideDish getSubMealTree(SideDish sidedish){
       for(SideDish m : getSubMeal()){
            if(sidedish.equals(m)) return m;
       }
       return null;
    }
    // Set multiple sideDishes


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSideDishName() {
        return sideDishName;
    }

    public void setSideDishName(String sideDishName) {
        this.sideDishName = sideDishName;
    }

    public MenuItem getActualMenuItem() {
        return actualMenuItem;
    }

    public void setActualMenuItem(MenuItem actualMenuItem) {
        this.actualMenuItem = actualMenuItem;
    }

    public ArrayList<SideDish> getSubMeal() {
        return subMeal;
    }

    public void setSubMeal(ArrayList<SideDish> subMeal) {
        this.subMeal = subMeal;
    }

    public ArrayList<SideDish> getParentMeal() {return parentMeal;}

    public void setParentMeal(ArrayList<SideDish> parentMeal) {
        this.parentMeal = parentMeal;
    }
}
