package de.neoventus.persistence.repository.advanced;

import de.neoventus.rest.dto.OrderItemDto;

/**
 * @author Julian Beck
 * @version 0.0.1
 */
public interface NVOrderItemRepository {

	/**
	 * convenience method to update or method user by dto
	 *
	 * @param dto
	 */
	void save(OrderItemDto dto);


}

