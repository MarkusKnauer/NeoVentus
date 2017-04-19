package de.neoventus.persistence.repository.advanced;/**
 * Created by julian on 19.04.2017.
 */

import de.neoventus.persistence.entity.Desk;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/
public interface DeskRepositoryCustom {

    //Desk-Operations
    public void insertDesk(Desk desk);
    public void updateDesk(Desk desk);
    public void deleteDesk(Desk desk);
}
