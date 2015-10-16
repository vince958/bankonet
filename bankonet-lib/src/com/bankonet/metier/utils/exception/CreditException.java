package com.bankonet.metier.utils.exception;

public class CreditException extends CompteException{
	
	private static final long serialVersionUID = 1L;

	public CreditException() {
		super();
	}

	public CreditException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CreditException(String message, Throwable cause) {
		super(message, cause);
	}

	public CreditException(String message) {
		super(message);
	}

	public CreditException(Throwable cause) {
		super(cause);
	}
}
