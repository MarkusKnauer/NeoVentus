package de.neoventus.init;

import de.neoventus.persistence.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

/**
 * initialize the demo data for the project
 *
 * @author Dennis Thanner, Julian Beck, Markus Knauer, Tim Heidelbach
 * @version 0.0.7 deleted init on every start, added drop indexes method - DT
 * 			0.0.6 Using constructor injection - TH
 *          0.0.5 Refactor default Data in separate Class - JB
 *          0.0.4 added permissions as enum - MK
 *          0.0.3 user status clean up - DT
 *          0.0.2 added users - JB
 */
@Component
@Profile("default")
public class RestaurantDemoInit {

    private final static Logger LOGGER = Logger.getLogger(RestaurantDemoInit.class.getName());
    private final DeskRepository deskRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuItemRepository menuItemRepository;
    private final ReservationRepository reservationRepository;
    private final BillingRepository billingRepository;
    private final MenuItemCategoryRepository menuItemCategoryRepository;
    private final SideDishRepository sideDishRepository;
	private final MongoTemplate mongoTemplate;

    @Autowired
    public RestaurantDemoInit(DeskRepository deskRepository, UserRepository userRepository,
							  OrderItemRepository orderItemRepository, MenuItemRepository menuItemRepository, MenuItemCategoryRepository menuItemCategoryRepository,
							  ReservationRepository reservationRepository, BillingRepository billingRepository, SideDishRepository sideDishRepository,
							  MongoTemplate mongoTemplate) {

        this.billingRepository = billingRepository;
        this.reservationRepository = reservationRepository;
        this.deskRepository = deskRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.menuItemRepository = menuItemRepository;
        this.menuItemCategoryRepository = menuItemCategoryRepository;
        this.sideDishRepository =sideDishRepository;
		this.mongoTemplate = mongoTemplate;
	}

    /**
     * generate data method executed in application startup
     *
     * @see PostConstruct
     */
//    @PostConstruct
	public void initialize() {
        clearData();
        new DefaultDemoDataIntoDB(deskRepository, userRepository, menuItemRepository, menuItemCategoryRepository, orderItemRepository, reservationRepository,billingRepository,sideDishRepository);
	}

    /**
     * clear before regenerate to allow changes
     */
    private void clearData() {
        deskRepository.deleteAll();
        menuItemRepository.deleteAll();
        userRepository.deleteAll();
        orderItemRepository.deleteAll();
        reservationRepository.deleteAll();
        menuItemCategoryRepository.deleteAll();
        sideDishRepository.deleteAll();

    }

	/**
	 * clear existing indexes
	 */
	private void clearIndexes() {
		for (String collection : this.mongoTemplate.getDb().getCollectionNames()) {
			this.mongoTemplate.getDb().getCollection(collection).dropIndexes();
		}
	}
}
