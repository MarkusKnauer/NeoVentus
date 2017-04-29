package de.neoventus.persistence.event;

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

/**
 * class for handling entity events for menuItem
 *
 * @author Dennis Thanner
 * @version 0.0.2 changed before save to before convert
 **/
@Component
public class MenuItemLifecycleEvents extends AbstractMongoEventListener<MenuItem> {

	private MenuItemRepository menuItemRepository;

	@Override
	public void onBeforeConvert(BeforeConvertEvent<MenuItem> event) {
		// automatically increment set userId
		MenuItem menuItem = event.getSource();

		// only set number if not exists yet
		if(menuItem.getNumber() == null) {
			MenuItem max = menuItemRepository.findFirstByOrderByNumberDesc();

			if( max ==null || (max.getNumber()==null)) {
				menuItem.setNumber(1);
			} else {
				menuItem.setNumber(max.getNumber() + 1);
			}
		}
	}

	@Autowired
	public void setMenuItemRepository(MenuItemRepository menuItemRepository) {
		this.menuItemRepository = menuItemRepository;
	}
}
