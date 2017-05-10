package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.entity.OrderItem;
import de.neoventus.persistence.repository.*;
import de.neoventus.persistence.repository.advanced.NVOrderItemRepository;
import de.neoventus.rest.dto.OrderItemDto;
import de.neoventus.rest.dto.OrderItemOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Julian Beck, Dennis Thanner
 * @version 0.0.5 orderState refactoring - DT
 *          0.0.4 state with enum - DS
 *          0.0.3 redundancy clean up - DT
 *          0.0.2 added variable state - DS
 *          0.0.1
 **/
public class OrderItemRepositoryImpl implements NVOrderItemRepository {

	@Autowired
	private OrderItemRepository orderItemRepository;

	private MongoTemplate mongoTemplate;
	private DeskRepository deskRepository;
	private MenuItemRepository menuItemRepository;
	private UserRepository userRepository;
	private ReservationRepository reservationRepository;
	private BillingRepository billingRepository;

	@Override
	public void save(OrderItemDto dto) {
		OrderItem o;

		if (dto.getId() != null) {
			o = mongoTemplate.findById(dto.getId(), OrderItem.class);
		} else {
			o = new OrderItem();
		}
		o.setDesk(dto.getDeskNumber() != null ? deskRepository.findByNumber(dto.getDeskNumber()) : null);
		o.setItem(dto.getMenuItemNumber() != null ? menuItemRepository.findByNumber(dto.getMenuItemNumber()) : null);
		o.setWaiter(dto.getWaiter() != null ? userRepository.findByWorkerId(dto.getWaiter()) : null);
		o.setGuestWish(dto.getGuestWish() != null ? dto.getGuestWish() : "");

		mongoTemplate.save(o);

	}

	@Override
	public List<OrderItemOutputDto> searchOrderItemOutputDto(Integer number) {
		Desk desk = deskRepository.findByNumber(number);
		List<OrderItem> list = orderItemRepository.findAllOrderItemByDeskIdOrderByItemMenuItemCategoryId(desk.getId());
		OrderItemOutputDto tmp;
		List<OrderItemOutputDto> output = new ArrayList<OrderItemOutputDto>();
		Integer counter = 0;
		for (int i = 0; i < list.size(); i++) {
			counter = 1;
			tmp = new OrderItemOutputDto();
			tmp.addOrderItemIds(list.get(i).getId());
			tmp.setDesk(number.toString());
			tmp.setWaiter(list.get(i).getWaiter().getUsername());
			tmp.setCategory(list.get(i).getItem().getMenuItemCategory().getName());
			tmp.setGuestWish(list.get(i).getGuestWish());
			tmp.setMenuItem(list.get(i).getItem().getName());
			tmp.setMenuItemCounter(counter);
			tmp.setPrice(list.get(i).getItem().getPrice());

			int j = i + 1;
			while (j < list.size() && (list.get(i).getItem().getMenuItemCategory().getName()).equals((list.get(j).getItem().getMenuItemCategory().getName()))) {
				counter++;
				tmp.setPrice(tmp.getPrice() + list.get(j).getItem().getPrice());
				tmp.setMenuItemCounter(counter);
				tmp.addOrderItemIds(list.get(j).getId());
				j++;
			}
			i = j - 1;
			output.add(tmp);
		}
		return output;
	}

	//Setter

	@Autowired
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Autowired
	public void setDeskRepository(DeskRepository deskRepository) {
		this.deskRepository = deskRepository;
	}

	@Autowired
	public void setMenuItemRepository(MenuItemRepository menuItemRepository) {
		this.menuItemRepository = menuItemRepository;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	public void setReservationRepository(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Autowired
	public void setBillingRepository(BillingRepository billingRepository) {
		this.billingRepository = billingRepository;
	}
	// Next  Respo Lager (not the Beer)
}
