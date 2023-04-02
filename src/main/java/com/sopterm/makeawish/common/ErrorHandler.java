package com.sopterm.makeawish.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ErrorHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
		ApiResponse response = ApiResponse.fail(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
}
