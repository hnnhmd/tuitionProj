package com.fdmgroup.TuitionProjectSpring;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepo;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Parent> userOptional = userRepo.findByUsername(username);
		Parent parent = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return new UserPrincipal(parent);
	}
	
}
