package de.neoventus.persistence.repository.advanced.impl;

import com.mongodb.BasicDBObject;
import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.advanced.NVDeskRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.DeskOrderAggregation;
import de.neoventus.persistence.repository.advanced.impl.aggregation.DeskOverviewDetails;
import de.neoventus.persistence.repository.advanced.impl.aggregation.ObjectCountAggregation;
import de.neoventus.rest.dto.DeskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;
import java.util.logging.Logger;


/**
 * @author Julian Beck, Dominik Streif
 **/
public class DeskRepositoryImpl implements NVDeskRepository {

	private static final Logger LOGGER = Logger.getLogger(DeskRepositoryImpl.class.getName());

	private MongoTemplate mongoTemplate;

	@Autowired
	public DeskRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void save(DeskDto dto) {

		Desk d;
		if (dto.getId() != null) {
			d = mongoTemplate.findById(dto.getId(), Desk.class);
		} else {
			d = new Desk();
		}

		d.setSeats(dto.getSeats());

		mongoTemplate.save(d);

	}

	@Override
	public List<DeskOverviewDetails> getDesksWithDetails() {

		// gets all desks
		List<Desk> desks = this.mongoTemplate.findAll(Desk.class);
//		LOGGER.info("desks: " + desks.size());

		// gets all reservations between now and next day
		Date now = new Date();
		Query reservationQuery = new Query();
		Aggregation reservatiuonAggregation = Aggregation.newAggregation(
			Aggregation.match(Criteria.where("time").gt(now)),
			Aggregation.sort(Sort.Direction.ASC, "time"),
			Aggregation.group("desk").first("$$ROOT").as("document"),
			// project only used field from first document
			Aggregation.project().and("document.time").as("time").and("document.desk").as("desk")
		);
		List<Reservation> reservations = this.mongoTemplate.aggregate(reservatiuonAggregation, Reservation.class,
			Reservation.class).getMappedResults();
		LOGGER.info("reservations: " + reservations.size());

		Map<Integer, DeskOverviewDetails> deskOverview = new HashMap<>();
		// creates an detail object for each desk
		for (Desk desk : desks) {
			DeskOverviewDetails overviewDetails = new DeskOverviewDetails();
			overviewDetails.setDeskNumber(desk.getNumber());
			overviewDetails.setBeaconMajor(desk.getBeaconMajor());
			overviewDetails.setBeaconMinor(desk.getBeaconMinor());
			overviewDetails.setBeaconUUID(desk.getBeaconUUID());
			deskOverview.put(desk.getNumber(), overviewDetails);
		}

		Map<String, String> orderPush = new HashMap<>();
		orderPush.put("object", "$_id.items");
		orderPush.put("count", "$count");

		Aggregation deskorderAggregation = Aggregation.newAggregation(
			Aggregation.match(Criteria.where("billing").is(null).and("states.state").ne(OrderItemState.State.CANCELED)),
			Aggregation.group("id").push("item").as("items").first("waiter").as("waiter")
				.first("desk").as("desk").first("sideDishes").as("sideDishes"),
			Aggregation.project("waiter", "desk").and("items").concatArrays("items", "sideDishes"),
			Aggregation.unwind("items"),
			Aggregation.group("desk", "items").count().as("count").addToSet("waiter").as("waiters"),
			Aggregation.unwind("waiters"),
			Aggregation.group("_id.desk").addToSet(new BasicDBObject(orderPush)).as("items").addToSet("waiters").as("waiters"),
			Aggregation.project("items", "waiters").and("desk").previousOperation()
		);
		AggregationResults<DeskOrderAggregation> aggResult = this.mongoTemplate
			.aggregate(deskorderAggregation, OrderItem.class, DeskOrderAggregation.class);
		List<DeskOrderAggregation> deskOrderResults = aggResult.getMappedResults();

		for (DeskOrderAggregation deskOrder : deskOrderResults) {

			double sum = 0;
			for (ObjectCountAggregation<MenuItem> orderCount : deskOrder.getItems()) {
				// BUG IN SPRING COUNTING COUNTS DOUBLE -->  so we have to divide by 2
				sum += orderCount.getObject().getPrice() * orderCount.getCount() / 2;
			}

			Set<String> waiter = new HashSet<>();
			for (User u : deskOrder.getWaiters()) {
				waiter.add(u.getUsername());
			}

			deskOverview.get(deskOrder.getDesk().getNumber()).setTotalPaid(sum);
			deskOverview.get(deskOrder.getDesk().getNumber()).setWaiters(waiter);
		}

		// add the next coming reservation for each table to detail object
		for (Reservation reservation : reservations) {
			deskOverview.get(reservation.getDesk().getNumber()).setNextReservation(reservation.getTime());
		}

		return new ArrayList<>(deskOverview.values());
	}

	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

}
