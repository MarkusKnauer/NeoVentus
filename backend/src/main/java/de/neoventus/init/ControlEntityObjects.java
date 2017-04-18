/**
 * Created by julian on 17.04.2017.
 */

package de.neoventus.init;

import de.neoventus.init.dbOperations.*;
import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.*;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author: Julian Beck, Tim Heidelbach
 * @version: 0.0.2
 * @description: Control repository access
 **/
class ControlEntityObjects {

    // for find and Update -> better methods!
    private InsertUpdateDelete insUpdDel;
    private SelectByID select;
    private final static Logger LOGGER = Logger.getLogger(ControlEntityObjects.class.getName());

    ControlEntityObjects(DeskRepository deskRepository,
                         UserRepository userRepository,
                         MenuItemRepository menuItemRepository,
                         OrderItemRepository orderItemRepository,
                         ReservationRepository reservationRepository,
                         BillingRepository billingRepository) {
        insUpdDel = new InsertUpdateDelete(deskRepository, userRepository, menuItemRepository, billingRepository,
                orderItemRepository,reservationRepository);
        select = new SelectByID(userRepository, deskRepository, menuItemRepository, billingRepository,
                orderItemRepository, reservationRepository);
    }

    /**
     * add specified demo menu items to database
     */
    void generateMenuItems(String name, double price, String currency, String description, String mediaUrl,
                           List<String> list) {
        LOGGER.info("Generate new MenuItem");
        MenuItem mI = new MenuItem();
        mI.setAll(name, price, currency, description, mediaUrl, list);
        insUpdDel.insertMenuItem(mI);
    }

    void generateDesks(int seats) {
        LOGGER.info("Init demo restaurant desks");
        Desk des = new Desk();
        des.setSeats(seats);
        insUpdDel.insertDesk(des);
    }

    /**
     * generate demo User
     */
    void generateUser(String username, String password, Permission... permission) {
        LOGGER.info("Init demo User");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPermissions(Arrays.asList(permission));
        insUpdDel.insertUser(user);
    }

    void generateOrder(Integer userID, Integer menuItemID, Integer deskID, String guestwish) {
        /*
        User user = select.findByUserID(userID);
        MenuItem menuItem = select.findByMenuItemID(menuItemID);
        Desk desk = select.findByDeskID(deskID);
        OrderItem orderItem = new OrderItem(user, desk, menuItem, guestwish);
        */
        insUpdDel.insertOrder(new OrderItem(select.findByUserID(userID),select.findByDeskID(deskID),select.findByMenuItemID(menuItemID),guestwish));
    }

    public void updateUserDesk(Integer userID, Integer deskID){
        User user = select.findByUserID(userID);
        List<Desk> tmp = user.getDesks();
        tmp.add(select.findByDeskID(deskID));
        user.setDesks(tmp);
        insUpdDel.updateUser(user);
    }

    private void updateUser(Integer userID, String field, String value) {
        User des = select.findByUserID(userID);
        insUpdDel.updateUser(des);
    }


}
