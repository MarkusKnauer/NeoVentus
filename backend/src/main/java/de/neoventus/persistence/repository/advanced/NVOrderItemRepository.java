package de.neoventus.persistence.repository.advanced;

import de.neoventus.rest.dto.OrderItemDto;

/**
 * @author Julian Beck, Dennis Thanner
 * @version 0.0.2 deleted searchOrderItemOutputDto method - DT
 */
public interface NVOrderItemRepository {

	/**
	 * convenience method to update or method user by dto
	 *
	 * @param dto
	 */
	void save(OrderItemDto dto);

}

