package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.persistence.repository.advanced.NVDeskRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.DeskOrderAggregation;
import de.neoventus.persistence.repository.advanced.impl.aggregation.DeskOverviewDetails;
import de.neoventus.rest.dto.DeskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
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

	private OrderItemRepository orderItemRepository;

	@Autowired
	public void setOrderItemRepository(OrderItemRepository orderItemRepository) {
		this.orderItemRepository = orderItemRepository;
	}

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
		Calendar midnight = new GregorianCalendar();
		midnight.set(Calendar.HOUR_OF_DAY, 0);
		midnight.set(Calendar.MINUTE, 0);
		midnight.set(Calendar.SECOND, 0);
		midnight.set(Calendar.MILLISECOND, 0);
		midnight.add(Calendar.DAY_OF_MONTH, 1); // next day
		Date now = new Date();
		Query reservationQuery = new Query();
		reservationQuery.addCriteria(Criteria.where("time").gte(now).lt(midnight.getTime()));
		List<Reservation> reservations = this.mongoTemplate.find(reservationQuery, Reservation.class);
		LOGGER.info("reservations: " + reservations.size());

		Map<Integer, DeskOverviewDetails> deskOverview = new HashMap<>();
		// creates an detail object for each desk
		for (Desk desk : desks) {
			DeskOverviewDetails overviewDetails = new DeskOverviewDetails();
			overviewDetails.setDeskNumber(desk.getNumber());
			deskOverview.put(desk.getNumber(), overviewDetails);
		}

		Aggregation deskorderAggregation = Aggregation.newAggregation(
			Aggregation.match(Criteria.where("billing").is(null).and("states.state").ne(OrderItemState.State.CANCELED)),
			Aggregation.group("id").push("item").as("items").first("waiter").as("waiter")
				.first("desk").as("desk").first("sideDishes").as("sideDishes"),
			Aggregation.project("waiter", "desk").and("items").concatArrays("items", "sideDishes"),
			Aggregation.unwind("items"),
			Aggregation.group("desk").push("items").as("items").push("waiter").as("waiters"),
			Aggregation.project("items", "waiters").and("desk").previousOperation()
		);
		List<DeskOrderAggregation> deskOrderAggregations = this.mongoTemplate
			.aggregate(deskorderAggregation, OrderItem.class, DeskOrderAggregation.class).getMappedResults();

		for (DeskOrderAggregation deskOrder : deskOrderAggregations) {

			double sum = 0;
			for (MenuItem m : deskOrder.getItems()) {
				sum += m.getPrice();
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

			int reservationDeskNumber = reservation.getDesk().getNumber();

			if (deskOverview.get(reservationDeskNumber) == null ||
				deskOverview.get(reservationDeskNumber).getNextReservation().after(reservation.getTime())) {
				deskOverview.get(reservationDeskNumber).setNextReservation(reservation.getTime());
			}
		}

		return new ArrayList<>(deskOverview.values());
	}

	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

}
