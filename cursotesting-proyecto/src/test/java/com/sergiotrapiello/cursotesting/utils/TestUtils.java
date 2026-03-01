package com.sergiotrapiello.cursotesting.utils;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class TestUtils {

	private TestUtils() {
		// es una clase de utilidades no queremos que haya instancias
	}


	public static Clock clock(String isoInstant) {

		return Clock.fixed(Instant.parse(isoInstant), ZoneId.systemDefault());
		
	}
}
