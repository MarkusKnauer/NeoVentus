package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.entity.OrderItemState;
import de.neoventus.persistence.repository.*;
import de.neoventus.persistence.repository.advanced.NVOrderItemRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.OrderDeskAggregationDto;
import de.neoventus.rest.dto.OrderItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

/**
 * @author Julian Beck, Dennis Thanner
 * @version 0.0.6 deleted searchOrderItemOutpuDto - DT
 *          0.0.5 orderState refactoring - DT
 *          0.0.4 state with enum - DS
 *          0.0.3 redundancy clean up - DT
 *          0.0.2 added variable state - DS
 *          0.0.1
 **/
public class OrderItemRepositoryImpl implements NVOrderItemRepository {

	@Autowired
	private OrderItemRepository orderItemRepository;

	private MongoTemplate mongoTemplate;
	private DeskRepository deskRepository;
	private MenuItemRepository menuItemRepository;
	private UserRepository userRepository;
	private ReservationRepository reservationRepository;
	private BillingRepository billingRepository;

	@Override
	public void save(OrderItemDto dto) {
		OrderItem o;

		if (dto.getId() != null) {
			o = mongoTemplate.findById(dto.getId(), OrderItem.class);
		} else {
			o = new OrderItem();
		}
		o.setDesk(dto.getDeskNumber() != null ? deskRepository.findByNumber(dto.getDeskNumber()) : null);
		o.setItem(dto.getMenuItemNumber() != null ? menuItemRepository.findByNumber(dto.getMenuItemNumber()) : null);
		o.setWaiter(dto.getWaiter() != null ? userRepository.findByWorkerId(dto.getWaiter()) : null);
		o.setGuestWish(dto.getGuestWish() != null ? dto.getGuestWish() : "");

		mongoTemplate.save(o);

	}

	@Override
	public List<OrderDeskAggregationDto> getGroupedNotPayedOrdersByItemForDesk(Desk desk) {
		Aggregation agg = Aggregation.newAggregation(
			Aggregation.match(Criteria.where("billing").is(null).and("desk").is(desk).and("states.state").ne(OrderItemState.State.CANCELED)),
			Aggregation.group("item").count().as("count").first("waiter").as("waiter"),
			Aggregation.project("waiter", "count").and("item").previousOperation()
		);

		AggregationResults<OrderDeskAggregationDto> aggR = this.mongoTemplate.aggregate(agg, OrderItem.class, OrderDeskAggregationDto.class);

		return aggR.getMappedResults();

	}

	//Setter

	@Autowired
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Autowired
	public void setDeskRepository(DeskRepository deskRepository) {
		this.deskRepository = deskRepository;
	}

	@Autowired
	public void setMenuItemRepository(MenuItemRepository menuItemRepository) {
		this.menuItemRepository = menuItemRepository;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	public void setReservationRepository(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Autowired
	public void setBillingRepository(BillingRepository billingRepository) {
		this.billingRepository = billingRepository;
	}
	// Next  Respo Lager (not the Beer)
}
