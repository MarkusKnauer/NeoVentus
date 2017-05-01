package de.neoventus.persistence.event;/**
 * Created by julian on 28.04.2017.
 */

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.SideDish;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.SideDishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

/**
 * @author: Julian Beck
 * @version: 0.0.1
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
				if(sideDishRepository.findBySideDishName(max.getSideDishName())!= null){

				}
			}
		}


		@Override
		public void onAfterSave(AfterSaveEvent<SideDish> event){
			SideDish max = event.getSource();

			if(max != null && max.getParentMeal() != null) {

				SideDish parent = max.getLastParentMeal();
				if(parent != null) {
					if (parent.getSubMeal() == null) {
						if(max.getActualMenuItem() == null){
							parent.addsubMeal(max);
						} else{
							parent.addsubMeal(max.getActualMenuItem().getSideDish());
						}

						sideDishRepository.save(parent);

					}
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
