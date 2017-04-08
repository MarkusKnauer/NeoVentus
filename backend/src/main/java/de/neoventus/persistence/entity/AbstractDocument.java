package de.neoventus.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
@Document
public abstract class AbstractDocument {
	
	@Id
	protected String id;
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
