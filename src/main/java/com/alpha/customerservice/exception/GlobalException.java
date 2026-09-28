package com.alpha.customerservice.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException extends RuntimeException {

	
	@ExceptionHandler(CustomerNotFoundException.class)
	public void customerNotFoundException()
	{
		System.err.println("Customer not found");
	}
}
