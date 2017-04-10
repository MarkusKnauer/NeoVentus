package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
public class User extends AbstractDocument {

	@Indexed(unique = true)
	private String username;
	private int status; //Status 0 = CEO; 1 = employees; 2 = guests
	private String password;

	private List<String> permissions;

	@DBRef
	private List<Desk> desks;

	// constructor

	public User() {
		this.desks = new ArrayList<>();
		this.permissions = new ArrayList<>();
	}

	// getter and setter

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getStatus(){
		return status;
	}
	public void setStatus(int status){
		this.status =status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public List<Desk> getDesks() {
		return desks;
	}

	public void setDesks(List<Desk> desks) {
		this.desks = desks;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("User{");
		sb.append("Id: ");
		sb.append(this.id);
		sb.append(", Username: ");
		sb.append(this.username);
		sb.append(", Permissions: ");
		sb.append(this.permissions);
		sb.append(", Desks: ");
		sb.append(this.desks);
		sb.append("}");
		return sb.toString();
	}
}
