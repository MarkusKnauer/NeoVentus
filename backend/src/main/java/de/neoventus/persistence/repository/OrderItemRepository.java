package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.entity.OrderItemState;
import de.neoventus.persistence.repository.advanced.NVOrderItemRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dennis Thanner
 * @version 0.0.2 redundancy clean up - DT
 **/
@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, String>, NVOrderItemRepository {

	/**
	 * find all OrderItems by state
	 *
	 * @param state
	 * @return Iterable<OrderItem>
	 */
	Iterable<OrderItem> findByState(OrderItemState state);

}
