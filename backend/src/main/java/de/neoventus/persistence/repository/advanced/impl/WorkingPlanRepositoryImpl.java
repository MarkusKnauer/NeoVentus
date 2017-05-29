package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.Workingplan;
import de.neoventus.persistence.entity.Workingshift;
import de.neoventus.persistence.repository.advanced.NVWorkingPlanRepository;
import de.neoventus.rest.dto.WorkingPlanDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


/**
 * @author Julian Beck
 **/
@Repository
public class WorkingPlanRepositoryImpl implements NVWorkingPlanRepository {

	private MongoTemplate mongoTemplate;
	private final static Logger LOGGER = Logger.getLogger(WorkingPlanRepositoryImpl.class.getName());


	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}



	@Override
	public List<Workingplan> findAllUsersByPeriod(String date1, String date2) {
		List<Workingplan> wp = new ArrayList<>();
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

			Date beginDate= formatter.parse(date1);
			Date endDate= formatter.parse(date2);
			beginDate.setHours(0);
			beginDate.setMinutes(0);
			endDate.setHours(23);
			endDate.setMinutes(59);
			LOGGER.info("Date1: "+beginDate.toString());
			LOGGER.info("Date2: "+endDate.toString());
			Query query = new Query();
			query.addCriteria(Criteria.where("workingDay").gte(beginDate).lt(endDate));
			return  mongoTemplate.find(query,Workingplan.class);
		} catch (ParseException e) {

		}

		return null;
	}

	@Override
	public List<Workingshift> findAllUserbyPeriod(String Username, String date1, String date2) {
		List<Workingplan> wp = new ArrayList<>();
		List<Workingshift> ws = new ArrayList<>();

		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

			Date beginDate= formatter.parse(date1);
			Date endDate= formatter.parse(date2);
			beginDate.setHours(0);
			beginDate.setMinutes(0);
			endDate.setHours(23);
			endDate.setMinutes(59);
			LOGGER.info("Date1: "+beginDate.toString());
			LOGGER.info("Date2: "+endDate.toString());
			Query query = new Query();
			query.addCriteria(Criteria.where("workingDay").gte(beginDate).lt(endDate));
			wp = mongoTemplate.find(query,Workingplan.class);
			for(Workingplan plan : wp){
				for(Workingshift shift: plan.getWorkingshift()){
					if(shift.getUser().getUsername().equals(Username)){
						ws.add(shift);
					}
				}
			}
			return ws;
		} catch (ParseException e) {

		}
		return null;
	}



	@Override
	public void save(WorkingPlanDto dto) {

		Workingplan workingPlan;
		if (dto.getId() != null) {
			workingPlan = mongoTemplate.findById(dto.getId(), Workingplan.class);
		} else {
			workingPlan = new Workingplan();
		}
		workingPlan.setCreatedPlan(new Date(dto.getCreatedPlan()));
		workingPlan.setWorkingDay(new Date(dto.getWorkingDay()));

		// todo: Workingshift input

		mongoTemplate.save(workingPlan);
	}

}
