package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;
import de.neoventus.persistence.repository.advanced.NVOrderItemRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.OrderDeskAggregationDto;
import de.neoventus.rest.dto.OrderItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Julian Beck, Dennis Thanner
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
	private MenuItemCategoryRepository menuItemCategoryRepository;

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

		for (String sideDishId : dto.getSideDishIds())
			o.getSideDishes().add(this.menuItemRepository.findOne(sideDishId));

		mongoTemplate.save(o);

	}

	@Override
	public List<OrderDeskAggregationDto> getGroupedNotPayedOrdersByItemForDesk(Desk desk) {
		Aggregation agg = Aggregation.newAggregation(
			Aggregation.match(Criteria.where("billing").is(null).and("desk").is(desk).and("states.state").ne(OrderItemState.State.CANCELED)),
			Aggregation.group("item", "sideDishes").count().as("count").first("waiter")
				.as("waiter").addToSet("$id").as("orderIds"),
			Aggregation.project("waiter", "count", "orderIds").and("_id.item").as("item").and("_id.sideDishes").as("sideDishes")
		);

		AggregationResults<OrderDeskAggregationDto> aggR = this.mongoTemplate.aggregate(agg, OrderItem.class, OrderDeskAggregationDto.class);

		return aggR.getMappedResults();

	}

	@Override
	public Map<Integer, List<OrderDeskAggregationDto>> getUnfinishedOrdersForCategoriesGroupedByDeskAndOrderItem(boolean forKitchen) {
		List<Desk> desks = this.deskRepository.findAll();
		List<MenuItemCategory> categories = this.menuItemCategoryRepository.findByForKitchen(forKitchen);
		List<MenuItem> itemsInterested = this.menuItemRepository.findAllByMenuItemCategoryIn(categories);

		Map<Integer, List<OrderDeskAggregationDto>> result = new HashMap<>();

		GroupOperation group = Aggregation.group("item", "sideDishes", "guestWish").addToSet("$id").as("orderIds");

		ProjectionOperation projection = Aggregation.project("orderIds").and("_id.item").as("item")
			.and("_id.sideDishes").as("sideDishes")
			.and("_id.guestWish").as("guestWish");

		for (Desk d : desks) {

			Aggregation agg = Aggregation.newAggregation(
				Aggregation.match(Criteria.where("billing").is(null).and("states.state")
					.nin(Arrays.asList(OrderItemState.State.CANCELED, OrderItemState.State.FINISHED))
					.and("item").in(itemsInterested).and("desk").is(d)),
				group,
				projection
			);

			AggregationResults<OrderDeskAggregationDto> aggR = this.mongoTemplate.aggregate(agg, OrderItem.class, OrderDeskAggregationDto.class);

			result.put(d.getNumber(), aggR.getMappedResults());

		}

		return result;
	}

	@Override
	public List<OrderDeskAggregationDto> getUnfinishedOrderForCategoriesGroupedByItem(boolean forKitchen) {
		List<MenuItemCategory> categories = this.menuItemCategoryRepository.findByForKitchen(forKitchen);
		List<MenuItem> itemsInterested = this.menuItemRepository.findAllByMenuItemCategoryIn(categories);

		Aggregation agg = Aggregation.newAggregation(
			Aggregation.match(Criteria.where("billing").is(null).and("states.state")
				.nin(Arrays.asList(OrderItemState.State.CANCELED, OrderItemState.State.FINISHED))
				.and("item").in(itemsInterested)),
			Aggregation.group("item", "sideDishes", "guestWish").count().as("count"),
			Aggregation.project("count").and("_id.item").as("item").and("_id.sideDishes").as("sideDishes")
				.and("_id.guestWish").as("guestWish")
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

	@Autowired
	public void setMenuItemCategoryRepository(MenuItemCategoryRepository menuItemCategoryRepository) {
		this.menuItemCategoryRepository = menuItemCategoryRepository;
	}
}
