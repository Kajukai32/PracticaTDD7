package com.sergiotrapiello.cursotesting.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Clock;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sergiotrapiello.cursotesting.utils.TestUtils;

class TicketTest {
	private Clock clock;

	@BeforeEach
	void setUp() throws Exception {
		String isoInstant = "2026-02-28T13:07:00.00Z";
		clock = TestUtils.clock(isoInstant);
	}


	@Test
	void shouldCreate() {
		// GIVEN
		// WHEN
		Ticket ticket = new Ticket(clock);

		// THEN

		assertEquals(LocalDateTime.now(clock), ticket.getFechaYHoraDeEmision());
		assertNull(ticket.getId());
	}


}
