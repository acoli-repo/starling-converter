package de.unifrankfurt.informatik.acoli.starling.parser;

/**
 * Exception thrown when parser fails on data in XML language element
 * @author frank
 */
public class StarlingParseException extends Exception {

	private static final long serialVersionUID = 1L;

	public StarlingParseException(Integer recordNr, String language, String elementValue) {
		System.out.println("Error parsing record "+recordNr);
		System.out.println(language);
		System.out.println("XML value "+elementValue);
	}
}
