package de.neoventus.init.dbOperations;/**
 * Created by julian on 18.04.2017.
 */

import de.neoventus.init.dbOperations.insertUpdateDelete.*;
import de.neoventus.persistence.repository.*;
import de.neoventus.persistence.entity.*;
/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/
public class SelectByID {

    UserRepository userRepository;
    MenuItemRepository menuItemRepository;
    BillingRepository billingRepository;
    OrderItemRepository orderItemRepository;
    ReservationRepository reservationRepository;
    DeskRepository deskRepository;

    public SelectByID(UserRepository userRepository,
                      DeskRepository deskRepository,
                      MenuItemRepository menuItemRepository,
                      BillingRepository billingRepository,
                      OrderItemRepository orderItemRepository,
                      ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.deskRepository = deskRepository;
        this.menuItemRepository= menuItemRepository;
        this.billingRepository = billingRepository;
        this.orderItemRepository = orderItemRepository;
        this.reservationRepository = reservationRepository;

    }

    public User findByUserID(Integer id){return userRepository.findByUserID(id);}
    public MenuItem findByMenuItemID(Integer id){return menuItemRepository.findByMenuItemID(id);}
    public Billing findByBillingID(Integer id){return billingRepository.findByBillingID(id);}
    public OrderItem findByOrderItemID(Integer id){return orderItemRepository.findByOrderID(id);}
    public Reservation findByReservationID(Integer id){return reservationRepository.findByReservationID(id);}
    public Desk findByDeskID(Integer id){return deskRepository.findByNumber(id);}


}
