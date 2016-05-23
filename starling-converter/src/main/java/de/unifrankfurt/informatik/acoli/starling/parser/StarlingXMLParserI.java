package de.unifrankfurt.informatik.acoli.starling.parser;

import java.util.ArrayList;
import java.util.Map;


/**
 * Define XML Parser for Starling xml exports made with the star4win
 * windows application available from<p> 
 * <a href="http://starling.rinet.ru/downl.php?lan=en#soft">http://starling.rinet.ru/downl.php?lan=en#soft</a>.
 *
 * @author frank
 *
 */
public interface StarlingXMLParserI {
	
	/**
	 *
	 * Fetch one XML record from a Starling dictionary
	 * @param recordNo Number e.g. <record id="642">
	 * @return Map with XML element names and string values
	 */
	public Map <String,String> getXMLRecord (int recordNr);
	
	/**
	 * Get number of read record elements of Starling XML dictionary
	 *
	 * @return record count in Starling XML file.
	 */
	public int getXMLRecordCount ();

	/**
	 *
	 * Get content of <description> element in Starling XML dictionary
	 *
	 * @return description
	 */
	public String getXMLDescription();
	
	/**
	 * Get possible element types (e.g. MEANING) for a record in Starling XML dictionary
	 * from <fields> element and read protoLanguage description.
	 * 
	 * @return List of XML element names used in a Starling XML record
	 */
	public ArrayList <String> getXMLElementTypes();
	
	/**
	 *
	 * Get proto language name as defined in <fields><field name="PROTO"> .. .
	 * @return Lexicon language
	 * @author frank
	 */
	public String getProtoLanguage();
	
	/**
	 * Get Starling XML dictionary path
	 * @return Path
	 */
	public String getInputPath();
	
}
