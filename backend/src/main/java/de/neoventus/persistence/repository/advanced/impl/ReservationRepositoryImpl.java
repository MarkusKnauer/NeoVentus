package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.Reservation;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.UserRepository;
import de.neoventus.persistence.repository.advanced.NVReservationRepository;
import de.neoventus.rest.dto.ReservationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.logging.Logger;

/**
 * @author Tim Heidelbach
 * @version 0.0.1
 **/
@Repository
public class ReservationRepositoryImpl implements NVReservationRepository {

	private static final Logger LOGGER = Logger.getLogger(ReservationRepositoryImpl.class.getName());
	private MongoTemplate mongoTemplate;
	private DeskRepository deskRepository;
	private UserRepository userRepository;

	@Override
	public void save(ReservationDto dto) {

		Reservation reservation;
		if (dto.getId() != null) {
			reservation = mongoTemplate.findById(dto.getId(), Reservation.class);
		} else {
			reservation = new Reservation();
		}

		for (String deskId : dto.getDesk()) {
			reservation.getDesk().add(deskRepository.findOne(deskId));
		}
		reservation.setDuration(dto.getDuration());
		reservation.setReservedBy(userRepository.findByUsername(dto.getReservedBy()));
		reservation.setReservationName(dto.getReservationName());
		reservation.setTime(dto.getTime());


		mongoTemplate.save(reservation);
		LOGGER.info("Reservation " + dto.getId() + " was saved");
	}

	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Autowired
	public void setDeskRepository(DeskRepository deskRepository) {
		this.deskRepository = deskRepository;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
}
