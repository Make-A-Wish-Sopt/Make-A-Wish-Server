package com.sopterm.makeawish.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class ErrorHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
		ApiResponse response = ApiResponse.fail(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse> handleIllegalArgumentExceptionException(IllegalArgumentException exception) {
		ApiResponse response = ApiResponse.fail(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<ApiResponse> handleHttpClientErrorExceptionException(HttpClientErrorException exception) {
		ApiResponse response = ApiResponse.fail(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
