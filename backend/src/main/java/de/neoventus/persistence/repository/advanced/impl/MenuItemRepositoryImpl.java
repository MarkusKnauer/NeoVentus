package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.MenuItemCategory;
import de.neoventus.persistence.repository.advanced.NVMenuItemRepository;
import de.neoventus.rest.dto.MenuDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author Julian Beck, Markus Knauer, Dennis Thanner
 * @version 0.0.3 added null support for menu category, redundancy clean up - DT
 * 			0.0.2 Insert Method save(MenuDto) MK
 * 			0.0.1 created JB
 **/
public class MenuItemRepositoryImpl implements NVMenuItemRepository {

    private final static Logger LOGGER = Logger.getLogger(MenuItemRepositoryImpl.class.getName());

	private MongoTemplate mongoTemplate;

    // Default-Values for MenuItem
    @Override
    public void setDefaultMenu() {
        //MenuItem menu = Name, Price, Currency, Description, mediaURL, Notices
        LOGGER.info("Set Default Menu");
        MenuItem[] menu = {
        new MenuItem(MenuItemCategory.APPETIZER,"kleiner Salat", 4.80, "EUR", "kalte Vorspeise", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.APPETIZER,"Bärlauchcremesuppe mit Räucherlachs", 4.80, "EUR", "warme Vorspeise", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.MAIN_COURSE,"Lachsfilet", 14.60, "EUR","mit Tagliatelle und Tomaten", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.MAIN_COURSE,"Salatteller", 13.80, "EUR", "mit gebratenem Zanderfilet", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.MAIN_COURSE,"Pasta Bolognese", 11.90, "EUR", "mit Tomaten und Parmesan", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.MAIN_COURSE,"Schweinerückensteak", 13.90, "EUR", "mit Pfefferrahmsauce und Kartoffel Wedges", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.MAIN_COURSE,"Hausgemachte Kartoffel Gnocchi", 11.80, "EUR", "Gnocchi mit mediterranem Gemüse, Fetakäse, Rucola & Parmesan", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.MAIN_COURSE,"Hausgemachte Käsespätzle", 11.60, "EUR", "mit Beilagensalat", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.DESSERT,"Drei Kugeln Eis", 6.10, "EUR", "mit Sahne", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.DRINKS,"Spezi", 2.10, "EUR", "0,20l", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.DRINKS,"Ginger Ale", 2.20, "EUR", "0,20l", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.DRINKS,"Warsteiner", 2.00, "EUR", "0.25l", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.DRINKS,"Guinness", 3.00, "EUR", "0.30l", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.DRINKS,"Cabernet Sauvignon", 3.90, "EUR", "0.20l", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.DRINKS,"Pinot Grigio", 3.90, "EUR", "0.20l", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.DRINKS,"Jägermeister", 2.00, "EUR", "5cl", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.DRINKS,"Sambuca", 3.90, "EUR", "5cl", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.DRINKS,"Tasse Kaffee", 1.50, "EUR", "mit Milch, Zucker", "", new ArrayList<>()),
        new MenuItem(MenuItemCategory.DRINKS,"Heiße Schokolade", 2.10, "EUR", "mit Sahne", "", new ArrayList<>())
        };
		for (MenuItem m : menu) mongoTemplate.save(m);
	}

    @Override
    public void save(MenuDto dto) {
        MenuItem item;
		if(dto.getId() != null) {
			item = mongoTemplate.findById(dto.getId(), MenuItem.class);
		} else {
            item = new MenuItem();
        }
		item.setCategory(dto.getCategory() != null ? MenuItemCategory.valueOf(dto.getCategory()) : null);
		item.setCurrency(dto.getCurrency());
        item.setDescription(dto.getDescription());
        item.setMediaUrl(dto.getMediaUrl());
        item.setName(dto.getName());
        item.setNotices(dto.getNotices());
        item.setNumber(dto.getNumber());
        item.setPrice(dto.getPrice());

        mongoTemplate.save(item);
    }


	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
}
