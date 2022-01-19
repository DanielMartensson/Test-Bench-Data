package se.danielmartensson.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserPasswordService {

	private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

	public UserPasswordService(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
		this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
	}

	public boolean changePassword(String currentUsername, String newPassword) {
		try {
			UserDetails user = inMemoryUserDetailsManager.loadUserByUsername(currentUsername);
			inMemoryUserDetailsManager.changePassword(user.getPassword(), "{noop}" + newPassword);
			return true;
		}catch(UsernameNotFoundException e) {
			return false;
		}
	}

}
