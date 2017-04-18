package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Thanner, Julian Beck, Tim Heidelbach
 * @version 0.0.4 removed local variable StringBuilder
 *          0.0.3 code clean up
 *          0.0.2 added full constructor, currency and print currency method
 **/
public class MenuItem extends AbstractDocument {

    @Indexed(unique = true)
    private String name;

    private Double price;

    private String description;

    private String mediaUrl;

    private Integer menuItemID;

    private List<String> notices;

    private String currency;

    // constructor
    public MenuItem() {
        this.notices = new ArrayList<>();
    }

    // Setter for All Attributes
    public void setAll(String name, Double price, String currency, String description, String mediaUrl,
                       List<String> notices) {
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.description = description;
        this.mediaUrl = mediaUrl;
        this.notices = notices;
    }

    /**
     * print currency sign
     *
     * @param currency currency to print sign for
     * @return currency sign string, defaults to currency name
     */
    public String printCurrencySign(String currency) {
        if (currency.equals("EUR")) return "€";
        if (currency.equals("USD")) return "$";
        return currency;
    }

    // getter and setter

    public Integer getMenuItemID() {
        return menuItemID;
    }

    public void setMenuItemID(Integer menuItemID) {
        this.menuItemID = menuItemID;
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

    @Override
    public String toString() {
        return "MenuItem{" +
                "Id: " +
                this.id +
                ", Name: " +
                this.name +
                ", Price: " +
                this.price +
                "}";
    }
}
