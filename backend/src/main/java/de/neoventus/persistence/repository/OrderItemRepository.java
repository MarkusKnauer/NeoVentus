package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.advanced.NVOrderItemRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, String>, NVOrderItemRepository {
    /**
     * find OrderItem by ID
     *
     * @param orderID orderID to search for
     * @return OrderItem
     */
    OrderItem findByOrderID(Integer orderID);

    /**
     * find orderItem with max OrderItem
     *
     * @return OrderItem
     */
    OrderItem findFirstByOrderByOrderIDDesc();
}
