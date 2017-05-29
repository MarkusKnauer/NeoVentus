package de.neoventus.rest.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Julian Beck
 * @version 0.0.1
 */
public class WorkingPlanDto implements Serializable {

	private String id;

	private String createdPlan;

	private String workingDay;

	private List<String> workingshift;

	public WorkingPlanDto() {
	}

	public WorkingPlanDto(String createdPlan, String workingDay, List<String> workingshift) {
		this.createdPlan = createdPlan;
		this.workingDay = workingDay;
		this.workingshift = workingshift;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedPlan() {
		return createdPlan;
	}

	public void setCreatedPlan(String createdPlan) {
		this.createdPlan = createdPlan;
	}

	public String getWorkingDay() {
		return workingDay;
	}

	public void setWorkingDay(String workingDay) {
		this.workingDay = workingDay;
	}

	public List<String> getWorkingshift() {
		return workingshift;
	}

	public void setWorkingshift(List<String> workingshift) {
		this.workingshift = workingshift;
	}
}
