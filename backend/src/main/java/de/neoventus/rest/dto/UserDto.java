package de.neoventus.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
public class UserDto implements Serializable {

	private String id;

	@NotNull
	private String username;

	private String password;

	private List<Integer> permissions = new ArrayList<>();

	private List<String> desks = new ArrayList<>();

	private String firstName;

	private String lastName;

	// constructor

	public UserDto() {
	}

	// getter and setter

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Integer> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Integer> permissions) {
		this.permissions = permissions;
	}

	public List<String> getDesks() {
		return desks;
	}

	public void setDesks(List<String> desks) {
		this.desks = desks;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
