package com.sergiotrapiello.cursotesting.domain.api;

import static com.sergiotrapiello.cursotesting.utils.TestUtils.clock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.time.Clock;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.sergiotrapiello.cursotesting.domain.exception.TicketException;
import com.sergiotrapiello.cursotesting.domain.model.Ticket;
import com.sergiotrapiello.cursotesting.domain.spi.TicketRepositoryPort;
import com.sergiotrapiello.cursotesting.infrastructure.jdbc.TicketRepositoryPortImpl;


class TicketServiceImplTest {

	private Clock clock;
	private String isoInstant;
	private TicketRepositoryPort ticketRepositoryPortMock;
	private Connection connJdbc;
	private TicketService ticketService;
	private Double pricePerMinute;
	private Double dailyMaximum;

	@BeforeEach
	void setUp() throws Exception {

		dailyMaximum = 25.00;
		pricePerMinute = 0.033;
		isoInstant = "2026-02-28T13:07:00.00Z";
		ticketRepositoryPortMock = mock(TicketRepositoryPortImpl.class);
		ticketService = new TicketServiceImpl(clock(isoInstant), pricePerMinute, dailyMaximum,
				ticketRepositoryPortMock);

	}

	// Ticket emitido a las 21:15 del 10/09, y son las 8:27 del 12/09. Precio
	// 0,033€/minuto. Máximo diario 25€ → Devuelve un importe a pagar de 47,18€ (son
	// 35h y 12 min en total. 25€ del tope por las primeras 24h y 22,18€ el resto)
	// Ticket emitido a las 23:30 y son las 0:30 del día siguiente. Precio
	// 0,033€/minuto → Devuelve un importe a pagar de 1,98€ (60 minutos)
	// Ticket emitido a las 9:15 y son las 22:27. Precio 0,033€/minuto. Máximo
	// diario 25€ → Devuelve un importe a pagar de 25€
	// (son 792 minutos * 0.033 = 26,14€, que supera el máximo diario)
	// Ticket emitido a las 14:30 y son las 16:32. Precio 0,033€/minuto → Devuelve
	// un importe a pagar de 4,03€ (122 mins * 0.033)
	@ParameterizedTest
	@MethodSource("shouldCalculateAmountArguments")
	void shouldCalculateAmount(Clock clockIssuedTicket, Clock clockCollectTicket, Double expectedAmount)
			throws TicketException {
		// GIVEN
		Integer existentId = 11;

		ticketService = new TicketServiceImpl(clockCollectTicket, pricePerMinute, dailyMaximum,
				ticketRepositoryPortMock);

		when(ticketRepositoryPortMock.getTicketById(existentId)).thenReturn(Optional.of(new Ticket(clockIssuedTicket)));

		// WHEN

		Double amountToPay = ticketService.calculateAmount(existentId);

		// THEN

		assertEquals(expectedAmount, amountToPay, 0.01);
	}

	// Recibe un nº de ticket inexistente → Error, ticket no encontrado
	@Test
	void shouldThrowExceptionIfUnexistentTicket() {
		// GIVEN
		Integer unexistentId = 18;

		when(ticketRepositoryPortMock.getTicketById(anyInt())).thenReturn(Optional.empty());
		// WHEN

		Executable e = () -> {
			ticketService.calculateAmount(unexistentId);
		};
		// THEN
		TicketException te = assertThrows(TicketException.class, e);

		assertEquals(unexistentId, te.getTicketNumber());
		System.out.println(te.toString());

	}

	private static Stream<Arguments> shouldCalculateAmountArguments() {

		return Stream.of(Arguments.of(clock("2023-09-19T14:30:15.00Z"), clock("2023-09-19T16:32:15.00Z"), 4.03),
				Arguments.of(clock("2023-09-19T09:15:15.00Z"), clock("2023-09-19T22:27:15.00Z"), 25.0),
				Arguments.of(clock("2023-09-19T23:30:15.00Z"), clock("2023-09-20T00:30:15.00Z"), 1.98),
				Arguments.of(clock("2023-09-10T21:15:15.00Z"), clock("2023-09-12T08:27:15.00Z"), 47.18),
				Arguments.of(clock("2023-09-10T09:00:00.00Z"), clock("2023-09-12T15:00:00.00Z"), 25.0 + 25.0 + (360.0 * 0.033)));
	}
}












