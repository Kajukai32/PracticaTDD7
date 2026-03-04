package com.sergiotrapiello.cursotesting.application.controller;

import com.sergiotrapiello.cursotesting.application.ui.Paths;
import com.sergiotrapiello.cursotesting.application.ui.RequestMapping;
import com.sergiotrapiello.cursotesting.application.ui.ResponseEntity;
import com.sergiotrapiello.cursotesting.domain.api.TicketService;
import com.sergiotrapiello.cursotesting.domain.exception.TicketException;

public final class TicketController implements Controller {

	private TicketService ticketService;


	public TicketController(TicketService ticketService) {

		this.ticketService = ticketService;
	}

	@RequestMapping(Paths.Ticket.ISSUE)
	public ResponseEntity issueTicket() {

		return ResponseEntity.ok(ticketService.issueTicket());
	}

	@RequestMapping(Paths.Ticket.CALCULATE_AMOUNT)
	public ResponseEntity calculateAmount(int nroDeTicket) throws TicketException {
		try {
			return ResponseEntity.ok(ticketService.calculateAmount(nroDeTicket));

		} catch (TicketException e) {
			return ResponseEntity.error("non registered ticket for: " + e.getTicketNumber());
		}
	}
}
