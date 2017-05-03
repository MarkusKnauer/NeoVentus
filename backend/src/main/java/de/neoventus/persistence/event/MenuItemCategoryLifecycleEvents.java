package de.neoventus.persistence.event;

import de.neoventus.persistence.entity.MenuItemCategory;
import de.neoventus.persistence.repository.MenuItemCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author Julian Beck, Dennis Thanner
 * @version 0.0.2 improved after save event to stop recursion - DT
 **/
@Component
public class MenuItemCategoryLifecycleEvents extends AbstractMongoEventListener<MenuItemCategory> {

	private MenuItemCategoryRepository menuItemRepositoryCategory;

	@Override
	public void onAfterSave(AfterSaveEvent<MenuItemCategory> event) {
		MenuItemCategory current = event.getSource();
		MenuItemCategory newParent = current.getParent();
		MenuItemCategory oldParent = menuItemRepositoryCategory.findBySubcategoryContaining(current);

		// update old parent
		if (oldParent != null) {
			if (!oldParent.equals(newParent)) {
				oldParent.getSubcategory().remove(current);
				menuItemRepositoryCategory.save(oldParent);
			}
		}

		// update new parent
		if (newParent != null) {
			if (newParent.getSubcategory() == null) {
				newParent.setSubcategory(new ArrayList<>());
			}
			if (!newParent.getSubcategory().contains(current)) {
				newParent.getSubcategory().add(current);
				menuItemRepositoryCategory.save(newParent);
			}
		}

	}


	@Autowired
	public void setMenuItemRepository(MenuItemCategoryRepository menuItemRepositoryCategory) {
		this.menuItemRepositoryCategory = menuItemRepositoryCategory;
	}

}
