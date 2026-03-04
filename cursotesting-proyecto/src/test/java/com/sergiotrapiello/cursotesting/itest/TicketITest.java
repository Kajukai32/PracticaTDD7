package com.sergiotrapiello.cursotesting.itest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.core.connection.ConnectionHolderImpl;
import com.github.database.rider.junit5.DBUnitExtension;
import com.sergiotrapiello.cursotesting.application.controller.TicketController;
import com.sergiotrapiello.cursotesting.application.ui.Paths;
import com.sergiotrapiello.cursotesting.application.ui.RequestDispatcher;
import com.sergiotrapiello.cursotesting.application.ui.ResponseEntity;
import com.sergiotrapiello.cursotesting.application.ui.ResponseEntity.Status;
import com.sergiotrapiello.cursotesting.domain.api.TicketService;
import com.sergiotrapiello.cursotesting.domain.api.TicketServiceImpl;
import com.sergiotrapiello.cursotesting.domain.exception.TicketException;
import com.sergiotrapiello.cursotesting.domain.model.Ticket;
import com.sergiotrapiello.cursotesting.infrastructure.jdbc.TicketRepositoryPortImpl;
import com.sergiotrapiello.cursotesting.utils.TestUtils;


@ExtendWith(DBUnitExtension.class)
@DataSet("tickets.yml")
class TicketITest {

	private Clock clock;
	private Double dailyMaximum;
	private Double pricePerMinute;
	private Connection connJdbc;
	@SuppressWarnings("unused")
	private ConnectionHolder connectionHolder;
	private RequestDispatcher dispatcher;
	private TicketController registeredController;
	private TicketRepositoryPortImpl ticketRepositoryPort;

	@BeforeEach
	void setUp() throws Exception {

		pricePerMinute = 0.022;
		dailyMaximum = 32.0;
		String isoInstant = "2026-02-28T13:00:00.00Z";
		clock = TestUtils.clock(isoInstant);
		connJdbc = DriverManager.getConnection("jdbc:h2:mem:test;INIT=runscript from 'classpath:schema.sql'", "sa", "");
		connectionHolder = new ConnectionHolderImpl(connJdbc);
		ticketRepositoryPort = new TicketRepositoryPortImpl(connJdbc);
		TicketService ticketService = new TicketServiceImpl(clock, pricePerMinute, dailyMaximum, ticketRepositoryPort);
		registeredController = new TicketController(ticketService);
		dispatcher = new RequestDispatcher(Set.of(registeredController));


	}

	@Test
	void shouldFailIfTicketInexistent() {
		// GIVEN
		int nonRegisteredTicket = 13;
		// WHEN

		ResponseEntity responseEntity = dispatcher.doDispatch(Paths.Ticket.CALCULATE_AMOUNT, nonRegisteredTicket);
		// THEN

		assertNotNull(responseEntity);
		assertEquals(Status.ERROR, responseEntity.getStatus());
		assertEquals("non registered ticket for: " + nonRegisteredTicket, responseEntity.getBody());
	}


	@Test
	void shouldCalculateAmountToPay() {
		// GIVEN
		int nroDeTicket = 101;

		Double expectedAmount = (pricePerMinute * 14);
		// WHEN
		ResponseEntity responseEntity = dispatcher.doDispatch(Paths.Ticket.CALCULATE_AMOUNT, nroDeTicket);

		// THEN

		assertNotNull(responseEntity);
		assertEquals(Status.OK, responseEntity.getStatus());
		assertNotNull(responseEntity.getBody());
		assertEquals(expectedAmount, responseEntity.getBody());

	}

	@Test
	@ExpectedDataSet(value = "ticket_expected_after_issue.yml", ignoreCols = "id")
	void shouldIssueTest() {

		// GIVEN
		TicketRepositoryPortImpl ticketRepositoryPort = new TicketRepositoryPortImpl(connJdbc);
		TicketService ticketService = new TicketServiceImpl(clock, 0.033, 25.0, ticketRepositoryPort);
		TicketController registeredController = new TicketController(ticketService);
		RequestDispatcher dispatcher = new RequestDispatcher(Set.of(registeredController));

		// WHEN
		ResponseEntity responseEntity = dispatcher.doDispatch(Paths.Ticket.ISSUE);

		// THEN
		assertNotNull(responseEntity);
		assertEquals(Status.OK, responseEntity.getStatus());
		Ticket responseTicket = (Ticket) responseEntity.getBody();
		assertNotNull(responseTicket);
		assertEquals(LocalDateTime.now(clock), responseTicket.getFechaYHoraDeEmision());
		assertNotNull(responseTicket.getId());

	}




}










