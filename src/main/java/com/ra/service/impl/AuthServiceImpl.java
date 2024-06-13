package com.ra.service.impl;

import com.ra.constants.RoleName;
import com.ra.dto.request.FormLogin;
import com.ra.dto.request.FormRegister;
import com.ra.dto.response.JwtResponse;
import com.ra.exception.CustomException;
import com.ra.model.Roles;
import com.ra.model.Users;
import com.ra.repository.IUserRepository;
import com.ra.security.jwt.JwtProvider;
import com.ra.security.principal.UserDetailCustom;
import com.ra.service.IAuthService;
import com.ra.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
	private final IRoleService roleService;
	private final IUserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public JwtResponse handleLogin(FormLogin formLogin) throws CustomException {
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(formLogin.getEmail(), formLogin.getPassword()));
		} catch (AuthenticationException e) {
			throw new CustomException("Invalid email or password",HttpStatus.CONFLICT);
		}
		UserDetailCustom userDetailCustom = (UserDetailCustom) authentication.getPrincipal();
		
		String accessToken = jwtProvider.generateToken(userDetailCustom);
		
		return JwtResponse.builder()
				  .accessToken(accessToken)
				  .fullName(userDetailCustom.getFullName())
				  .email(userDetailCustom.getEmail())
				  .address(userDetailCustom.getAddress())
				  .phone(userDetailCustom.getPhone())
				  .status(userDetailCustom.getStatus())
				  .roles(userDetailCustom.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
				  .build();
	}
	
	@Override
	public void handleRegister(FormRegister formRegister) {
		Set<Roles> roles = new HashSet<>();
		if (formRegister.getRoles() == null || formRegister.getRoles().isEmpty()) {
			roles.add(roleService.findByRoleName(RoleName.ROLE_USER));
		} else {
			formRegister.getRoles().forEach(role -> {
				switch (role) {
					case "admin":
						roles.add(roleService.findByRoleName(RoleName.ROLE_ADMIN));
					case "moderator":
						roles.add(roleService.findByRoleName(RoleName.ROLE_MODERATOR));
					case "user":
						roles.add(roleService.findByRoleName(RoleName.ROLE_USER));
					default:
						throw new RuntimeException("role not found");
				}
			});
		}
		
		Users users = Users.builder()
				  .fullName(formRegister.getFullName())
				  .email(formRegister.getEmail())
				  .password(passwordEncoder.encode(formRegister.getPassword()))
				  .roles(roles)
				  .build();
		userRepository.save(users);
	}
}
