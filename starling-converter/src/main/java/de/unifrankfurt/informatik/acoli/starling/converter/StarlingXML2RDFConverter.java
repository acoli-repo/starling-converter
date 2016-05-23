package de.unifrankfurt.informatik.acoli.starling.converter;

import de.unifrankfurt.informatik.acoli.starling.model.StarlingRDFModelBuilder;
import de.unifrankfurt.informatik.acoli.starling.model.StarlingRDFModelBuilderI;
import de.unifrankfurt.informatik.acoli.starling.parser.StarlingParser;
import de.unifrankfurt.informatik.acoli.starling.parser.StarlingParserI;
import de.unifrankfurt.informatik.acoli.starling.parser.StarlingXMLParserI;

/**
 * Base class XML-RDF Converter for Starling dictionaries.
 * The abstract convert method should contain the conversion logic.
 * @author Frank
 */
public abstract class StarlingXML2RDFConverter {
	
	static String elementName;
	static String elementValue;
	
	/**
	 *  Base URL
	 */
	String base = "http://www.example.com/";
	
	/**
	 *  Lexicon prefix
	 */
	String lexiconPrefix = "lexicon_";
	
	/**
	 *  XML file input path
	 */
	public String inputPath;
	
	/**
	 *  XML reader
	 */
	StarlingXMLParserI xmlParser;
	
	/**
	 *  Starling parser
	 */
	public StarlingParserI starlingParser;
	
	/**
	 *  RDF model builder
	 */
	public StarlingRDFModelBuilderI modelBuilder;
	
	
	// Constructor
	public StarlingXML2RDFConverter (String inputPath, StarlingXMLParserI xmlParser, StarlingParser parser, StarlingRDFModelBuilder modelBuilder
			) {
		
	// set instance variables
	this.inputPath = inputPath;
	this.xmlParser = xmlParser;
	this.starlingParser = parser;
	this.modelBuilder=modelBuilder;
	modelBuilder.setConverter(this);
	}
	 
		
	/** 
	 * Left to implement
	 */
	public abstract boolean convert ();
	

	/**
	 * Set the lexicon prefix for the RDF file. 
	 * (i.e. 'http://www.example.com/lexicon_GAG/kemik')
	 * @param lexiconPrefix
	 */
	public void setLexiconprefix(String lexiconPrefix) {
		this.lexiconPrefix = lexiconPrefix;
	}
	
	/**
	 * Get the lexicon prefix
	 * @return Prefix used in each lexicon URL.
	 */
	public String getLexiconprefix() {
		return lexiconPrefix;
	}

	
	/**
	 * Get the model builder
	 * @return StarlingRDFModelBuilder
	 */
	public StarlingRDFModelBuilderI getModelBuilder() {
		return modelBuilder;
	}
	
	
	/**
	 * Get the parser
	 * @return StarlingRDFModelBuilder
	 */
	public StarlingXMLParserI getXMLParser() {
		return this.xmlParser;
	}

}

