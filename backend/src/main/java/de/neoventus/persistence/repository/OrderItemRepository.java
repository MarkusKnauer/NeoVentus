package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.entity.OrderItemState;
import de.neoventus.persistence.repository.advanced.NVOrderItemRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Dennis Thanner
 * @version 0.0.2 redundancy clean up - DT
 **/
@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, String>, NVOrderItemRepository {

	/**
	 * find orderItem by deskNumber
	 *
	 * @param desk name to search for
	 * @return OrderItem
	 */
	OrderItem findByDesk(Desk desk);



	/**
	 * find orderItem by deskNumber
	 *
	 * @param id name to search for
	 * @return OrderItem
	 */

	List<OrderItem> findAllOrderItemByDeskIdOrderByItemMenuItemCategoryId(String id);

	List<OrderItem> findAllOrderItemByItemIdAndDesk(String id,Desk desk);

	/**
	 * find all OrderItems by state
	 *
	 * @param state
	 * @return Iterable<OrderItem>
	 */
	Iterable<OrderItem> findByState(OrderItemState state);

}
