package de.neoventus.persistence.repository.implementation;/**
 * Created by julian on 19.04.2017.
 */

import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.OrderItemRepository;
import de.neoventus.persistence.repository.customs.OrderItemRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Julian Beck
 * @version: 0.0.1
 * @description:
 **/
public class OrderItemRepositoryImpl implements OrderItemRepositoryCustom{
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public void insertOrder(OrderItem item) {
        item.setOrderID((int)orderItemRepository.count()+1);
        orderItemRepository.save(item);
    }

    @Override
    public void updateOrder(OrderItem item) {
        orderItemRepository.save(item);
    }

    @Override
    public void deleteOrder(OrderItem item) {
        orderItemRepository.delete(item);
    }
}
