package com.bankonet.exception;

public class CompteNonTrouveException extends CompteException {
	
	private static final long serialVersionUID = 1L;
	
	public CompteNonTrouveException() {
		super();
	}

	public CompteNonTrouveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CompteNonTrouveException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompteNonTrouveException(String message) {
		super(message);
	}

	public CompteNonTrouveException(Throwable cause) {
		super(cause);
	}
}
