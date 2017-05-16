package de.neoventus.rest.auth;

import de.neoventus.persistence.entity.Permission;
import de.neoventus.persistence.entity.User;
import de.neoventus.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * overwrite default spring user-details for login
 * and role based access management-
 *
 * @author Dennis Thanner
 * @version 0.0.3 switched to custom user details impl
 *          0.0.2 added spring role prefix support
 **/
@Service
public class NVUserDetailsService implements UserDetailsService {

	private static String springRolePrefix = "ROLE_";

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User u = userRepository.findByUsername(username);

		if (u == null) {
			throw new UsernameNotFoundException("Invalid username: " + username);
		}

		List<GrantedAuthority> authorityList = new ArrayList<>();
		for (Permission p : u.getPermissions()) {
			authorityList.add(new SimpleGrantedAuthority(springRolePrefix + p.name()));
		}

		return new NVUserDetails(
			u.getId(),
			u.getUsername(),
			u.getPassword(),
			u.getWorkerId(),
			authorityList
		);
	}
}
