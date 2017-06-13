package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.entity.OrderItemState;
import de.neoventus.persistence.entity.Reservation;
import de.neoventus.persistence.repository.advanced.NVDeskRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.DeskOverviewDetails;
import de.neoventus.rest.dto.DeskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
		LOGGER.info("desks: " + desks.size());

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

		// gets all open orders
		Query orderItemQuery = new Query();
		orderItemQuery.addCriteria(Criteria.where("states.state")
			.nin(Arrays.asList(OrderItemState.State.FINISHED, OrderItemState.State.CANCELED)));
		List<OrderItem> orders = this.mongoTemplate.find(orderItemQuery, OrderItem.class);
		LOGGER.info("orders: " + orders.size());

		Map<Integer, DeskOverviewDetails> deskOverview = new HashMap<>();

		// creates an detail object for each desk
		for (Desk desk : desks) {
			int deskNumber = desk.getNumber();
			DeskOverviewDetails details = new DeskOverviewDetails();
			details.setDeskNumber(deskNumber);
			details.setBeaconUUID(desk.getBeaconUUID());
			details.setBeaconMajor(desk.getBeaconMajor());
			details.setBeaconMinor(desk.getBeaconMinor());
			deskOverview.put(deskNumber, details);
		}

		for (OrderItem item : orders) {

			int orderDeskNumber = item.getDesk().getNumber();

			// adds each item price to the its desks totalPaid
			deskOverview.get(orderDeskNumber).setTotalPaid(
				deskOverview.get(orderDeskNumber).getTotalPaid()
					+ item.getItem().getPrice());

			// adds unique item waiters to desk
			deskOverview.get(orderDeskNumber).getWaiters().add(
				item.getWaiter().getUsername());
		}

		// add the next coming reservation for each table to detail object
		for (Reservation reservation : reservations) {

			int reservationDeskNumber = reservation.getDesk().getNumber();

			if (deskOverview.get(reservationDeskNumber).getNextReservation() == null ||
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
