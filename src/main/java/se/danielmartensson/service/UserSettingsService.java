package se.danielmartensson.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsService {

	private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
	
	@Value("${login.masterRights}")
	private String masterRights;

	public UserSettingsService(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
		this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
	}

	public boolean changePassword(String username, String newPassword) {
		try {
			UserDetails user = inMemoryUserDetailsManager.loadUserByUsername(username);
			inMemoryUserDetailsManager.updatePassword(user, "{noop}" + newPassword);
			return true;
		}catch(UsernameNotFoundException e) {
			return false;
		}
		
	}
	
	public boolean loggedInUserIsMaster() {
		String username = getLoggedInUsername();
		Collection<? extends GrantedAuthority> authorities = inMemoryUserDetailsManager.loadUserByUsername(username).getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator(); // Only one rights are applied into the list
		String rights = iterator.next().getAuthority();
		if(rights.equals("ROLE_" + masterRights))
			return true;
		else
			return false;
	}
	
	public String getLoggedInUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
}
