package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.MenuItemCategoryRepository;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.UserRepository;
import de.neoventus.persistence.repository.advanced.NVOrderItemRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.ObjectCountAggregation;
import de.neoventus.persistence.repository.advanced.impl.aggregation.OrderDeskAggregationDto;
import de.neoventus.rest.dto.OrderItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Update;

import java.util.*;
import java.util.logging.Logger;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author Julian Beck, Dennis Thanner
 **/
public class OrderItemRepositoryImpl implements NVOrderItemRepository {

	private MongoTemplate mongoTemplate;
	private MenuItemRepository menuItemRepository;
	private UserRepository userRepository;
	private MenuItemCategoryRepository menuItemCategoryRepository;

	@Override
	public void save(OrderItemDto dto) {
		OrderItem o;

		if (dto.getId() != null) {
			o = mongoTemplate.findById(dto.getId(), OrderItem.class);
		} else {
			o = new OrderItem();
		}
		o.setDesk(dto.getDeskNumber() != null ? this.mongoTemplate.findOne(query(where("number").is(dto.getDeskNumber())), Desk.class) : null);
		o.setItem(dto.getMenuItemNumber() != null ? menuItemRepository.findByNumber(dto.getMenuItemNumber()) : null);
		o.setWaiter(dto.getWaiter() != null ? userRepository.findByWorkerId(dto.getWaiter()) : null);
		o.setGuestWish(dto.getGuestWish() != null ? dto.getGuestWish() : "");

		for (String sideDishId : dto.getSideDishIds())
			o.getSideDishes().add(this.menuItemRepository.findOne(sideDishId));

		mongoTemplate.save(o);

	}

	@Override
	public List<OrderDeskAggregationDto> getGroupedNotPayedOrdersByItemForDesk(Desk... desk) {
		Aggregation agg = Aggregation.newAggregation(
			Aggregation.match(where("billing").is(null).and("desk").in(desk).and("states.state").ne(OrderItemState.State.CANCELED)),
			Aggregation.group("item", "sideDishes", "guestWish", "desk").count().as("count").first("waiter")
				.as("waiter").addToSet("$id").as("orderIds"),
			Aggregation.project("waiter", "count", "orderIds").and("_id.item").as("item").and("_id.sideDishes").as("sideDishes")
				.and("_id.guestWish").as("guestWish").and("_id.desk").as("desk")
		);

		AggregationResults<OrderDeskAggregationDto> aggR = this.mongoTemplate.aggregate(agg, OrderItem.class, OrderDeskAggregationDto.class);

		return aggR.getMappedResults();

	}

	@Override
	public Map<Integer, List<OrderDeskAggregationDto>> getUnfinishedOrdersForCategoriesGroupedByDeskAndOrderItem(boolean forKitchen) {
		long start = System.currentTimeMillis();
		List<Desk> desks = this.mongoTemplate.findAll(Desk.class);
		List<MenuItemCategory> categories = this.menuItemCategoryRepository.findByForKitchen(forKitchen);
		List<MenuItem> itemsInterested = this.menuItemRepository.findAllByMenuItemCategoryIn(categories);

		Map<Integer, List<OrderDeskAggregationDto>> result = new HashMap<>();

		GroupOperation group = Aggregation.group("item", "sideDishes", "guestWish", "desk").addToSet("$id").as("orderIds");

		ProjectionOperation projection = Aggregation.project("orderIds").and("_id.item").as("item")
			.and("_id.sideDishes").as("sideDishes")
			.and("_id.guestWish").as("guestWish")
			.and("_id.desk").as("desk");

		Aggregation agg = Aggregation.newAggregation(
			Aggregation.match(where("states.state")
				.nin(Arrays.asList(OrderItemState.State.CANCELED, OrderItemState.State.FINISHED))
				.and("item").in(itemsInterested).and("desk").in(desks)),
			group,
			projection
		);

		List<OrderDeskAggregationDto> dbResult = this.mongoTemplate.aggregate(agg, OrderItem.class, OrderDeskAggregationDto.class).getMappedResults();

		// group by desk
		for (OrderDeskAggregationDto dto : dbResult) {
			int number = dto.getDesk().getNumber();
			if (!result.containsKey(number)) {
				result.put(number, new ArrayList<>());
			}
			result.get(number).add(dto);
		}

		Logger.getAnonymousLogger().info("Kitchen/Bar Aggregation for Desks took: " + (System.currentTimeMillis() - start));
		return result;
	}

	@Override
	public List<OrderDeskAggregationDto> getUnfinishedOrderForCategoriesGroupedByItemOrderByCount(boolean forKitchen) {
		List<MenuItemCategory> categories = this.menuItemCategoryRepository.findByForKitchen(forKitchen);
		List<MenuItem> itemsInterested = this.menuItemRepository.findAllByMenuItemCategoryIn(categories);

		Aggregation agg = Aggregation.newAggregation(
			Aggregation.match(where("states.state")
				.nin(Arrays.asList(OrderItemState.State.CANCELED, OrderItemState.State.FINISHED))
				.and("item").in(itemsInterested)),
			Aggregation.group("item", "sideDishes", "guestWish").count().as("count"),
			Aggregation.project("count").and("_id.item").as("item").and("_id.sideDishes").as("sideDishes")
				.and("_id.guestWish").as("guestWish"),
			Aggregation.sort(Sort.Direction.DESC, "count")
		);

		AggregationResults<OrderDeskAggregationDto> aggR = this.mongoTemplate.aggregate(agg, OrderItem.class, OrderDeskAggregationDto.class);

		return aggR.getMappedResults();
	}

	@Override
	public List<ObjectCountAggregation> getTop10OrderedMenuItems(boolean forKitchen) {
		List<MenuItem> menuItems = this.menuItemRepository.findAllByMenuItemCategoryIn(this.menuItemCategoryRepository.findByForKitchen(forKitchen));
		Calendar c = new GregorianCalendar();
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);

		Aggregation agg = Aggregation.newAggregation(
			Aggregation.match(where("states.0.date").gte(c.getTime()).and("states.state").ne(OrderItemState.State.CANCELED)
				.and("item").in(menuItems)),
			Aggregation.group("item").count().as("count"),
			Aggregation.sort(Sort.Direction.DESC, "count"),
			Aggregation.limit(10),
			Aggregation.project("count").and("object").previousOperation()
		);


		return this.mongoTemplate.aggregate(agg, OrderItem.class, ObjectCountAggregation.class).getMappedResults();
	}

	@Override
	public List<Map<String, Object>> getMenuCategoryDistribution() {
		List<MenuItemCategory> categories = this.menuItemCategoryRepository.getRootElements();

		Calendar c = new GregorianCalendar();
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);

		Aggregation agg = Aggregation.newAggregation(
			Aggregation.match(where("states.0.date").gte(c.getTime()).and("states.state").ne(OrderItemState.State.CANCELED)),
			Aggregation.group("item").count().as("count"),
			Aggregation.sort(Sort.Direction.DESC, "count"),
			Aggregation.project("count").and("object").previousOperation()
		);

		List<ObjectCountAggregation> aggResult = this.mongoTemplate.aggregate(agg, OrderItem.class, ObjectCountAggregation.class).getMappedResults();

		Map<String, Map<String, Object>> result = new HashMap<>();
		for (MenuItemCategory cat : categories) {
			Map<String, Object> map = new HashMap<>();
			map.put("category", cat.getName());
			map.put("count", 0);
			result.put(cat.getId(), map);
		}

		for (ObjectCountAggregation<MenuItem> itemAgg : aggResult) {
			MenuItemCategory itemRootCategory = itemAgg.getObject().getMenuItemCategory();
			while (itemRootCategory.getParent() != null) {
				itemRootCategory = itemRootCategory.getParent();
			}

			Map<String, Object> catResult = result.get(itemRootCategory.getId());
			catResult.put("count", itemAgg.getCount() + (int) catResult.get("count"));
		}

		return new ArrayList<>(result.values());
	}


	/**
	 * change desk of orders
	 *
	 * @param deskId
	 * @param orderIds
	 */
	@Override
	public void changeDeskForOrders(String deskId, String... orderIds) {
		Desk d = this.mongoTemplate.findOne(query(where("id").is(deskId)), Desk.class);
		this.mongoTemplate.updateMulti(query(where("id").in(orderIds)), Update.update("desk", d), OrderItem.class);
	}

	//Setter

	@Autowired
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
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
	public void setMenuItemCategoryRepository(MenuItemCategoryRepository menuItemCategoryRepository) {
		this.menuItemCategoryRepository = menuItemCategoryRepository;
	}
}
