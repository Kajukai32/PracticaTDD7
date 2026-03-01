package com.sergiotrapiello.cursotesting.application.ui;

public final class RequestDispatcherException extends RuntimeException {

	private static final long serialVersionUID = 6917309544219292666L;

	public RequestDispatcherException(Throwable cause) {
		super(cause);
	}

	public RequestDispatcherException(String message) {
		super(message);
	}

}
