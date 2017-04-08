package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
public class MenuItem extends AbstractDocument {
	
	@Indexed(unique = true)
	private String name;
	
	private Double price;
	
	private String description;
	
	private String mediaUrl;
	
	private List<String> notices;
	
	// constructor
	
	public MenuItem() {
		this.notices = new ArrayList<>();
	}
	
	// getter and setter
	
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
		StringBuilder sb = new StringBuilder();
		sb.append("MenuItem{");
		sb.append("Id: ");
		sb.append(this.id);
		sb.append(", Name: ");
		sb.append(this.name);
		sb.append(", Price: ");
		sb.append(this.price);
		sb.append("}");
		return sb.toString();
	}
}
