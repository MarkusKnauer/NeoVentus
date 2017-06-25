package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.Billing;
import de.neoventus.persistence.entity.BillingItem;
import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.persistence.repository.UserRepository;
import de.neoventus.persistence.repository.advanced.NVBillingRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.ObjectRevenueAggregation;
import de.neoventus.rest.dto.BillingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;


/**
 * @author Julian Beck, Dennis Thanner
 **/
@Repository
public class BillingRepositoryImpl implements NVBillingRepository {

	private MongoTemplate mongoTemplate;

	private OrderItemRepository orderItemRepository;

	private UserRepository userRepository;

	@Autowired
	public BillingRepositoryImpl(MongoTemplate mongoTemplate, OrderItemRepository orderItemRepository, UserRepository userRepository) {
		this.mongoTemplate = mongoTemplate;
		this.orderItemRepository = orderItemRepository;
		this.userRepository = userRepository;
	}

	@Override
	public void save(BillingDto dto) {

		Billing billing;
		if (dto.getId() != null) {
			billing = mongoTemplate.findById(dto.getId(), Billing.class);
		} else {
			billing = new Billing();
		}

		billing.setTotalPaid(dto.getTotalPaid());

		if (dto.getWaiter() != null)
			billing.setWaiter(this.userRepository.findOne(dto.getWaiter()));

		billing.getItems().clear();
		for (String orderId : dto.getItems()) {
			OrderItem order = this.orderItemRepository.findOne(orderId);
			int sideDishesPrice = 0;
			for (MenuItem sideDish : order.getSideDishes()) {
				sideDishesPrice += sideDish.getPrice();
			}

			billing.getItems().add(new BillingItem(
				order,
				order.getItem().getPrice() + sideDishesPrice
			));
		}


		mongoTemplate.save(billing);
	}

	@Override
	public List<ObjectRevenueAggregation> getQuartersRevenue() {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, Calendar.JANUARY);

		Aggregation agg = Aggregation.newAggregation(
			Aggregation.match(Criteria.where("billedAt").gte(c.getTime())),
			Aggregation.project("totalPaid").and("billedAt").extractMonth().as("month"),
			Aggregation.project("totalPaid").and("month").divide(3).as("quarter"),
			Aggregation.project("totalPaid").and("quarter").ceil().as("quarter"),
			Aggregation.group("quarter").sum("totalPaid").as("revenue"),
			Aggregation.project("revenue").and("object").previousOperation(),
			Aggregation.sort(Sort.Direction.ASC, "object")
		);

		return this.mongoTemplate.aggregate(agg, Billing.class, ObjectRevenueAggregation.class).getMappedResults();
	}

	@Override
	public List<ObjectRevenueAggregation> getTop10Waiters() {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);

		Aggregation agg = Aggregation.newAggregation(
			Aggregation.match(where("billedAt").gte(c.getTime())),
			Aggregation.group("waiter").sum("totalPaid").as("revenue"),
			Aggregation.sort(Sort.Direction.DESC, "revenue"),
			Aggregation.limit(10),
			Aggregation.project("revenue").and("object").previousOperation()
		);

		return this.mongoTemplate.aggregate(agg, Billing.class, ObjectRevenueAggregation.class).getMappedResults();
	}


}
