package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.customs.OrderItemRepositoryCustom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, String>,OrderItemRepositoryCustom {
    /**
     * find OrderItem by ID
     *
     * @param orderID menuItemID to search for
     * @return OrderItem
     */
    OrderItem findByOrderID(Integer orderID);
}
