package de.neoventus.init.dbOperations;/**
 * Created by julian on 17.04.2017.
 */
import de.neoventus.init.dbOperations.insertUpdateDelete.*;
import de.neoventus.persistence.entity.*;
import de.neoventus.persistence.repository.*;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description: Easier-way to coordinate the insert, update, delete on DB with complex AC/DCs
 **/
public class InsertUpdateDelete {
    private IUDBillingItem iudBillingItem;
    private IUDDesk iudDesk;
    private IUDMenuItem iudMenuItem;
    private IUDOrder iudOrder;
    private IUDReservation iudReservation;
    private IUDUser iudUser;

    public InsertUpdateDelete(DeskRepository deskRepository,
                   UserRepository userRepository,
                   MenuItemRepository menuItemRepository,
                   BillingRepository billingRepository,
                   OrderItemRepository  orderItemRepository,
                   ReservationRepository reservationRepository){

        iudBillingItem = new IUDBillingItem(billingRepository);
        iudDesk = new IUDDesk(deskRepository);
        iudMenuItem = new IUDMenuItem(menuItemRepository);
        iudOrder = new IUDOrder(orderItemRepository);
        iudReservation = new IUDReservation(reservationRepository);
        iudUser = new IUDUser(userRepository);
    }

    // User-Operations
    public void insertUser(User user){iudUser.insert(user);}
    public void updateUser(User user){iudUser.update(user);}
    public void deleteUser(User user){iudUser.delete(user);}

    //Desk-Operations
    public void insertDesk(Desk item){iudDesk.insert(item);}
    public void updateDesk(Desk item){iudDesk.update(item);}
    public void deleteDesk(Desk item){iudDesk.delete(item);}

    //MenuItem-Operations
    public void insertMenuItem(MenuItem item){iudMenuItem.insert(item);}
    public void updateMenuItem(MenuItem item){iudMenuItem.update(item);}
    public void deleteMenuItem(MenuItem item){iudMenuItem.delete(item);}

    //Order-Operations
    public void insertOrder(OrderItem item){iudOrder.insert(item);}
    public void updateOrder(OrderItem item){iudOrder.update(item);}
    public void deleteOrder(OrderItem item){iudOrder.delete(item);}

    //Reservation-Operations
    public void insertReservation(Reservation item){iudReservation.insert(item);}
    public void updateReservation(Reservation item){iudReservation.update(item);}
    public void deleteReservation(Reservation item){iudReservation.delete(item);}

    //Billing-Operations
    public void insertBilling(Billing item){iudBillingItem.insert(item);}
    public void updateBilling(Billing item){iudBillingItem.update(item);}
    public void deleteBilling(Billing item){iudBillingItem.delete(item);}

}
