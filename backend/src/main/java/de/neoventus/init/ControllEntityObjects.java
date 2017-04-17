package de.neoventus.init;/**
 * Created by julian on 17.04.2017.
 */
import de.neoventus.init.dbOperations.InsertUpdateDelete;
import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.MenuItem;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.MenuItemRepository;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.persistence.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description: Controll Respository access
 **/
public class ControllEntityObjects {

    // for find and Update -> better methods!
    private DeskRepository deskRepository;
    private UserRepository userRepository;
    private MenuItemRepository menuItemRepository;
    private OrderItemRepository orderItemRepository;
    private InsertUpdateDelete insUpdDel;


    private Logger logger = Logger.getLogger(this.getClass().getName());

    //
    public ControllEntityObjects(DeskRepository deskRepository,
                                 UserRepository userRepository,
                                 MenuItemRepository menuItemRepository,
                                 OrderItemRepository orderItemRepository){
        this.deskRepository =deskRepository;
        this.menuItemRepository =menuItemRepository;
        this.userRepository =userRepository;
        this.orderItemRepository = orderItemRepository;
        clearData();
        //Danger!! AC/DC with variable in the IUD-Classes (Collectionssize)!!
        insUpdDel = new InsertUpdateDelete(deskRepository, userRepository,menuItemRepository,null, orderItemRepository,null );
    }
    /**
     * add specified demo menu items to database
     */
    public void generateMenuItems(String name, double price, String currency, String description, String mediaUrl, List list ) {
        logger.info("Generate new MenuItem");
        MenuItem mI  = new MenuItem();
        mI.setAll(name, price,currency,description,mediaUrl,list);
        insUpdDel.insertMenuItem(mI);
    }

    /**
     *
     */
    public void generateDesks(int seats) {
        logger.info("Init demo restaurant desks");
            Desk des = new Desk();
            des.setSeats(seats);
            insUpdDel.insertDesk(des);
    }

    /**
     * generate demo User
     */
    public void generateUser(String username, String password, Permission permission) {
        logger.info("Init demo User");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPermissions(Arrays.asList(permission));

        insUpdDel.insertUser(user);
    }

    public void generateOrder(Integer userID, Integer menuItemID, Integer deskID, String guestwish){
        User user = userRepository.findByUserID(userID);
        MenuItem menuItem = menuItemRepository.findByMenuItemID(menuItemID);
        Desk desk = deskRepository.findByNumber(deskID);
        OrderItem orderItem = new OrderItem(user, desk, menuItem, guestwish);
        insUpdDel.insertOrder(orderItem);
    }



    private void updateTest(){
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
