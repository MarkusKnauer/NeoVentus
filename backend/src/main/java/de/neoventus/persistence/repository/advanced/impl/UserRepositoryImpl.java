package de.neoventus.persistence.repository.advanced.impl;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import de.neoventus.persistence.entity.Billing;
import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.persistence.repository.advanced.NVUserRepository;
import de.neoventus.persistence.repository.advanced.impl.aggregation.UserProfileDetails;
import de.neoventus.rest.dto.UserDto;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author Dennis Thanner
 **/
public class UserRepositoryImpl implements NVUserRepository {

	private MongoTemplate mongoTemplate;

	private DeskRepository deskRepository;

	private OrderItemRepository orderItemRepository;


	@Override
	public void save(UserDto dto) {
		User u;
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		if (dto.getId() != null) {
			u = mongoTemplate.findById(dto.getId(), User.class);
		} else {
			u = new User();
		}

		u.setPassword(dto.getPassword() != null ? bCryptPasswordEncoder.encode(dto.getPassword()) : null);
		u.setUsername(dto.getUsername());
		u.setFirstName(dto.getFirstName());
		u.setLastName(dto.getLastName());

		// todo add permissions
//		u.getPermissions().clear();
//		for(Permission p : dto.getPermissions()) {
//
//		}

		u.getDesks().clear();
		for (String deskId : dto.getDesks()) {
			u.getDesks().add(deskRepository.findOne(deskId));
		}

		mongoTemplate.save(u);
	}

	/**
	 * get user profile details
	 *
	 * @param id
	 * @return
	 */
	@Override
	public UserProfileDetails getUserProfileDetails(String id) {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Date today = calendar.getTime();

		calendar.add(Calendar.DAY_OF_MONTH, 1);
		Date tomorrow = calendar.getTime();

		// create matching operation on waiter and date
		MatchOperation matchOperation = Aggregation.match(Criteria.where("waiter.$id").is(new ObjectId(id))
			.and("billedAt").gte(today).lte(tomorrow));

		Aggregation revAgg = Aggregation.newAggregation(
			matchOperation,
			Aggregation.unwind("items"),
			// first grouping to sum up sub document item prices for each billing, using first command to transport info into next grouping layer
			Aggregation.group("id").sum("items.price").as("menuCost")
				.first("totalPaid").as("totalPaid").first("waiter").as("waiter"),
			// second grouping to sum revenue and sum total menu cost to calc tips
			Aggregation.group("waiter").sum("totalPaid").as("revenueToday").sum("menuCost").as("menuCostToday"),
			Aggregation.project("revenueToday").andExclude("_id").and("revenueToday").minus("menuCostToday").as("tipsToday")
		);

		UserProfileDetails profileDetails = this.mongoTemplate.aggregate(revAgg, Billing.class, UserProfileDetails.class).getUniqueMappedResult();

		if (profileDetails == null) {
			profileDetails = new UserProfileDetails();
		}

		// get waiter level and experience points
		long exp = this.orderItemRepository.countAllByWaiterId(id);

		// 1 exp per billing with saldo less than 200
		long countBillings = this.mongoTemplate.count(Query.query(Criteria.where("waiter.$id").is(new ObjectId(id)).and("totalPaid").lt(200.0)), Billing.class);
		exp += countBillings;

		// 10 exp per billing with saldo gte 200
		long highPaidBillings = this.mongoTemplate.count(Query.query(Criteria.where("waiter.$id").is(new ObjectId(id)).and("totalPaid").gte(200.0)), Billing.class);
		exp += highPaidBillings * 10;

		int lvl1Exp = 50;
		double lvlIncreaseRate = 1.2;

		// formular for exp for level = lvl1Exp * lvlIncreaseRate ^ level

		// current level = floor( log of lvlIncreaseRate ( currentExp / lvl1Exp))
		// use log identity log b (n) = ln (n) / ln(b)
		int currentLevel = (int) Math.floor(Math.log(exp * 1D / lvl1Exp) / Math.log(lvlIncreaseRate)) + 1;
		if (currentLevel < 0) {
			currentLevel = 0;
		}
		int nextLevelExp = (int) (lvl1Exp * Math.pow(lvlIncreaseRate, currentLevel));
		int levelStartExp = (int) (lvl1Exp * Math.pow(lvlIncreaseRate, currentLevel - 1));

		profileDetails.setExp(exp);
		profileDetails.setExpNextLevel(nextLevelExp);
		profileDetails.setLevel(currentLevel);
		profileDetails.setExpLevelStart(currentLevel == 0 ? 0 : levelStartExp);

		return profileDetails;
	}

	@Override
	public Map<Long, Double> getLastWeekTips(String userId) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.DATE, -7);
		Logger.getAnonymousLogger().info(c.getTimeInMillis() + "");
		Aggregation tipAggregation = Aggregation.newAggregation(
			Aggregation.match(Criteria.where("waiter.$id").is(new ObjectId(userId)).and("billedAt").gte(c.getTime())),
			Aggregation.unwind("items"),
			Aggregation.group("id").sum("items.price").as("cost")
				.first("totalPaid").as("paid").first("waiter").as("waiter").first("billedAt").as("billedAt"),
			Aggregation.project("id", "cost", "paid", "waiter")
				.and("billedAt").extractDayOfMonth().as("day")
				.and("billedAt").extractYear().as("year")
				.and("billedAt").extractMonth().as("month"),
			Aggregation.group("day", "year", "month").sum("cost").as("totalCost").sum("paid").as("totalPaid"),
			Aggregation.project("_id").and("totalPaid").minus("totalCost").as("tip")
		);

		DBObject result = this.mongoTemplate.aggregate(tipAggregation, Billing.class, Billing.class).getRawResults();

		TreeMap<Long, Double> tips = new TreeMap<>();

		for (Object r : ((BasicDBList) result.get("result"))) {
			DBObject o = (DBObject) r;
			Map<String, Integer> dateGroup = ((DBObject) o.get("_id")).toMap();
			java.sql.Date d = new java.sql.Date(dateGroup.get("year") - 1900, dateGroup.get("month") - 1, dateGroup.get("day"));
			tips.put(d.getTime(), ((Double) o.get("tip")));
		}

		while (tips.size() < 7) {
			if (!tips.containsKey(c.getTime().getTime())) {
				tips.put(c.getTime().getTime(), 0.);
				Logger.getAnonymousLogger().info("Added date: " + c.getTime().toString());
			}
			c.add(Calendar.DATE, 1);
		}

		return tips;
	}

	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Autowired
	public void setDeskRepository(DeskRepository deskRepository) {
		this.deskRepository = deskRepository;
	}

	@Autowired
	public void setOrderItemRepository(OrderItemRepository orderItemRepository) {
		this.orderItemRepository = orderItemRepository;
	}
}
