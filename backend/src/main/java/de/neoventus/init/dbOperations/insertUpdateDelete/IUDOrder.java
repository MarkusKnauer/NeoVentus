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
public class IUDOrder extends IUDAbstract{
    private OrderItemRepository  orderItemRepository;
    private OrderItem orderItem;

    public IUDOrder(OrderItemRepository  orderItemRepository){
     this.orderItemRepository = orderItemRepository;
    }
    @Override
    public void insert(AbstractDocument abs) {
        orderItem = (OrderItem)abs;
        setCollectionSize(getCollectionSize()+1);
        orderItem.setOrderID(getCollectionSize());
        orderItemRepository.save(orderItem);
    }

    @Override
    public void update(AbstractDocument abs) {
        orderItem = (OrderItem)abs;
        orderItemRepository.save(orderItem);
    }
    @Override
    public void delete(AbstractDocument abs) {
        orderItem = (OrderItem)abs;
        setCollectionSize(getCollectionSize()-1);
        orderItemRepository.delete(orderItem);
    }
}
