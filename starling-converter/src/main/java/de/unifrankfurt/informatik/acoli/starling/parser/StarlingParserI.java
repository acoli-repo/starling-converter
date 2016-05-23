package de.unifrankfurt.informatik.acoli.starling.parser;

import java.util.ArrayList;
import java.util.HashMap;

import de.unifrankfurt.informatik.acoli.starling.types.StarlingMeaning;
import de.unifrankfurt.informatik.acoli.starling.types.StarlingReference;
import de.unifrankfurt.informatik.acoli.starling.types.StarlingWord;

/**
 * Implement methods to parse complex information stored in a single string 
 * in XML fields of a record in a Starling XML dictionary.
 * @author frank
 */
public interface StarlingParserI {

	/**
	 * Parse XML language element into StarlingWord objects. 
	 * @param s XML element value for language
	 * @return List with StarlingWord objects
	 */
	public ArrayList <StarlingWord> parseLanguageElement (String s);
	
	/** 
	 * Parse XML MEANING element which may contain several meanings optionally
	 * encoded with integer indices as their key.
	 * @param s XML element value
	 * @return Map with meaning index as key and StarlingMeaning object as value
     */ 
	public HashMap <Integer, StarlingMeaning> parseMEANING (String s);
	
	/** 
	 * Parse XML PROTO element which contains the proto-form of a word in 
	 * a Starling dictionary.
	 * @param s XML element value
	 * TODO : For now only check unparseable field -> parse complex proto element 
	 * which defines multiple proto-forms in a single expression.
	 * @return Returns input string
	 */ 
	public String parsePROTO(String s);

	 
	/** 
	 * Parse XML RUSMEAN with Russian meaning(s) (see also parseMEANING)
	 * @param s XML element value
	 * @return Map with meaning index as key and parsed meaning string as value
	 */
	public HashMap <Integer, StarlingMeaning> parseRUSMEAN (String s); 

	 
	/** 
	 * Parse XML REFERENCE element which contains bibliographic references.
	 * @param s XML element value
	 * @return Map with abbreviation of author/source e.g (Qutb) as key
	 * and StarlingReference object as value.
	 */
	public HashMap <Integer, StarlingReference> parseREFERENCE (String s);
	 
}
