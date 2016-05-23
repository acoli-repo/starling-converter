package de.unifrankfurt.informatik.acoli.starling.types;

import java.util.ArrayList;

/**
 * Class for a word in a Starling dictionary. Actually its the representation
 * of a XML proto or a cognate word. The unit word refers to lexical
 * entries in a RDF lexicon.
 * <p> 
 * Example (Excerpt from Starling Turkic dictionary) :<p>
 *  &ltrecord id="1236"&gt<br>
 *     &ltfield name="NUMBER"&gt1236&lt/field&gt<br>
 *     &ltfield name="PROTO"&gt*jɨĺ&lt/field&gt<br>
 *     &ltfield name="PRNUM"&gt2569&lt/field&gt<br>
 *     &ltfield name="MEANING"&gt1 mountain forest, thicket 2 spine&lt/field&gt<br>
 *     &ltfield name="RUSMEAN"&gt1 горный лес, чаща 2 хребет&lt/field&gt<br>
 *     &ltfield name="ATU">jɨš 1 (Orkh.)&lt/field&gt<br>
 *     &ltfield name="KRH">jɨš 'high ground' (MK)&lt/field&gt<br>
 * <p>
 * Cognate ATU :<br>
 * word = jɨš<br>
 * bracketComment = (Orkh.)<br>
 * quotedComment =<br>
 * semReference = 1<br>
 * <p>
 * Cognate KRH :<br>
 * word = jɨš<br>
 * bracketComment = (MK)<br>
 * quotedComment = 'high ground'<br>
 * semReference =<p>

 * @author frank
 */
public class StarlingWord {
	
	public int wordID;
	public String word;
	public ArrayList <String> quotedComment;				// (anything)
	public ArrayList <Integer>  semReference = new ArrayList <Integer> (); // reference to index in element meaning
	public ArrayList <String> bracketComment;				// 'anything'
	
	
	public StarlingWord () {}
	
	/**
	 * Constructor
	 * @param id Integer number used for resource URI
	 * @param t The proto/cognate word itself
	 * @param qc Quoted comment, e.g. 'any comment'
	 * @param bc Bracket comment, e.g. [any comment] or (any comment)
	 * @param sr List of integers which are references to the meaning as defined in the XML MEANING and RUSMEAN fields
	 */
	public StarlingWord (int id, String t, ArrayList <String> qc, ArrayList <String> bc, ArrayList <Integer> sr) {
		this.wordID = id;
		this.word = t;
		this.quotedComment = qc;
		this.bracketComment = bc;
		this.semReference = sr;
	}
	
	public StarlingWord (String t) {
		this.word = t;
	}

	public int getWordID() {
		return wordID;
	}

	public void setWordID(int word_id) {
		this.wordID = word_id;
	}
	
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public String getWord() {
		return word;
	}
	
	public ArrayList <String> getQuotedComment() {
		return quotedComment;
	}
	public void setQuotedComment(ArrayList <String> qc) {
		this.quotedComment = qc;
	}
	
	public ArrayList <String> getBracketComment() {
		return bracketComment;
	}
	public void setBracketComment(ArrayList <String> bc) {
		this.bracketComment = bc;
	}
	
	public ArrayList<Integer> getMeaningIDs() {
		return semReference;
	}
	public void setSemReference(ArrayList<Integer> semReference) {
		this.semReference = semReference;
	}

}
