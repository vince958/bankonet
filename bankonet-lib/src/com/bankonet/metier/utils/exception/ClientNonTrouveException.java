package com.bankonet.metier.utils.exception;

public class ClientNonTrouveException extends CompteException {
	
	private static final long serialVersionUID = 1L;
	
	public ClientNonTrouveException() {
		super();
	}

	public ClientNonTrouveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ClientNonTrouveException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientNonTrouveException(String message) {
		super(message);
	}

	public ClientNonTrouveException(Throwable cause) {
		super(cause);
	}
}
