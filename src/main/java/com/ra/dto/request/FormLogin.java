package com.ra.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormLogin {
//	@NotNull(message = "email must be not null")
	@NotEmpty(message = "email must be not empty")
	@NotBlank(message = "email must be not blank")
	private String email;
//	@NotNull(message = "password must be not null")
	@NotEmpty(message = "password must be not empty")
	@NotBlank(message = "password must be not blank")
	private String password;
	
}
