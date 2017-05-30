package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.SideDishGroup;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.advanced.NVSideDishRepository;
import de.neoventus.rest.dto.SideDishDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author: Julian Beck, Markus Knauer
 **/
public class SideDishRepositoryImpl implements NVSideDishRepository {

	private MongoTemplate mongoTemplate;

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Override
	public void save(SideDishDto dto) {
		SideDishGroup item;
		if(dto.getId() != null) {
			item = mongoTemplate.findById(dto.getId(), SideDishGroup.class);
		} else {
			item = new SideDishGroup();
		}

		item.setName(dto.getNameDish());
		MenuItem menuItem;


		if(dto.getMenuItemName() != null){
			menuItem = menuItemRepository.findByName(dto.getMenuItemName());
			if(menuItem != null){
				item.addSideDish(menuItem);
			}

		}

		mongoTemplate.save(item);
	}



	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
}
