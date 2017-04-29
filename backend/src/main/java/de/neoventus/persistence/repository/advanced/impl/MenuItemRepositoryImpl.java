package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.repository.MenuItemCategoryRepository;
import de.neoventus.persistence.repository.advanced.NVMenuItemRepository;
import de.neoventus.rest.dto.MenuDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.logging.Logger;

/**
 * @author Julian Beck, Markus Knauer, Dennis Thanner
 * @version 0.0.5 new Categories
 * 			0.0.4 moved set default menu to init
 *          0.0.3 added null support for menu category, redundancy clean up - DT
 *          0.0.2 Insert Method save(MenuDto) MK
 *          0.0.1 created JB
 **/
public class MenuItemRepositoryImpl implements NVMenuItemRepository {

	private final static Logger LOGGER = Logger.getLogger(MenuItemRepositoryImpl.class.getName());

	private MongoTemplate mongoTemplate;

	@Autowired
	private MenuItemCategoryRepository menuItemCategoryRepository;

	@Override
	public void save(MenuDto dto) {
		MenuItem item;
		if(dto.getId() != null) {
			item = mongoTemplate.findById(dto.getId(), MenuItem.class);
		} else {
			item = new MenuItem();
		}
		item.setCategory(dto.getCategory() != null ? menuItemCategoryRepository.findByName(dto.getCategory()) : null);
		item.setCurrency(dto.getCurrency());
		item.setDescription(dto.getDescription());
		item.setMediaUrl(dto.getMediaUrl());
		item.setName(dto.getName());
		item.setNotices(dto.getNotices());
		item.setNumber(dto.getNumber());
		item.setPrice(dto.getPrice());
		item.setSideDish(null);

		mongoTemplate.save(item);
	}

	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
}
