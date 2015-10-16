package com.bankonet.metier.utils.exception;

public class CompteException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public CompteException() {
		super();
	}

	public CompteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CompteException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompteException(String message) {
		super(message);
	}

	public CompteException(Throwable cause) {
		super(cause);
	}	
	

}