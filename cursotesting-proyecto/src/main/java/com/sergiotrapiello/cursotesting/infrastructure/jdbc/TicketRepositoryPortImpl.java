package com.sergiotrapiello.cursotesting.infrastructure.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sergiotrapiello.cursotesting.domain.model.Ticket;
import com.sergiotrapiello.cursotesting.domain.spi.TicketRepositoryPort;

public class TicketRepositoryPortImpl implements TicketRepositoryPort {

	private Connection connJdbc;

	public TicketRepositoryPortImpl(Connection connJdbc) {

		this.connJdbc = connJdbc;

	}

	@Override
	public Ticket save(Ticket ticketSinElID) {

		String query = "INSERT INTO TICKET (ISSUED_DATETIME) VALUES (?)";

		try (PreparedStatement statement = connJdbc.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

			statement.setObject(1, ticketSinElID.getFechaYHoraDeEmision());

			statement.executeUpdate();

			int generatedId = getGeneratedId(statement);
			ticketSinElID.setId(generatedId);


		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return ticketSinElID;


	}

	private int getGeneratedId(PreparedStatement statement) throws SQLException {
		ResultSet generatedKeys = statement.getGeneratedKeys();

		generatedKeys.next();

		return generatedKeys.getInt(1);

	}

}
