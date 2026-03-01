package com.sergiotrapiello.cursotesting.domain.api;

import java.time.Clock;

import com.sergiotrapiello.cursotesting.domain.model.Ticket;
import com.sergiotrapiello.cursotesting.domain.spi.TicketRepositoryPort;

public class TicketServiceImpl implements TicketService {

	private Clock clock;

	private final TicketRepositoryPort ticketRepositoryPort;

	public TicketServiceImpl(Clock clock2, TicketRepositoryPort ticketRepositoryPort) {
		this.clock = clock2;
		this.ticketRepositoryPort = ticketRepositoryPort;
	}

	@Override
	public Ticket issueTicket() {

		return ticketRepositoryPort.save(new Ticket(clock));

	}

}
