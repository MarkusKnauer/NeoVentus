package de.neoventus.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Markus Knauer, Julian Beck
 * @version 0.0.2 new varibale category - JB
 *          0.0.1 created by - MK
 */
public class MenuDto implements Serializable {

    private String id;

    @NotNull
    private String name;

    private Double price;

    private String description;

    private String mediaUrl;

    private Integer number;

    private List<String> notices;

    private String currency;

    private String category;


    // constructor
    public MenuDto(){}

    // getter and setter


	public String getCategory() {return category;}

	public void setCategory(String category) {this.category = category;}

	public Integer getMenuItemID() {
        return number;
    }

    public void setMenuItemID(Integer menuItemID) {
        this.number = menuItemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public List<String> getNotices() {
        return notices;
    }

    public void setNotices(List<String> notices) {
        this.notices = notices;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

}
