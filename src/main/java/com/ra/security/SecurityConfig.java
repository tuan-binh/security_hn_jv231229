package com.ra.security;

import com.ra.constants.RoleName;
import com.ra.security.exception.AccessDenied;
import com.ra.security.exception.JwtEntryPoint;
import com.ra.security.jwt.JwtTokenFilter;
import com.ra.security.principal.UserDetailCustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final UserDetailCustomService userDetailCustomService;
	private final JwtEntryPoint jwtEntryPoint;
	private final AccessDenied accessDenied;
	private final JwtTokenFilter jwtTokenFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				  .csrf(AbstractHttpConfigurer::disable)
				  .authorizeHttpRequests(
							 url -> url.requestMatchers("/api/v1/admin/**").hasAuthority(RoleName.ROLE_ADMIN.name())
										.requestMatchers("/api/v1/moderator/**").hasAuthority(RoleName.ROLE_MODERATOR.name())
										.requestMatchers("/api/v1/user/**").hasAuthority(RoleName.ROLE_USER.name())
										.anyRequest().permitAll()
				  )
				  .authenticationProvider(authenticationProvider())
				  .exceptionHandling(
							 exception -> exception
										.authenticationEntryPoint(jwtEntryPoint)
										.accessDeniedHandler(accessDenied)
				  )
				  .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
				  .build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailCustomService);
		return provider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
		return auth.getAuthenticationManager();
	}
	
}
