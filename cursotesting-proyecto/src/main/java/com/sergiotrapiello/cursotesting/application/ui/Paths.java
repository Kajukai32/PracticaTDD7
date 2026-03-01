package com.sergiotrapiello.cursotesting.application.ui;

/**
 * Define paths de funcionalidades de la aplicación.
 * 
 * <p>
 * Se organiza en subclases por bloques funcionales
 * </p>
 */
public final class Paths {

	private Paths() {
		// private constructor to not allow instances
	}

	/**
	 * Paths relativos a funcionalidades de gestión de Ticket
	 */
	public final class Ticket {
		public static final String ISSUE = "ticket/issue";
		public static final String CALCULATE_AMOUNT = "ticket/calculateAmount";

		private Ticket() {
			// private constructor to not allow instances
		}
	}

}
