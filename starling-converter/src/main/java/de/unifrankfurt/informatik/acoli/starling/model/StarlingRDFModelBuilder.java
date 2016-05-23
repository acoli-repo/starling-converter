package de.unifrankfurt.informatik.acoli.starling.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.jena.riot.RDFFormat;

import de.unifrankfurt.informatik.acoli.starling.converter.StarlingXML2RDFConverter;
import de.unifrankfurt.informatik.acoli.starling.types.LanguageCodeNotFoundException;
import de.unifrankfurt.informatik.acoli.starling.types.StarlingBibRef;
import de.unifrankfurt.informatik.acoli.starling.types.StarlingLanguage;
import de.unifrankfurt.informatik.acoli.starling.types.StarlingMeaning;
import de.unifrankfurt.informatik.acoli.starling.types.StarlingWord;


/**
 * Extend the StarlingRDFModelBuilderI interface with some object definitions which
 * are somehow containers for information gathered by the StarlingParser.
 * @author frank
 */
public abstract class StarlingRDFModelBuilder implements StarlingRDFModelBuilderI {
	
	/**
	 * converter instance
	 */
	public StarlingXML2RDFConverter converter;
	
	/**
	 * Maps an integer to a StarlingMeaning object (see XML MEANING element)
	 */
	public HashMap <Integer, StarlingMeaning> meaningMap;
	
	/**
	 * A language XML element is parsed into a list of objects of type StarlingWord.
	 * <p>
	 * Example :
	 * &ltfield name="TRK">gün 2, güneš 1, (dial.) gujaš 1&lt/field&gt<br>
	 * <p>
	 * The XML element will be parsed into 3 objects of type StarlingWord
	 * (for the words gün, güneš and gujaš respectively).
	 */
	public ArrayList<StarlingWord> languageWords;
	
	/**
	 *  HashMap for bibliographic references read from text file
	 *  @see /input-files/starbib.txt
	 */
	protected HashMap<String, StarlingBibRef> bibRefMap;
	
	/**
	 *  Count cognates (debug)
	 */
	public Integer cognates = 0;

	/**
	 * Map Starling languages codes to iso-codes
	 */
	public HashMap<String, StarlingLanguage> isoLanguageMap;

	
	
	/**
	 *  Constructor
	 */
	public StarlingRDFModelBuilder(){};
	
	/**
	 * Set the converter instance
	 */
	public void setConverter(StarlingXML2RDFConverter starlingXML2RDFConverter) {
		this.converter = starlingXML2RDFConverter;
	}
		
	/**
	 * Hashmap stores bibliographic references.
	 * @param map
	 */
	public void setBibRefMap(HashMap<String, StarlingBibRef> map) {
		this.bibRefMap = map;
	}
	
	/**
	 *  Get cognate count
	 * @return Count of cognates
	 */
	public abstract Integer getCognateCount();
	

	// Other methods from the interface
	
	public abstract Integer buildPROTO (Integer recordNr, String elementName, String elementValue);
	
	public abstract Integer buildMEANING (Integer recordNr, String elementValue);
	
	public abstract Integer buildWORDS (Integer recordNr, String elementName, String elementValue);

	public abstract void buildRUSMEAN(String elementValue);

	public abstract void buildREFERENCE(String elementValue);
	
	public abstract boolean initializeModel();
	
	public abstract boolean serialize(String outputPath, RDFFormat format);
	
	/**
	 * Convert Starling language code identifier to ISO-639
	 * @param Starling language identifier given as 3 or 4 letter code
	 * @param return ISO language 3 letter code
	 * @author frank
	 */
	public String starlingToISOLanguage (String language, String protoLanguage) throws LanguageCodeNotFoundException{
			
			if (!language.contains("PROTO")) {
			String isoString = isoLanguageMap.get(language).getAcronym();
			if (!(isoString == null)) {
				return isoString;
			} else {
				throw new LanguageCodeNotFoundException(language);
			}
			} else {
			return protoLanguage;
			}
		}
	
	}
