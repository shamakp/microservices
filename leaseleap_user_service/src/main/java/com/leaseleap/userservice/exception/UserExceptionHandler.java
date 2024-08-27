package com.leaseleap.userservice.exception;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.leaseleap.userservice.model.ErrorMessage;


@ControllerAdvice
public class UserExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorMessage> handleNotValidExeption(MethodArgumentNotValidException e) {
		var errors = e.getAllErrors();
		if (Objects.nonNull(errors)) {
			ObjectError error = errors.stream().findFirst().orElseThrow();
			return new ResponseEntity<ErrorMessage>(
					new ErrorMessage(400, error.getDefaultMessage()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<ErrorMessage>(
				new ErrorMessage(400, "Bad Request"), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ErrorMessage> handleNotFoundException(NoSuchElementException e) {
		return new ResponseEntity<ErrorMessage>(
				new ErrorMessage(404, e.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({
		IllegalArgumentException.class,
		HttpMessageNotReadableException.class,
		HttpRequestMethodNotSupportedException.class	
	})
	public ResponseEntity<ErrorMessage> handleIllegalException(Exception e) {
		return new ResponseEntity<ErrorMessage>(
				new ErrorMessage(400, e.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleUsernameNotFoundException(UsernameNotFoundException e) {
		return new ResponseEntity<ErrorMessage>(
				new ErrorMessage(404, e.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(WrongPasswordException.class)
	public ResponseEntity<ErrorMessage> handleWrongPasswordException(WrongPasswordException e) {
		return new ResponseEntity<ErrorMessage>(
				new ErrorMessage(403, e.getMessage()), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(UserCreationException.class)
	public ResponseEntity<ErrorMessage> handleUserCreationException(UserCreationException e) {
		return new ResponseEntity<ErrorMessage>(
				new ErrorMessage(400, e.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException e) {
		return new ResponseEntity<ErrorMessage>(
				new ErrorMessage(404, e.getMessage()), HttpStatus.NOT_FOUND);
	}

}
