package de.neoventus.persistence.event;/**
 * Created by julian on 28.04.2017.
 */

import de.neoventus.persistence.entity.MenuItemCategory;
import de.neoventus.persistence.repository.MenuItemCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/

	@Component
	public class MenuItemCategoryLifecycleEvents extends AbstractMongoEventListener<MenuItemCategory> {

		private MenuItemCategoryRepository menuItemRepositoryCategory;

		@Override
		public void onAfterSave(AfterSaveEvent<MenuItemCategory> event){
			MenuItemCategory max = event.getSource();
			if(max.getParent() != null) {
				MenuItemCategory parent = max.getParent();

				// Break for Recur.
				if(!parent.getSubcategory().contains(max)){
					parent.addSubcategory(max);
					menuItemRepositoryCategory.save(parent);
				}

			}

		}


		@Autowired
		public void setMenuItemRepository(MenuItemCategoryRepository menuItemRepositoryCategory) {
			this.menuItemRepositoryCategory = menuItemRepositoryCategory;
		}

}
