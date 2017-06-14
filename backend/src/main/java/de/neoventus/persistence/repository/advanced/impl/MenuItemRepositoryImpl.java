package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.entity.OrderItemState;
import de.neoventus.persistence.repository.MenuItemCategoryRepository;
import de.neoventus.persistence.repository.SideDishRepository;
import de.neoventus.persistence.repository.advanced.NVMenuItemRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.MenuItemProcessingDetails;
import de.neoventus.persistence.repository.advanced.impl.aggregation.ObjectCountAggregation;
import de.neoventus.rest.dto.MenuDto;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author Julian Beck, Markus Knauer, Dennis Thanner
 **/
public class MenuItemRepositoryImpl implements NVMenuItemRepository {

	private final static Logger LOGGER = Logger.getLogger(MenuItemRepositoryImpl.class.getName());

	private MongoTemplate mongoTemplate;

	@Autowired
	private MenuItemCategoryRepository menuItemCategoryRepository;

	@Autowired
	private SideDishRepository sideDishRepository;


	@Override
	public void save(MenuDto dto) {
		MenuItem item;
		if (dto.getId() != null) {
			item = mongoTemplate.findById(dto.getId(), MenuItem.class);
		} else {
			item = new MenuItem();
		}
		item.setMenuItemCategory(dto.getCategory() != null ? menuItemCategoryRepository.findByName(dto.getCategory()) : null);
		item.setCurrency(dto.getCurrency());
		item.setDescription(dto.getDescription());
		item.setMediaUrl(dto.getMediaUrl());
		item.setName(dto.getName());
		item.setNotices(dto.getNotices());
		item.setNumber(dto.getNumber());
		item.setPrice(dto.getPrice());

		item.setSideDishGroup(dto.getSideDish() != null ? sideDishRepository.findByName(dto.getSideDish()) : null);


		mongoTemplate.save(item);
	}

	@Override
	public List<ObjectCountAggregation> getPopularGuestWishesForItem(String id) {
		Aggregation agg = Aggregation.newAggregation(
			Aggregation.match(Criteria.where("item.$id").is(new ObjectId(id)).and("guestWish").ne("")),
			Aggregation.group("guestWish").count().as("count"),
			Aggregation.sort(Sort.Direction.DESC, "count"),
			Aggregation.project("count").and("object").previousOperation(),
			Aggregation.limit(5)
		);

		return this.mongoTemplate.aggregate(agg, OrderItem.class, ObjectCountAggregation.class).getMappedResults();
	}

	@Override
	public MenuItemProcessingDetails getProcessingDetails(String id) {
		// todo limit orders from last 3 months
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.MONTH, -3);

		Date months3Before = calendar.getTime();

		Aggregation agg = Aggregation.newAggregation(
			Aggregation.match(Criteria.where("item.$id").is(new ObjectId(id)).and("states.0.date").gte(months3Before)),
			Aggregation.unwind("states"),
			Aggregation.match(Criteria.where("states.state").in(Arrays.asList(OrderItemState.State.NEW, OrderItemState.State.FINISHED))),
			Aggregation.sort(Sort.Direction.ASC, "states.date"),
			Aggregation.group("id").first("states.date").as("startTime").last("states.date").as("finishTime"),
			Aggregation.project("id").and("finishTime").minus("startTime").as("time"),
			Aggregation.group("id").avg("time").as("time"),
			Aggregation.project().and("time").divide(1000).as("timeSec")
		);

		return this.mongoTemplate.aggregate(agg, OrderItem.class, MenuItemProcessingDetails.class).getUniqueMappedResult();
	}

	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
}
