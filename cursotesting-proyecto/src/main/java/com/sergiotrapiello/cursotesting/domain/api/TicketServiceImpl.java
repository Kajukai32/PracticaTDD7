package com.sergiotrapiello.cursotesting.domain.api;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import com.sergiotrapiello.cursotesting.domain.exception.TicketException;
import com.sergiotrapiello.cursotesting.domain.model.Ticket;
import com.sergiotrapiello.cursotesting.domain.spi.TicketRepositoryPort;

public class TicketServiceImpl implements TicketService {

	private static final int MINUTES_PER_DAY = 1440;
	private Clock clock;
	private Double pricePerMinute;
	private Double dailyMax;

	private final TicketRepositoryPort ticketRepositoryPort;

	public TicketServiceImpl(Clock clock, Double pricePerMinute, Double dailyMaximum,
			TicketRepositoryPort ticketRepositoryPort) {
		this.clock = clock;
		this.pricePerMinute = pricePerMinute;
		this.ticketRepositoryPort = ticketRepositoryPort;
		this.dailyMax = dailyMaximum;
	}

	@Override
	public Ticket issueTicket() {

		return ticketRepositoryPort.save(new Ticket(clock));

	}

	@Override
	public Double calculateAmount(Integer ticketId) throws TicketException {

		Optional<Ticket> ticketById = ticketRepositoryPort.getTicketById(ticketId);
//	
//		if(!ticketById.isPresent()) {
//			throw new TicketException(ticketId, "non registered ticked id");
//			}
//		return ticketById.get()
//		
		ticketById.orElseThrow(() -> new TicketException(ticketId, "non registered ticked for: " + ticketId));

		LocalDateTime collectInstant = LocalDateTime.now(clock);
		long totalMinutes = ChronoUnit.MINUTES.between(ticketById.get().getFechaYHoraDeEmision(), collectInstant);

		double fulldaysAmount = calculateFullDaysAmount(totalMinutes);

		double remainderMinutesAmount = calculateRemainderAmount(totalMinutes);

		return fulldaysAmount + remainderMinutesAmount;

	}

	private double calculateRemainderAmount(long totalMinutes) {

		double remainderMinutes = totalMinutes % MINUTES_PER_DAY;

		double remainderMinutesAmount = remainderMinutes * pricePerMinute;

		if (remainderMinutesAmount > dailyMax) {
			remainderMinutesAmount = dailyMax;
		}

		return remainderMinutesAmount;
	}

	private double calculateFullDaysAmount(long totalMinutes) {
		return totalMinutes / MINUTES_PER_DAY * dailyMax;
	}



}












