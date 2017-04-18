/**
 * Created by julian on 17.04.2017.
 */

package de.neoventus.init;

import de.neoventus.init.dbOperations.InsertUpdateDelete;
import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.persistence.repository.UserRepository;

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
    private DeskRepository deskRepository;
    private UserRepository userRepository;
    private MenuItemRepository menuItemRepository;
    private OrderItemRepository orderItemRepository;
    private InsertUpdateDelete insUpdDel;

    private final static Logger LOGGER = Logger.getLogger(ControlEntityObjects.class.getName());

    ControlEntityObjects(DeskRepository deskRepository,
                         UserRepository userRepository,
                         MenuItemRepository menuItemRepository,
                         OrderItemRepository orderItemRepository) {
        this.deskRepository = deskRepository;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        clearData();
        //Danger!! AC/DC with variable in the IUD-Classes (Collectionssize)!!
        insUpdDel = new InsertUpdateDelete(deskRepository, userRepository, menuItemRepository, null,
                orderItemRepository, null);
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
        User user = userRepository.findByUserID(userID);
        MenuItem menuItem = menuItemRepository.findByMenuItemID(menuItemID);
        Desk desk = deskRepository.findByNumber(deskID);
        OrderItem orderItem = new OrderItem(user, desk, menuItem, guestwish);
        insUpdDel.insertOrder(orderItem);
    }


    private void updateTest() {
        Desk des = deskRepository.findByNumber(1);
        des.setNumber(12);
        insUpdDel.updateDesk(des);
    }

    /**
     * clear before regenerate to allow changes
     */
    private void clearData() {
        deskRepository.deleteAll();
        menuItemRepository.deleteAll();
        userRepository.deleteAll();
        orderItemRepository.deleteAll();
    }

}
