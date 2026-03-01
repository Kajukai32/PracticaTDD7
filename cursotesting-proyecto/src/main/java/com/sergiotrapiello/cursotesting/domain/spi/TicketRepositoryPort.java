package com.sergiotrapiello.cursotesting.domain.spi;

import com.sergiotrapiello.cursotesting.domain.model.Ticket;

public interface TicketRepositoryPort {

	public Ticket save(Ticket ticketSinElID);
}
