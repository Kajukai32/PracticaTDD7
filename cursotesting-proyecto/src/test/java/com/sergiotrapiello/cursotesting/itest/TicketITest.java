package com.sergiotrapiello.cursotesting.itest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

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
import com.sergiotrapiello.cursotesting.domain.model.Ticket;
import com.sergiotrapiello.cursotesting.infrastructure.jdbc.TicketRepositoryPortImpl;
import com.sergiotrapiello.cursotesting.utils.TestUtils;


@ExtendWith(DBUnitExtension.class)
@DataSet("tickets.yml")
class TicketITest {

	private Clock clock;

	private Connection connJdbc;
	@SuppressWarnings("unused")
	private ConnectionHolder connectionHolder;

	@BeforeEach
	void setUp() throws Exception {

		String isoInstant = "2026-02-28T13:07:00.00Z";
		clock = TestUtils.clock(isoInstant);
		connJdbc = DriverManager.getConnection("jdbc:h2:mem:test;INIT=runscript from 'classpath:schema.sql'", "sa", "");
		connectionHolder = new ConnectionHolderImpl(connJdbc);
	}

	@Test
	@ExpectedDataSet(value = "ticket_expected_after_issue.yml", ignoreCols = "id")
	void shouldIssueTest() {

		// GIVEN
		TicketRepositoryPortImpl ticketRepositoryPort = new TicketRepositoryPortImpl(connJdbc);
		TicketService ticketService = new TicketServiceImpl(clock, ticketRepositoryPort);
		TicketController registeredController = new TicketController(ticketService);
		RequestDispatcher dispatcher = new RequestDispatcher(Set.of(registeredController));

		// WHEN
		ResponseEntity responseEntity = dispatcher.doDispatch(Paths.Ticket.ISSUE);

		// THEN
		assertNotNull(responseEntity);
		assertEquals(Status.OK, responseEntity.getStatus());
		Ticket responseTicket = (Ticket) responseEntity.getBody();
		assertNotNull(responseTicket.getFechaYHoraDeEmision());
		assertEquals(LocalDateTime.now(clock), responseTicket.getFechaYHoraDeEmision());
		assertNotNull(responseTicket.getId());
		assertEquals(1,responseTicket.getId());


	}


}
