package de.unifrankfurt.informatik.acoli.starling.types;

/**
 * Exception thrown when a Starling language identifier could not be mapped to ISO format.
 * The ISO-639 mapping is defined in the class starling.Utils .
 * @author frank
 */
public class LanguageCodeNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LanguageCodeNotFoundException(String language) {
		System.out.println("Language code for "+language+" not found in HashMap isoLanguage !");
	}
}
