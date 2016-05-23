package de.unifrankfurt.informatik.acoli.starling.types;

import java.net.URI;

/**
 * 
 * Class for representing languages
 * @author frank
 */
public class StarlingLanguage {

	String comment;
	String acronym;
	String codeName;
	// LOD link i.e. http://www.lexvo.org/page/iso639-3/otk
	URI link;
	
	

	public StarlingLanguage (String acronym, URI link) {
		
		this.acronym = acronym;
		this.link = link;
	}
	
	public StarlingLanguage (String acronym, String comment, String codeName, URI link) {
		
		this.acronym = acronym;
		this.comment = comment;
		this.codeName = codeName;
		this.link = link;
	}
	
	public String getAcronym() {
		return acronym;
	}
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String code) {
		this.codeName = code;
	}
	public URI getLink() {
		return link;
	}
	public void setLink(URI link) {
		this.link = link;
	}
}
