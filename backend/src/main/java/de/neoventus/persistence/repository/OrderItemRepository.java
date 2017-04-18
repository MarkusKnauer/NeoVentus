package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, String> {
    /**
     * find OrderItem by ID
     *
     * @param orderID menuItemID to search for
     * @return OrderItem
     */
    OrderItem findByOrderID(Integer orderID);
}
