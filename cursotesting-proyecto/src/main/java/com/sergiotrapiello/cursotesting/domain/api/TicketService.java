package com.sergiotrapiello.cursotesting.domain.api;

import com.sergiotrapiello.cursotesting.domain.exception.TicketException;
import com.sergiotrapiello.cursotesting.domain.model.Ticket;

public interface TicketService {

	 public Ticket issueTicket();

	 public Double calculateAmount(Integer unexistentId) throws TicketException;
}
