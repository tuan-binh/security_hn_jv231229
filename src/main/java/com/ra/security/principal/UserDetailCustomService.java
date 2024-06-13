package com.ra.security.principal;

import com.ra.model.Users;
import com.ra.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailCustomService implements UserDetailsService {
	private final IUserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Users> optionalUsers = userRepository.findByEmail(username);
		if (optionalUsers.isPresent()) {
			Users users = optionalUsers.get();
			return UserDetailCustom.builder()
					  .id(users.getId())
					  .fullName(users.getFullName())
					  .email(users.getEmail())
					  .password(users.getPassword())
					  .phone(users.getPhone())
					  .address(users.getAddress())
					  .status(users.getStatus())
					  // simpleGrantedAuthority implements GrantedAuthority
					  .authorities(
								 users.getRoles().stream()
											.map(roles -> new SimpleGrantedAuthority(roles.getRoleName().name()))
											.toList()
					  )
					  .build();
		}
		throw new UsernameNotFoundException("username not found");
	}
}
