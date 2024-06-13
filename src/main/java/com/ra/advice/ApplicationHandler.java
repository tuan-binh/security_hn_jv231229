package com.ra.advice;

import com.ra.constants.EHttpStatus;
import com.ra.dto.response.ResponseWrapper;
import com.ra.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidException(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
		return ResponseEntity.badRequest().body(
				  ResponseWrapper.builder()
							 .eHttpStatus(EHttpStatus.FAILED)
							 .statusCode(HttpStatus.BAD_REQUEST.value())
							 .data(errors)
							 .build()
		);
	}
	
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?> handleCustomException(CustomException ex) {
		return new ResponseEntity<>(
				  ResponseWrapper.builder()
							 .eHttpStatus(EHttpStatus.FAILED)
							 .statusCode(ex.getHttpStatus().value())
							 .data(ex.getMessage())
							 .build(),
				  ex.getHttpStatus()
		);
	}
	
}
