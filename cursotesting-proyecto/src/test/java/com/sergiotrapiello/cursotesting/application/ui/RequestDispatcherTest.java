package com.sergiotrapiello.cursotesting.application.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.sergiotrapiello.cursotesting.application.controller.Controller;

class RequestDispatcherTest {

	private RequestDispatcher dispatcher;
	
	@BeforeEach
	void setup() {
		dispatcher = new RequestDispatcher(Set.of(new FooController()));
	}
	
	@Test
	void shouldDispatch_noArgs() {

		// GIVEN
		String path = "path1";

		// WHEN
		ResponseEntity response = dispatcher.doDispatch(path);

		// THEN
		assertNotNull(response);
		assertEquals("No args", response.getBody());
	}

	@Test
	void shouldDispatch_args() {

		// GIVEN
		String path = "path2";
		String arg1 = "Patatas: ";
		int arg2 = 3;

		// WHEN
		ResponseEntity response = dispatcher.doDispatch(path, arg1, arg2);

		// THEN
		assertNotNull(response);
		assertEquals("Patatas: 3", response.getBody());
	}

	@Test
	void shouldFailDispatching_noMappingFound() {

		// GIVEN
		String path = "unknownPath";
		
		RequestDispatcher dispatcher = new RequestDispatcher(Set.of());
		
		// WHEN
		Executable executable = () -> dispatcher.doDispatch(path);		
		
		// THEN
		RequestDispatcherException e = assertThrows(RequestDispatcherException.class, executable);
		assertEquals("No controller destination configured for path: " + path, e.getMessage());
	}
	
	@Test
	void shouldFailDispatching_mappingInUnregisteredController() {

		// GIVEN
		String path = "path3";
		
		// WHEN
		Executable executable = () -> dispatcher.doDispatch(path);		
		
		// THEN
		RequestDispatcherException e = assertThrows(RequestDispatcherException.class, executable);
		assertEquals("No controller destination configured for path: " + path, e.getMessage());
	}
}

class FooController implements Controller {

	@RequestMapping("path1")
	public ResponseEntity method1() {
		return ResponseEntity.ok("No args");
	}

	@RequestMapping("path2")
	public ResponseEntity method2(String param1, int param2) {
		return ResponseEntity.ok(param1 + param2);
	}
}

class BarController implements Controller {

	@RequestMapping("path3")
	public ResponseEntity method() {
		throw new UnsupportedOperationException("This method should not be called. If called, there is a bug");
	}

}
