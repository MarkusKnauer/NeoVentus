package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.Desk;
import de.neoventus.persistence.repository.advanced.NVDeskRepository;
import de.neoventus.rest.dto.DeskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author Julian Beck, Dominik Streif
 * @version 0.0.1
 **/
public class DeskRepositoryImpl implements NVDeskRepository {

	private MongoTemplate mongoTemplate;

	@Override
	public void save(DeskDto dto) {

		Desk d;
		if (dto.getId() != null) {
			d = mongoTemplate.findById(dto.getId(), Desk.class);
		} else {
			d = new Desk();
		}

		d.setSeats(dto.getSeats());

		mongoTemplate.save(d);

	}

	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

}
