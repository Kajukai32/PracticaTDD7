package com.sergiotrapiello.cursotesting.application.ui;

public final class ResponseEntity {

	public enum Status {
		OK, ERROR
	}

	private Status status;

	private Object body;

	public ResponseEntity(Status status, Object body) {
		this.status = status;
		this.body = body;
	}

	public Status getStatus() {
		return status;
	}

	public Object getBody() {
		return body;
	}

	public static ResponseEntity ok(Object body) {
		return new ResponseEntity(Status.OK, body);
	}

	public static ResponseEntity error(String errorMsg) {
		return new ResponseEntity(Status.ERROR, errorMsg);
	}

	public boolean isOk() {
		return Status.OK == status;
	}
}
