package com.sopterm.makeawish.common;

import static com.sopterm.makeawish.common.message.ErrorMessage.*;

import java.nio.file.AccessDeniedException;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.popbill.api.PopbillException;
import com.popbill.api.Response;
import com.sopterm.makeawish.exception.WrongAccessTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.client.HttpClientErrorException;

import lombok.val;

@RestControllerAdvice
public class ErrorHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
		val response = ApiResponse.fail(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse> handleIllegalArgumentExceptionException(IllegalArgumentException exception) {
		val response = ApiResponse.fail(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<ApiResponse> handleHttpClientErrorExceptionException(HttpClientErrorException exception) {
		val response = ApiResponse.fail(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(JsonProcessingException.class)
	public ResponseEntity<ApiResponse> jsonProcessingException(JsonProcessingException exception) {
		val response = ApiResponse.fail(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(WrongAccessTokenException.class)
	public ResponseEntity<ApiResponse> wrongAccessTokenException(WrongAccessTokenException exception) {
		val response = ApiResponse.fail(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiResponse> httpRequestMethodNotSupportedException() {
		val response = ApiResponse.fail(INVALID_HTTP_REQUEST.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiResponse> accessDeniedException(AccessDeniedException exception) {
		val response = ApiResponse.fail(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(DateTimeParseException.class)
	public ResponseEntity<ApiResponse> dateTimeParseException() {
		val response = ApiResponse.fail(FAULT_DATE_FORMATTER.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ApiResponse> illegalStateException(IllegalStateException exception) {
		val response = ApiResponse.fail(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PopbillException.class)
	public ResponseEntity<ApiResponse> popbillException(PopbillException exception){
		val response = ApiResponse.fail(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(AbuseException.class)
	public ResponseEntity<ApiResponse> abuseException(AbuseException exception){
		val response = ApiResponse.fail(exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
