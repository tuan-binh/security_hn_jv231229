package com.ra.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JwtResponse {
	private String accessToken;
	private String fullName;
	private String email;
	private String phone;
	private String address;
	private Boolean status;
	private Set<String> roles;
}
