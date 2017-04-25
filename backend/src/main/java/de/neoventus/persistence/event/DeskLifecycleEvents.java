package de.neoventus.persistence.event;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.repository.DeskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

/**
 * class for handling entity events for desks
 *
 * @author Dennis Thanner
 * @version 0.0.2 changed before save to before convert
 **/
@Component
public class DeskLifecycleEvents extends AbstractMongoEventListener<Desk> {

	private DeskRepository deskRepository;

	@Override
	public void onBeforeConvert(BeforeConvertEvent<Desk> event) {
		// automatically increment set deskId
		Desk desk = event.getSource();

		// only set number if not exists yet
		if(desk.getNumber() == null) {
			Desk max = deskRepository.findFirstByOrderByNumberDesc();

			desk.setNumber(max == null ? 1 : max.getNumber() + 1);
		}
	}

	@Autowired
	public void setDeskRepository(DeskRepository deskRepository) {
		this.deskRepository = deskRepository;
	}
}
