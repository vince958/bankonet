package com.bankonet.metier.utils.exception;

public class DebitException extends CompteException {

	private static final long serialVersionUID = 1L;

	public DebitException(String message, Throwable cause) {
		super(message, cause);
	}

	public DebitException(String message) {
		super(message);
	}

	public DebitException(Throwable cause) {
		super(cause);
	}
}