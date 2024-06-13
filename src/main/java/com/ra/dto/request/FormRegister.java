package com.ra.dto.request;

import com.ra.validation.EmailExist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormRegister {
	private String fullName;
	@EmailExist(message = "email is exist")
	private String email;
	private String password;
	private Set<String> roles;
}
