package de.unifrankfurt.informatik.acoli.starling.types;


/**
 * Class for representing bibliographic references as used 
 * in the REFERENCE field in the XML export of a Starling dictionary.<p>
 * Example : Parse XML REFERENCE into StarlingReference
 * <p>
 * &ltfield name="REFERENCE"&gtЭСТЯ 6, 9-10, VEWT 275 
 * (should be distinguished from PT *Kob-).&lt/field&gt<p>
 * Reference 1 :<br>
 * ShortNameId = ЭСТЯ<br>
 * pageRange = 9-10<br>
 * comment =<br>
 * <p>
 * Reference 2 :<br>
 * ShortNameId = VEWT<br>
 * pageRange = 275<br>
 * comment = (should be distinguished from PT *Kob-)<p>
 * 
 * @author frank
 *
 */
public class StarlingReference {
	
	private String shortNameId;
	private String pageRange;
	private String comment;

	/** Constructor
	 * @param shortName Abbreviation (key) for reference in starbib.txt
	 * @param pageRange Pages which are referred to
	 * @param comment Additional gloss
	 */
	public StarlingReference(String shortName, String pageRange,
			String comment) {
		this.shortNameId = shortName;
		this.pageRange = pageRange;
		this.comment = comment;
	}
	
	public String getShortNameId() {
		return shortNameId;
	}
	public void setShortNameId(String shortName) {
		this.shortNameId = shortName;
	}
	public String getPageRange() {
		return pageRange;
	}
	public void setPageRange(String pageRange) {
		this.pageRange = pageRange;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}



}
