package de.neoventus.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dennis Thanner, Julian Beck, Markus Knauer, Tim Heidelbach
 * @version 0.0.8 added workingTimeModell - JB
 * 			0.0.7 added first und lastname - DT
 *          0.0.6 removed local variable StringBuilder
 *          0.0.5 Add variable workerId - JB
 *          0.0.4 edit permissions as enum - MK
 *          0.0.3 user status clean up - DT
 *          0.0.2 added user status - JB
 **/
public class User extends AbstractDocument {

	@Indexed(unique = true)
	private String username;
	@JsonIgnore
	private String password;
	private String firstName;
	private String lastName;

	private Integer workerId;

	private List<Permission> permissions;

	private String workingTimeModell;

	@DBRef
	private List<Desk> desks = new ArrayList<>();

	// constructor
	public User() {
		this.permissions = new ArrayList<>();
	}

	public User(String username, String firstname, String lastName, String password, String workingTimeModell, Permission... permissions) {
		this.username = username;
		this.password = password;
		this.firstName = firstname;
		this.lastName = lastName;
		this.workingTimeModell = workingTimeModell;
		this.permissions = Arrays.asList(permissions);
	}

	// getter and setter


	public String getWorkingTimeModell() {
		return workingTimeModell;
	}

	public void setWorkingTimeModell(String workingTimeModell) {
		this.workingTimeModell = workingTimeModell;
	}

	public Integer getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
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

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public List<Desk> getDesks() {
		return desks;
	}

	public void setDesks(List<Desk> desks) {
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

	@Override
	public String toString() {
		return "User{" +
				"Id: " +
				this.id +
				", Username: " +
				this.username +
				", Permissions: " +
				this.permissions +
				", Desks: " +
				this.desks +
				"}";
	}
}
