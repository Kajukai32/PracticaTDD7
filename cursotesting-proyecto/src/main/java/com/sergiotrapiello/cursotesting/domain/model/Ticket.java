package com.sergiotrapiello.cursotesting.domain.model;

import java.time.Clock;
import java.time.LocalDateTime;


public class Ticket {

	private Integer id;
	private LocalDateTime fechaYHoraDeEmision;

	public Ticket(Clock clock) {

		this.fechaYHoraDeEmision = LocalDateTime.now(clock);
	
	}


	public LocalDateTime getFechaYHoraDeEmision() {

		return this.fechaYHoraDeEmision;
	}

	public Integer getId() {

		return this.id;
	}


	public void setId(Integer id) {
		this.id = id;
	}

}
