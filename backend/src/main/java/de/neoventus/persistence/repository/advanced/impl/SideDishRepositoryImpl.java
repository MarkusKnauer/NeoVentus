package de.neoventus.persistence.repository.advanced.impl;/**
 * Created by julian on 28.04.2017.
 */

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.SideDish;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.SideDishRepository;
import de.neoventus.persistence.repository.advanced.NVSideDishRepository;
import de.neoventus.rest.dto.SideDishDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.logging.Logger;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
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
		SideDish item;
		if(dto.getId() != null) {
			item = mongoTemplate.findById(dto.getId(), SideDish.class);
		} else {
			item = new SideDish();
		}

		item.setSideDishName(dto.getNameDish());
		SideDish find;
		MenuItem menuItem;

		if(dto.getTargetMenuItem()!= null){

			find = sideDishRepository.findBySideDishName(dto.getTargetMenuItem());
			if(find != null){
				item.addParentalMeal(find);
			}else{

			}
		}

		if(dto.getMenuItemName() != null){
			menuItem = menuItemRepository.findByName(dto.getMenuItemName());
			if(menuItem != null){
				item.setActualMenuItem(menuItem);
			}

		}

		mongoTemplate.save(item);
	}



	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
}
