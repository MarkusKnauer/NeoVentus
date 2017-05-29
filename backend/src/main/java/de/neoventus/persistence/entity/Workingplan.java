package de.neoventus.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Workingplan extends AbstractDocument {

	private Date createdPlan;

	private Date workingDay;

	private List<Workingshift> workingshift;


	public void addShift(Workingshift workingshift){
		if(this.workingshift == null) this.workingshift = new ArrayList<Workingshift>();
		this.workingshift.add(workingshift);
	}



	// constructor


	public Workingplan() {
	}

	public Workingplan(Date createdPlan, Date workingDay, List<Workingshift> workingshift) {
		this.createdPlan = createdPlan;
		this.workingDay = workingDay;
		this.workingshift = workingshift;
	}

	public Date getCreatedPlan() {
		return createdPlan;
	}

	public void setCreatedPlan(Date createdPlan) {
		this.createdPlan = createdPlan;
	}

	public Date getWorkingDay() {
		return workingDay;
	}

	public void setWorkingDay(Date workingDay) {
		this.workingDay = workingDay;
	}

	public List<Workingshift> getWorkingshift() {
		return workingshift;
	}

	public void setWorkingshift(List<Workingshift> workingshift) {
		this.workingshift = workingshift;
	}


	@Override
	public String toString() {
		return "Workingplan{" +
			"createdPlan=" + createdPlan +
			", workingDay=" + workingDay +
			", workingshift=" + workingshift +
			'}';
	}
}
