package de.neoventus.persistence.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Workingshift {

	private Date startShift;

	private Date endShift;



    @DBRef
    private User user;

    @DBRef
    private List<Desk> deskList;

	public Workingshift() {
	}

	public Workingshift(User user, List<Desk> deskList) {
		this.user = user;
		this.deskList = deskList;
	}


	public Workingshift(Date startShift, Date endShift, User user, List<Desk> deskList) {
		this.startShift = startShift;
		this.endShift = endShift;
		this.user = user;
		this.deskList = deskList;
	}

	public void addDesk(Desk desk){
		if (deskList == null){
			deskList = new ArrayList<Desk>();
		}
		deskList.add(desk);
	}


	public Date getStartShift() {
		return startShift;
	}

	public void setStartShift(Date startShift) {
		this.startShift = startShift;
	}

	public Date getEndShift() {
		return endShift;
	}

	public void setEndShift(Date endShift) {
		this.endShift = endShift;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Desk> getDeskList() {
		return deskList;
	}

	public void setDeskList(List<Desk> deskList) {
		this.deskList = deskList;
	}

	@Override
	public String toString() {
		return "Workingshift{" +
			"user=" + user +
			", deskList=" + deskList +
			'}';
	}
}
