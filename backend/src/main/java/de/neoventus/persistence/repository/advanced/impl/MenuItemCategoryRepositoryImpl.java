package de.neoventus.persistence.repository.advanced.impl;/**
 * Created by julian on 28.04.2017.
 */

import de.neoventus.persistence.entity.MenuItemCategory;
import de.neoventus.persistence.repository.MenuItemCategoryRepository;
import de.neoventus.persistence.repository.advanced.NVMenuItemCategoryRepository;
import de.neoventus.rest.dto.MenuItemCategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.logging.Logger;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/
public class MenuItemCategoryRepositoryImpl implements NVMenuItemCategoryRepository {
	private final static Logger LOGGER = Logger.getLogger(MenuItemCategoryRepositoryImpl.class.getName());


	private MongoTemplate mongoTemplate;

	@Autowired
	private MenuItemCategoryRepository menuItemCategoryRepository;


	@Override
	public void save(MenuItemCategoryDto dto) {
		MenuItemCategory item;
		if(dto.getId() != null) {
			item = mongoTemplate.findById(dto.getId(), MenuItemCategory.class);
		} else {
			item = new MenuItemCategory();
		}

		item.setParent( menuItemCategoryRepository.findByName(dto.getParent()));
		item.setName(dto.getName());
		mongoTemplate.save(item);
	}



	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
}
