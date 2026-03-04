package com.sergiotrapiello.cursotesting.domain.spi;

import java.util.Optional;

import com.sergiotrapiello.cursotesting.domain.model.Ticket;

public interface TicketRepositoryPort {

	public Ticket save(Ticket ticketSinElID);

	public Optional<Ticket> getTicketById(Integer id);
}
