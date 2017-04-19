package de.neoventus.persistence.repository.customs;

import de.neoventus.persistence.entity.MenuItem;

/**
 * Created by julian on 19.04.2017.
 */
public interface MenuItemRepositoryCustom {

    //MenuItem-Operations
    public void insertMenuItem(MenuItem item);
    public void updateMenuItem(MenuItem item);
    public void deleteMenuItem(MenuItem item);
    public void setDefaultMenu();

}
