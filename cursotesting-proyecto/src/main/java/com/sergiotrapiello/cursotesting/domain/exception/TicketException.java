package com.sergiotrapiello.cursotesting.domain.exception;

public class TicketException extends Exception {

	private static final long serialVersionUID = 1L;
	private Integer ticketId;
	private String msg;
	
	
	public TicketException(Integer ticketId, String msg) {
		this.ticketId = ticketId;
		this.msg = msg;
	}


	public Integer getTicketNumber() {		
//		return this.ticketId;
		return this.ticketId;
	}


	@Override
	public String toString() {
		return msg;
	}
	
	

}
