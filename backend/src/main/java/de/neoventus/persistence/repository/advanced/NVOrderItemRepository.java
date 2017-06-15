package de.neoventus.persistence.repository.advanced;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.repository.advanced.impl.aggregation.OrderDeskAggregationDto;
import de.neoventus.rest.dto.OrderItemDto;

import java.util.List;
import java.util.Map;

/**
 * @author Julian Beck, Dennis Thanner
 */
public interface NVOrderItemRepository {

	/**
	 * convenience method to update or method user by dto
	 *
	 * @param dto
	 */
	void save(OrderItemDto dto);

	/**
	 * list aggregated orders group by item and count them
	 *
	 * @param desk
	 * @return
	 */
	List<OrderDeskAggregationDto> getGroupedNotPayedOrdersByItemForDesk(Desk... desk);

	/**
	 * get unfinished unpaid orders grouped by desk
	 *
	 * @param forKitchen@return
	 */
	Map<Integer, List<OrderDeskAggregationDto>> getUnfinishedOrdersForCategoriesGroupedByDeskAndOrderItem(boolean forKitchen);

	/**
	 * get unfinehs unpaid order grouped by item
	 *
	 * @param forKitchen@return
	 */
	List<OrderDeskAggregationDto> getUnfinishedOrderForCategoriesGroupedByItemOrderByCount(boolean forKitchen);

	/**
	 * update orders and change desk
	 *
	 * @param deskId
	 * @param orderIds
	 */
	void changeDeskForOrders(String deskId, String... orderIds);
}

