package com.sergiotrapiello.cursotesting.boot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Clock;
import java.util.Set;

import com.sergiotrapiello.cursotesting.application.controller.Controller;
import com.sergiotrapiello.cursotesting.application.controller.TicketController;
import com.sergiotrapiello.cursotesting.application.ui.ConsoleUI;
import com.sergiotrapiello.cursotesting.application.ui.RequestDispatcher;
import com.sergiotrapiello.cursotesting.domain.api.TicketServiceImpl;
import com.sergiotrapiello.cursotesting.domain.spi.TicketRepositoryPort;
import com.sergiotrapiello.cursotesting.infrastructure.jdbc.TicketRepositoryPortImpl;

public class CursotestingProyectoBootApplication {

	public static void main(String[] args) throws SQLException {

		Connection connJbdc = DriverManager.getConnection("jdbc:h2:mem:test;INIT=runscript from 'classpath:schema.sql'",
				"sa", "");
		TicketRepositoryPort ticketRepositoryImpl = new TicketRepositoryPortImpl(connJbdc);
		TicketServiceImpl ticketServiceImpl = new TicketServiceImpl(Clock.systemDefaultZone(), 0.34, 30.0,
				ticketRepositoryImpl);
		TicketController ticketController = new TicketController(ticketServiceImpl);

		Set<Controller> controllers = Set.of(ticketController);

		RequestDispatcher dispatcher = new RequestDispatcher(controllers);
		new ConsoleUI(dispatcher).run();
	}

}
