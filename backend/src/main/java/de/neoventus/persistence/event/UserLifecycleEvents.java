package de.neoventus.persistence.event;

import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

/**
 * class for handling entity events for user
 *
 * @author Dennis Thanner
 * @version 0.0.1
 **/
@Component
public class UserLifecycleEvents extends AbstractMongoEventListener<User> {

	private UserRepository userRepository;

	@Override
	public void onBeforeSave(BeforeSaveEvent<User> event) {
		// automatically increment set userId
		User user = event.getSource();

		// only a worker if he has a permission - and set only if not existis
		if(!user.getPermissions().isEmpty() && user.getUserId() == null) {
			User max = userRepository.findFirstByOrderByUserIdDesc();

			user.setUserId(max == null || max.getUserId() == null ? 1 : max.getUserId() + 1);
		}
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
}
