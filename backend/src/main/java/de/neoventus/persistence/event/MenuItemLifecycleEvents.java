package de.neoventus.persistence.event;

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

/**
 * class for handling entity events for menuItem
 *
 * @author Dennis Thanner
 * @version 0.0.1
 **/
@Component
public class MenuItemLifecycleEvents extends AbstractMongoEventListener<MenuItem> {

	private MenuItemRepository menuItemRepository;

	@Override
	public void onBeforeSave(BeforeSaveEvent<MenuItem> event) {
		// automatically increment set userId
		MenuItem menuItem = event.getSource();

		// only set number if not exists yet
		if(menuItem.getNumber() == null) {
			MenuItem max = menuItemRepository.findFirstByOrderByNumberDesc();

			menuItem.setNumber(max == null ? 1 : max.getNumber() + 1);
		}
	}

	@Autowired
	public void setMenuItemRepository(MenuItemRepository menuItemRepository) {
		this.menuItemRepository = menuItemRepository;
	}
}
