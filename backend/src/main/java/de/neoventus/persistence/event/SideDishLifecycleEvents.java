package de.neoventus.persistence.event;/**
 * Created by julian on 28.04.2017.
 */

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.SideDish;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.SideDishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

/**
 * @author: Julian Beck, Markus Knauer
 * @version:0.0.2 edited (deleted tree structure) MK
 * 			0.0.1 crated by JB
 * @description:
 **/

	@Component
	public class SideDishLifecycleEvents extends AbstractMongoEventListener<SideDish> {

		private SideDishRepository sideDishRepository;
		private MenuItemRepository menuItemRepository;
		private MenuItem menuItem;

		@Override
		public void onBeforeConvert(BeforeConvertEvent<SideDish> event){
			SideDish max = event.getSource();
			if (max != null){
				if (sideDishRepository.findByName(max.getName()) != null) {

				}
			}
		}


		@Autowired
		public void setSideDishRepository(SideDishRepository sideDishRepository) {
			this.sideDishRepository = sideDishRepository;
		}
		@Autowired
		public void setMenuItemRepository(MenuItemRepository menuItemRepository){
			this.menuItemRepository =menuItemRepository;
		}
}
