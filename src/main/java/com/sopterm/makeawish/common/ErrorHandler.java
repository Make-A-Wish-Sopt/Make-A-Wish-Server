package com.sopterm.makeawish.common;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sopterm.makeawish.exception.WrongAccessTokenException;
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

	@ExceptionHandler(JsonProcessingException.class)
	public ResponseEntity<ApiResponse> jsonProcessingException() {
		ApiResponse response = ApiResponse.fail(CODE_PARSE_ERROR.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(WrongAccessTokenException.class)
	public ResponseEntity<ApiResponse> wrongAccessTokenException(WrongAccessTokenException exception) {
		ApiResponse response = ApiResponse.fail(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> allException() {
		ApiResponse response = ApiResponse.fail(SERVER_INTERNAL_ERROR.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
