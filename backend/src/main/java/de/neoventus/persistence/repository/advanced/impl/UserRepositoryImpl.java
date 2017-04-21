package de.neoventus.persistence.repository.advanced.impl;

import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.DeskRepository;
import de.neoventus.persistence.repository.advanced.NVUserRepository;
import de.neoventus.rest.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

/**
 * @author Dennis Thanner
 * @version 0.0.1
 **/
@Repository
public class UserRepositoryImpl implements NVUserRepository {

	private MongoTemplate mongoTemplate;

	private DeskRepository deskRepository;


	@Override
	public void save(UserDto dto) {
		User u;
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		if (dto.getId() != null) {
			u = mongoTemplate.findById(dto.getId(), User.class);
		} else {
			u = new User();
		}

		u.setPassword(dto.getPassword() != null ? bCryptPasswordEncoder.encode(dto.getPassword()) : null);
		u.setUsername(dto.getUsername());
		u.setFirstName(dto.getFirstName());
		u.setLastName(dto.getLastName());

		// todo add permissions
//		u.getPermissions().clear();
//		for(Permission p : dto.getPermissions()) {
//
//		}

		u.getDesks().clear();
		for(String deskId : dto.getDesks()) {
			u.getDesks().add(deskRepository.findOne(deskId));
		}

		mongoTemplate.save(u);
	}

	@Autowired
	private void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Autowired
	public void setDeskRepository(DeskRepository deskRepository) {
		this.deskRepository = deskRepository;
	}
}
