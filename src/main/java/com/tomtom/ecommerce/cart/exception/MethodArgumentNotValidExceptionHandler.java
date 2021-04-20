package com.tomtom.ecommerce.cart.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodArgumentNotValidExceptionHandler.class);
	
	@ResponseStatus(BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Error methodArgumentNotValidException(MethodArgumentNotValidException ex) {
		LOGGER.info("Invalid request recieved", ex);
		BindingResult result = ex.getBindingResult();
		List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();
		return processFieldErrors(fieldErrors);
	}

	private Error processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
		Error error = new Error(BAD_REQUEST.value(), "validation error");
		for (org.springframework.validation.FieldError fieldError: fieldErrors) {
			error.addFieldError(fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
		}
		return error;
	}

	static class Error {
		private final int status;
		private final String message;
		private List<FieldError> fieldErrors = new ArrayList<>();

		Error(int status, String message) {
			this.status = status;
			this.message = message;
		}

		public int getStatus() {
			return status;
		}

		public String getMessage() {
			return message;
		}

		public void addFieldError(String path, String field, String message) {
			FieldError error = new FieldError(path,field, message);
			fieldErrors.add(error);
		}

		public List<FieldError> getFieldErrors() {
			return fieldErrors;
		}
	}
}
