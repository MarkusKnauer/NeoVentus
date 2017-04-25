package de.neoventus.persistence.event;

import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

/**
 * class for handling entity events for user
 *
 * @author Dennis Thanner
 * @version 0.0.2 changed before save to before convert
 **/
@Component
public class UserLifecycleEvents extends AbstractMongoEventListener<User> {

	private UserRepository userRepository;

	@Override
	public void onBeforeConvert(BeforeConvertEvent<User> event) {
		// automatically increment set userId
		User user = event.getSource();

		// only a worker if he has a permission - and set only if not existis
		if (!user.getPermissions().isEmpty() && user.getWorkerId() == null) {
			User max = userRepository.findFirstByOrderByWorkerIdDesc();

			user.setWorkerId(max == null || max.getWorkerId() == null ? 1 : max.getWorkerId() + 1);
		}
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
}
