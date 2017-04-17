package de.neoventus.init.dbOperations.insertUpdateDelete;/**
 * Created by julian on 17.04.2017.
 */

import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.repository.*;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/
public class IUDMenuItem extends IUDAbstract{
    private MenuItemRepository menuItemRepository;
    private MenuItem menuItem;
    public IUDMenuItem(MenuItemRepository menuItemRepository){
        this.menuItemRepository =  menuItemRepository;
    }
}
