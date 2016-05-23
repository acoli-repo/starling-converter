package de.unifrankfurt.informatik.acoli.starling.types;


/**
 * Class for lexicon objects used in lemon
 * @author frank
 */
public class StarlingLexicon {
	
	// Description for lexicon
	String description;	
	
	// Prefix in rdf encoding
	String endpointPrefix;
	
	// Reference for lexicon (i.e. lemon:Lexicon or Jena Resource)
	Object lexicon;
	
	// Language object including all language information
	StarlingLanguage language;
	
	
	public StarlingLexicon (
			String description,
			String endpointPrefix,
			Object lexicon,
			StarlingLanguage language) {
		
		this.description = description;
		this.endpointPrefix = endpointPrefix;
		this.lexicon = lexicon;
		this.language = language;
	}
	
	
	public Object getLexicon() {
		return lexicon;
	}
	public void setLexicon(Object lexicon) {
		this.lexicon = lexicon;
	}
	
	public String getAcronym() {
		return this.language.getAcronym();
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getEndpointPrefix() {
		return endpointPrefix;
	}

	public void setEndPointURL(String endPointURL) {
		endpointPrefix = endPointURL;
	}
		
}
