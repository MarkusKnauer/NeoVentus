package de.neoventus.persistence.repository.advanced.impl;/**
 * Created by julian on 28.04.2017.
 */

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.SideDishGroup;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.SideDishRepository;
import de.neoventus.persistence.repository.advanced.NVSideDishRepository;
import de.neoventus.rest.dto.SideDishDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.logging.Logger;

/**
 * @author: Julian Beck, Markus Knauer
 * @version: 0.0.2 edited (removed tree) MK
 * 			0.0.1 created by JB
 *
 **/
public class SideDishRepositoryImpl implements NVSideDishRepository {
	private final static Logger LOGGER = Logger.getLogger(SideDishRepositoryImpl.class.getName());


	private MongoTemplate mongoTemplate;

	@Autowired
	private SideDishRepository sideDishRepository;

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
