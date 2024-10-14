package com.sample.holiday.exception;

public class HolidayApiException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HolidayApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
