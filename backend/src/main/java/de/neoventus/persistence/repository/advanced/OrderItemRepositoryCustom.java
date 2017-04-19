package de.neoventus.persistence.repository.advanced;

import de.neoventus.persistence.entity.OrderItem;

/**
 * Created by julian on 19.04.2017.
 */
public interface OrderItemRepositoryCustom {

    //Order-Operations
    public void insertOrder(OrderItem item);
    public void updateOrder(OrderItem item);
    public void deleteOrder(OrderItem item);
}
