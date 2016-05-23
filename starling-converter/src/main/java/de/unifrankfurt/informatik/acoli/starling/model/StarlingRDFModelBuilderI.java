package de.unifrankfurt.informatik.acoli.starling.model;

import org.apache.jena.riot.RDFFormat;

import de.unifrankfurt.informatik.acoli.starling.converter.StarlingXML2RDFConverter;
import de.unifrankfurt.informatik.acoli.starling.types.LanguageCodeNotFoundException;

/**
 * A model builder should implement actions for translating each XML element found in a
 * record element of a Starling XML dictionary to RDF. The model builder makes use of the 
 * information (instances of classes in de.unifrankfurt.informatik.starling.types) already
 * extracted by the StarlingParser. For each XML element type in a record the interface
 * defines a method which will implement the RDF code generation. How the code generation
 * is implemented is left open. The route taken here is to use a Jena model
 * (see <a href="https://jena.apache.org">https://jena.apache.org</a>) which can be easily serialized
 * to many RDF file formats.
 * @author frank
 *
 */
public interface StarlingRDFModelBuilderI {

	/**
	 * Create RDF for XML PROTO element
	 * @param recordNr XML recordnr
	 * @param elementName XML element name
	 * @param elementValue XML element value (String)
	 * @return status
	 */
	public abstract Integer buildPROTO (Integer recordNr, String elementName, String elementValue);
	
	/**
	 * Create RDF for XML MEANING element
	 * @param recordNr XML recordnr
	 * @param elementValue XML element value (String)
	 * @return status
	 */
	public abstract Integer buildMEANING (Integer recordNr, String elementValue);
	
	/**
	 * Action for any XML element with a name different from PROTO, MEANING, RUSMEAN or NUMBER.
	 * Target elements that should be processed are all elements that have a Starling language identifier
	 * (3 or 4 letter language code) as their name (e.g. TAT). These actually refer to cognates of the
	 * proto-form in the XML PROTO element. Build lemon lexical entries, etc.
	 * @param recordNr XML recordnr
	 * @param elementName XML element name
	 * @param elementValue XML element value (String)
	 * @return status
	 */
	public abstract Integer buildWORDS (Integer recordNr, String elementName, String elementValue);

	/**
	 * Create RDF for XML RUSMEAN element. Build lemon sense resources.
	 * @param elementValue XML element value (String)
	 */
	public abstract void buildRUSMEAN(String elementValue);
	
	/**
	 * Create RDF for XML REFERENCE element. Build bibliographic
	 * references.
	 * @param elementValue XML element value (String)
	 */
	public abstract void buildREFERENCE(String elementValue);

	/**
	 * General purpose method called when starting the conversion
	 * @return status
	 */
	public boolean initializeModel();

	/**
	 * Export the Jena model as ttl, rdf-xml, etc. 
	 * @param outputPath
	 * @param outputFormat
	 * @return status
	 */
	public boolean serialize(String outputPath, RDFFormat outputFormat);
	
	/**
	 * Set converter instance
	 * @param starlingXML2RDFConverter
	 */
	public void setConverter(StarlingXML2RDFConverter starlingXML2RDFConverter);
	
	/**
	 * Convert Starling language code identifier to ISO-639
	 * @param Starling language identifier given as 3 or 4 letter code
	 * @param return ISO language 3 letter code
	 * @author frank
	 */
	public String starlingToISOLanguage (String language, String protoLanguage) throws LanguageCodeNotFoundException;



}
