package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.MenuItemCategory;
import de.neoventus.persistence.repository.MenuItemCategoryRepository;
import de.neoventus.persistence.repository.advanced.NVMenuItemRepository;
import de.neoventus.rest.dto.MenuDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.logging.Logger;

/**
 * @author Julian Beck, Markus Knauer, Dennis Thanner
 * @version 0.0.4 moved set default menu to init
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
		item.setCategory(dto.getCategory() != null ? convertIntoCategory(dto.getCategory()) : null);
		item.setCurrency(dto.getCurrency());
		item.setDescription(dto.getDescription());
		item.setMediaUrl(dto.getMediaUrl());
		item.setName(dto.getName());
		item.setNotices(dto.getNotices());
		item.setNumber(dto.getNumber());
		item.setPrice(dto.getPrice());

		mongoTemplate.save(item);
	}

	//
	public MenuItemCategory convertIntoCategory(String value){
		MenuItemCategory category = menuItemCategoryRepository.findByName("category");
		if(category != null) {
			int size = value.length();
			String tmp ="";
			char c;
			for (int i = 0; i < size; i++){
				c = value.charAt(i);
				if(c == 'L') {
					c = value.charAt(++i);
					while (c != 'L' && i < size) {
						c = value.charAt(i);
						tmp += c;
						i++;
					}
					if (category.getSubcategory() != null && !category.getSubcategory().isEmpty()&& category.getSubcategory().size()>=Integer.parseInt(tmp) ) {
						category = category.getSubcategory().get(Integer.parseInt(tmp));
					}
					tmp = "";
				}
			}
		}
		return category;
	}

	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
}
