package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.advanced.NVDeskRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.DeskOverviewDetails;
import de.neoventus.rest.dto.DeskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

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

//		// gets all desks
//		List<Desk> desks = this.mongoTemplate.findAll(Desk.class);
//		LOGGER.info("desks: " + desks.size());
//
//		// gets all reservations between now and next day
//		Calendar midnight = new GregorianCalendar();
//		midnight.set(Calendar.HOUR_OF_DAY, 0);
//		midnight.set(Calendar.MINUTE, 0);
//		midnight.set(Calendar.SECOND, 0);
//		midnight.set(Calendar.MILLISECOND, 0);
//		midnight.add(Calendar.DAY_OF_MONTH, 1); // next day
//		Date now = new Date();
//		Query reservationQuery = new Query();
//		reservationQuery.addCriteria(Criteria.where("time").gte(now).lt(midnight.getTime()));
//		List<Reservation> reservations = this.mongoTemplate.find(reservationQuery, Reservation.class);
//		LOGGER.info("reservations: " + reservations.size());
//
//		// gets all open orders
//		Query orderItemQuery = new Query();
//		orderItemQuery.addCriteria(Criteria.where("states.state")
//			.nin(Arrays.asList(OrderItemState.State.FINISHED, OrderItemState.State.CANCELED)));
//		List<OrderItem> orders = this.mongoTemplate.find(orderItemQuery, OrderItem.class);
//		LOGGER.info("orders: " + orders.size());

		Aggregation aggregation = newAggregation(
//			match(Criteria.where("states.state")
//				.nin(Arrays.asList(OrderItemState.State.FINISHED, OrderItemState.State.CANCELED))),
			Aggregation.unwind("billing"), Aggregation.unwind("desk"),
			group("desk")
				.push("desk.number").as("number")
				.sum("billing.totalPaid").as("totalPaid"),
			project("desk.number", "totalPaid"));

		AggregationResults<DeskOverviewDetails> results =
			this.mongoTemplate.aggregate(
				aggregation, OrderItem.class, DeskOverviewDetails.class);

//		DBObject obj = results.getRawResults();

		return results.getMappedResults();

	}

	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

}
