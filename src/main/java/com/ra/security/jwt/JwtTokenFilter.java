package com.ra.security.jwt;

import com.ra.security.principal.UserDetailCustomService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
	private final JwtProvider jwtProvider;
	private final UserDetailCustomService userDetailCustomService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String token = getTokenFromRequest(request);
		if (token != null && jwtProvider.validationToken(token)) {
			String username = jwtProvider.getUsernameFromToken(token);
			UserDetails userDetails = userDetailCustomService.loadUserByUsername(username);
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}
	
	/**
	 * headers: {
	 * Content-Type: application/json
	 * Authorization: Bearer kladjklasjd@eqAlkdjlq4141323e
	 * }
	 */
	public String getTokenFromRequest(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}
	
}
