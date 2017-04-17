package de.neoventus.init.dbOperations.insertUpdateDelete;/**
 * Created by julian on 17.04.2017.
 */

import de.neoventus.persistence.entity.*;
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

    @Override
    public void insert(AbstractDocument abs) {
        menuItem = (MenuItem)abs;
        setCollectionSize(getCollectionSize()+1);
        menuItem.setMenuItemID(getCollectionSize());
        menuItemRepository.save(menuItem);
    }

    @Override
    public void update(AbstractDocument abs) {
        menuItem = (MenuItem)abs;
        menuItemRepository.save(menuItem);
    }
    @Override
    public void delete(AbstractDocument abs) {
        menuItem = (MenuItem)abs;
        setCollectionSize(getCollectionSize()-1);
        menuItemRepository.delete(menuItem);
    }
}
