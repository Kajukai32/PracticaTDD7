package com.sergiotrapiello.cursotesting.application.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.sergiotrapiello.cursotesting.application.controller.Controller;

/**
 * Se encarga de despachar peticiones a los {@link Controller} que tenga
 * configurados
 */
public final class RequestDispatcher {

	private Set<Controller> registeredControllers;

	public RequestDispatcher(Set<Controller> controllers) {
		this.registeredControllers = controllers;
	}

	/**
	 * Despacha la petición usando el path y los argumentos que recibe
	 * 
	 * @param path del controller al que debe despachar la petición
	 * @param args argumentos para pasarle en la llamada
	 * @return {@link ResponseEntity} devuelta por el controller
	 */
	public ResponseEntity doDispatch(String path, Object... args) {
		try {
			for (Controller controller : registeredControllers) {
				Optional<Method> pathMethod = findMethod(path, controller);
				if (pathMethod.isPresent()) {
					return (ResponseEntity) pathMethod.get().invoke(controller, args);
				}
			}
			throw new RequestDispatcherException("No controller destination configured for path: " + path);

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RequestDispatcherException(e);
		}
	}

	private Optional<Method> findMethod(String path, Controller controller) {
		Method[] methods = controller.getClass().getDeclaredMethods();
		return Stream.of(methods)
				.filter(method -> path.equalsIgnoreCase(method.getAnnotation(RequestMapping.class).value()))
				.findFirst();
	}

}
