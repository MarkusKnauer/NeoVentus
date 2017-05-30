package de.neoventus.persistence.repository.advanced;

import de.neoventus.persistence.repository.advanced.impl.aggregation.GuestWishCount;
import de.neoventus.rest.dto.MenuDto;

import java.util.List;


/**
 * @author Julian Beck, Markus Knauer, Dennis Thanner
 */
public interface NVMenuItemRepository {

	/**
	 * convenience save method
	 *
	 * @param dto
	 */
	void save(MenuDto dto);

	/**
	 * get popular guest wished for item
	 *
	 * @param id
	 * @return
	 */
	List<GuestWishCount> getPopularGuestWishesForItem(String id);

}
