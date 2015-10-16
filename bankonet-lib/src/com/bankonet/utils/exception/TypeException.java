package com.bankonet.utils.exception;

public class TypeException extends CompteException {

	private static final long serialVersionUID = 1L;

	public TypeException() {
		super();
	}

	public TypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public TypeException(String message) {
		super(message);
	}

	public TypeException(Throwable cause) {
		super(cause);
	}
}