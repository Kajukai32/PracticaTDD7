package com.sergiotrapiello.cursotesting.application.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.sergiotrapiello.cursotesting.application.ui.ResponseEntity.Status;

class ResponseEntityTest {

	@Test
	void shouldCreateOk_bodyEntity() {

		// GIVEN
		Foo entity = new Foo("patata");

		// WHEN
		ResponseEntity responseEntity = ResponseEntity.ok(entity);

		// THEN
		assertNotNull(responseEntity);
		assertEquals(Status.OK, responseEntity.getStatus());
		assertEquals(entity, responseEntity.getBody());
	}

	@Test
	void shouldCreateError() {

		// GIVEN
		String errorMsg = "This is an error message";

		// WHEN
		ResponseEntity responseEntity = ResponseEntity.error(errorMsg);

		// THEN
		assertNotNull(responseEntity);
		assertEquals(Status.ERROR, responseEntity.getStatus());
		assertEquals(errorMsg, responseEntity.getBody());
	}

}

class Foo {

	String bar;

	public Foo(String bar) {
		this.bar = bar;
	}

}