package de.unifrankfurt.informatik.acoli.starling.types;


/**
 * Class for meaning as parsed from XML field MEANING in a Starling XML 
 * dictionary
 * @author frank
 */
public class StarlingMeaning {

	Integer meaningID;
	String meaning;
	String language;
	String link;
	
	/**
	 * Constructor
	 * @param meaningID The index with which a meaning is associated. (also key in MeaningMap)
	 * @param meaning String for meaning
	 * @param language Language
	 * @param link RDF resource URI of the lemon sense (where the meaning is used)
	 */
	public StarlingMeaning (Integer meaningID, String meaning, String language, String link) {
		
		this.meaningID = meaningID;
		this.meaning = meaning;
		this.language = language;
		this.link = link;
	}
	
	public Integer getMeaningID() {
		return meaningID;
	}
	public void setMeaningID(Integer meaningID) {
		this.meaningID = meaningID;
	}
	public String getMeaning() {
		return meaning;
	}
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}
