package de.neoventus.persistence.repository.advanced.impl;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
		Integer exp = this.orderItemRepository.countAllByWaiterId(id);

		int lvl1Exp = 50;
		double lvlIncreaseRate = 1.2;

		// formular for exp for level = lvl1Exp * lvlIncreaseRate ^ level

		// current level = floor( log of lvlIncreaseRate ( currentExp / lvl1Exp))
		// use log identity log b (n) = ln (n) / ln(b)
		int currentLevel = (int) Math.floor(Math.log(exp * 1D / lvl1Exp) / Math.log(lvlIncreaseRate)) + 1;
		int nextLevelExp = (int) (lvl1Exp * Math.pow(lvlIncreaseRate, currentLevel));
		int levelStartExp = (int) (lvl1Exp * Math.pow(lvlIncreaseRate, currentLevel - 1));
		Logger.getAnonymousLogger().info(currentLevel + "");

		profileDetails.setExp(exp);
		profileDetails.setExpNextLevel(nextLevelExp);
		profileDetails.setLevel(currentLevel);
		profileDetails.setExpLevelStart(currentLevel == 0 ? 0 : levelStartExp);

		return profileDetails;
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
