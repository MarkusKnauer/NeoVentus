package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.repository.advanced.NVMenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author Julian Beck
 * @version 0.0.1
 **/
@Repository
public class MenuItemRepositoryImpl implements NVMenuItemRepository {

    private final static Logger LOGGER = Logger.getLogger(MenuItemRepositoryImpl.class.getName());

	private MongoTemplate mongoTemplate;

    // Default-Values for MenuItem
    @Override
    public void setDefaultMenu() {
        //MenuItem menu = Name, Price, Currency, Description, mediaURL, Notices
        LOGGER.info("Set Default Menu");
        MenuItem[] menu = {
        new MenuItem("kleiner Salat", 4.80, "EUR", "kalte Vorspeise", "", new ArrayList<>()),
        new MenuItem("Bärlauchcremesuppe mit Räucherlachs", 4.80, "EUR", "warme Vorspeise", "", new ArrayList<>()),
        new MenuItem("Lachsfilet", 14.60, "EUR","mit Tagliatelle und Tomaten", "", new ArrayList<>()),
        new MenuItem("Salatteller", 13.80, "EUR", "mit gebratenem Zanderfilet", "", new ArrayList<>()),
        new MenuItem("Pasta Bolognese", 11.90, "EUR", "mit Tomaten und Parmesan", "", new ArrayList<>()),
        new MenuItem("Schweinerückensteak", 13.90, "EUR", "mit Pfefferrahmsauce und Kartoffel Wedges", "", new ArrayList<>()),
        new MenuItem("Hausgemachte Kartoffel Gnocchi", 11.80, "EUR", "Gnocchi mit mediterranem Gemüse, Fetakäse, Rucola & Parmesan", "", new ArrayList<>()),
        new MenuItem("Hausgemachte Käsespätzle", 11.60, "EUR", "mit Beilagensalat", "", new ArrayList<>()),
        new MenuItem("Drei Kugeln Eis", 6.10, "EUR", "mit Sahne", "", new ArrayList<>()),
        new MenuItem("Spezi", 2.10, "EUR", "0,20l", "", new ArrayList<>()),
        new MenuItem("Ginger Ale", 2.20, "EUR", "0,20l", "", new ArrayList<>()),
        new MenuItem("Warsteiner", 2.00, "EUR", "0.25l", "", new ArrayList<>()),
        new MenuItem("Guinness", 3.00, "EUR", "0.30l", "", new ArrayList<>()),
        new MenuItem("Cabernet Sauvignon", 3.90, "EUR", "0.20l", "", new ArrayList<>()),
        new MenuItem("Pinot Grigio", 3.90, "EUR", "0.20l", "", new ArrayList<>()),
        new MenuItem("Jägermeister", 2.00, "EUR", "5cl", "", new ArrayList<>()),
        new MenuItem("Sambuca", 3.90, "EUR", "5cl", "", new ArrayList<>()),
        new MenuItem("Tasse Kaffee", 1.50, "EUR", "mit Milch, Zucker", "", new ArrayList<>()),
        new MenuItem("Heiße Schokolade", 2.10, "EUR", "mit Sahne", "", new ArrayList<>())
        };
		for (MenuItem m : menu) mongoTemplate.save(m);
	}

	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
}
