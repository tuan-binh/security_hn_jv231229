package com.ra.controller;

import com.ra.constants.EHttpStatus;
import com.ra.dto.request.FormLogin;
import com.ra.dto.request.FormRegister;
import com.ra.dto.response.ResponseWrapper;
import com.ra.exception.CustomException;
import com.ra.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	private final IAuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<?> handleLogin(@Valid @RequestBody FormLogin formLogin) throws CustomException {
		return new ResponseEntity<>(
				  ResponseWrapper.builder()
							 .eHttpStatus(EHttpStatus.SUCCESS)
							 .statusCode(HttpStatus.OK.value())
							 .data(authService.handleLogin(formLogin))
							 .build(),
				  HttpStatus.OK
		);
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> handleRegister(@Valid @RequestBody FormRegister formRegister) {
		authService.handleRegister(formRegister);
		return new ResponseEntity<>(
				  ResponseWrapper.builder()
							 .eHttpStatus(EHttpStatus.SUCCESS)
							 .statusCode(HttpStatus.CREATED.value())
							 .data("Register successfully")
							 .build(),
				  HttpStatus.CREATED);
	}
	
}
