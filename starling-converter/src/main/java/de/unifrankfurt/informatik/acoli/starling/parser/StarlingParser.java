package de.unifrankfurt.informatik.acoli.starling.parser;

import static de.unifrankfurt.informatik.acoli.starling.util.Utils.*;
import de.unifrankfurt.informatik.acoli.starling.types.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Parse complex string values in XML elements of a Starling XML record 
 * @author frank
 */
public class StarlingParser implements StarlingParserI {
	
	
	static Pattern meaningPattern = Pattern.compile("((\\d+)([^0-9]+))");
	static Pattern referencePattern;
	static HashMap <Integer, StarlingMeaning> meaningMap = new HashMap <Integer, StarlingMeaning> ();
	static HashMap <Integer, StarlingMeaning> meaningMapRus = new HashMap <Integer, StarlingMeaning> ();
	static HashMap <Integer, StarlingReference> referenceMap = new HashMap <Integer, StarlingReference> ();
	static ArrayList <StarlingWord> translations = new ArrayList <StarlingWord> ();
	static Pattern protoFail = Pattern.compile(".*[,;/()?{}\\[\\]>~].*");
	static Pattern langFail = Pattern.compile(".*[>,<.{].*");
	
	

	/**
	 * Parse XML language element into StarlingWord objects. Uses a state
	 * parser.
	 * TODO : May use regular expressions instead but more work because of
	 * many encoding syntax variations.
	 * @param s XML element value for language
	 * @return ArrayList of StarlingWord objects
	 * @author frank
	 */
	 public ArrayList <StarlingWord> parseLanguageElement (String s) {
		 
		 ArrayList <StarlingWord> words = new ArrayList <StarlingWord> ();
		 
		 // Check forbidden characters in string s
		 if (!checkValid(s)) return words;
		 
		 int id = 1;
		 
		 // remove font info
		 s = removeFontInfo(s);
		 
		 // replace dial. with 'dial.'
		 s = s.replace("dial.","'dial.'");
		 
		 String word = "";
		 String meaningIndexString = "";
		 ArrayList <String> bracketComment = new ArrayList <String> ();
		 ArrayList <String> quotedComment = new ArrayList <String> ();

		 
		 int a = 0;		// single quotes
		 int a1 = 0;
		 int b = 0;		// brackets
		 int b1 = 0;
		 int z = 0;		// digit
		 int z1 = 0;
		 int z2 = 0;
		 int i = 0;
		 char t;
		 boolean is_digit;
		 boolean is_colon;
		 boolean is_space;
		 int last_end=0;
		 
		 print ("elementValue : "+s);
		 
		 while (i < s.length()) {
			 
			 t = s.charAt(i);
			 
			 if (t == '(')  {
				 if (a == 0) {
				 if (word.isEmpty()) word = trimAll(s.substring(last_end,i-1));
				 if (z > 0 && meaningIndexString.isEmpty()) meaningIndexString = trimAll(s.substring(z1,i-1));
				 a1=i;}
				 a+=1;
				 if (z > 0) z2=i;
				 i+=1;	
				 continue;}
			 if (t == ')')  {
				 a-=1;
				 if (a == 0) {
				//print (s.substring(a1,i+1));
				bracketComment.add(trimAll(s.substring(a1,i+1)));
				if (word.isEmpty()) last_end = i+1;
				}
				 i+=1; continue;}
			 if (t == '\'') {b+=1 % 2;
			 // open
			 // single quote not accepted in word (not preceeded by space)
			 if (b == 1 && s.charAt(i-1) != ' ') {b=0; i+=1; continue;}
			 if (b == 1) {b1 = i;
			 if (word.isEmpty()) word = trimAll(s.substring(last_end,i-1));
			 if (z > 0 && meaningIndexString.isEmpty()) meaningIndexString = trimAll(s.substring(z1,i-1));
			 } else {
			 // close
				//print (s.substring(b1,i+1));
				quotedComment.add (trimAll(s.substring(b1,i+1)));
				if (word.isEmpty()) last_end = i+1;
			 	}
			 if (z > 0 && b == 1) z2=i;
			 i+=1; continue;}
			 
			 // skip all text in brackets or single quotes
			 if (a > 0 || b > 0) {i+=1; continue;}
			
			 
			 is_digit = Character.isDigit(t);
			 is_colon = (t == ',');
			 is_space = (t == ' ');
			 
			 
			 // found colon after finding digit
			 if (is_colon && z == 1) {z=2;i+=1;continue;}
			 
			 // found digit
			 if (is_digit && a == 0 && b == 0) {
				 if (word.isEmpty()) word = trimAll(s.substring(last_end,i-1));
				 if (z == 0) z1=i;
				 z=1;i+=1;
				 continue;}
			 
			 // found delimiter
			 if (is_colon && a == 0 && b == 0 && z == 0) {
				//print (s.substring(0,i));
				if (word.isEmpty()) word = trimAll(s.substring(last_end,i));
				
				words.add(new StarlingWord(id++, word, quotedComment, bracketComment, intStringToArray (meaningIndexString)));
				word = "";
				meaningIndexString = "";
				bracketComment = new ArrayList <String> ();
				quotedComment = new ArrayList <String> ();
				last_end = i;
				i+=1;continue;
			 }
			 
			 // found delimiter
			 if (a == 0 && b == 0 && z == 2) {
				    if ((is_digit || is_space) == false) {
				    //print (s.substring(z1,i));
				    	
				    if (meaningIndexString.isEmpty()) meaningIndexString = trimAll(s.substring(z1,i));
					//print (s.substring(0,i));
					words.add(new StarlingWord(id++, word, quotedComment, bracketComment, intStringToArray (meaningIndexString)));
					//lemonModelAdd (translation, meaningIndexString, quotedComment, bracketComment);
					word = "";
					meaningIndexString = "";
					bracketComment = new ArrayList <String> ();
					quotedComment = new ArrayList <String> ();
					z1=0; z=0; last_end = i;
				}
			 		else {//z=1;
			 			 }
			 	}
		 i+=1;
		 }
		 // found delimiter
		 if (last_end != s.length()) {
			 if (z1 > 0) {
				 if (z2 == 0) z2 = i;
				 //print (s.substring(z1,z2));
				 if (meaningIndexString.isEmpty()) meaningIndexString = trimAll(s.substring(z1,i));
				 }
			 if (word.isEmpty()) word = trimAll(s.substring(last_end,i));
			 //print (s.substring(0,i));
   			 words.add(new StarlingWord(id++, word, quotedComment, bracketComment, intStringToArray (meaningIndexString)));
			 //lemonModelAdd (translation, meaningIndexString, quotedComment, bracketComment);
			 word = "";
			 meaningIndexString = "";
			 bracketComment = new ArrayList <String> ();
			 quotedComment = new ArrayList <String> ();
			 z=0;
		 }

		 return words;
	 }
	 
	 
	 /** 
		 * Parse XML MEANING element which may contain several meanings optionally
		 * encoded with integer indices as their key.
		 * @param s XML element value
		 * @return Map with meaning index as key and StarlingMeaning object as value
	     */ 
	 public HashMap <Integer, StarlingMeaning> parseMEANING (String s) {
		 
		 parseMeaningElement(s, meaningMap);
		 return meaningMap;
	 }
	 
	 /** 
		* Parse XML PROTO element which contains the proto-form of a word in 
	     * a Starling dictionary.
		 * @param s XML element value
		 * TODO : For now only check unparseable field -> parse complex proto element 
		 * which defines multiple proto-forms in a single expression.
		 * @return Returns input string
		 */ 
	 public String parsePROTO(String s) {
		 
		 // For now : skip a PROTO element which has one of
		 // ,;/()?{}[]&
		 // Remember the real parsing is done in buildWords !
		 Matcher m = protoFail.matcher(s);
		 while (m.find()) {
			return "";
		 }
		 
		 // return found 
		 return s;
	 }

	 
	 /** 
	   * Parse XML RUSMEAN with Russian meaning(s) (see also parseMEANING)
	   * @param s XML element value
	   * @return Map with meaning index as key and parsed meaning string as value
	   */
	 public HashMap <Integer, StarlingMeaning> parseRUSMEAN (String s) {
		 
		 // parse xml element value for meaning
		 parseMeaningElement(s, meaningMapRus);
		 return meaningMapRus;
	 }

	  
	 /**
	  * Parse XML MEANING element which may contain several meanings optionally
	  * encoded with integer indices as their key.<p>
	  * Example : "1 meaning a 2 meaning b ..." 
	  * @param s Complex XML element string
	  * @param meaningMap
	  * @return Map with meaning index as key and StarlingMeaning object as value
	  * @author frank
	  */
	 // TODO split "1 meaning a, meaning b" ?
	 public void parseMeaningElement(String s, HashMap<Integer, StarlingMeaning> _meaningMap) {
		 
	 _meaningMap.clear();
	 
	 // Prune all strings containing characters in protoFail
	 // meanings with a list of definitions are not converted !
	 // Matcher m1 = protoFail.matcher(s);
	 // while (m1.find()) {
	 //	return;
	 //}
	 
	 
	 // Search for multiple meanings
	 Matcher m = meaningPattern.matcher(s);
	 
	 
	 // Multiple meanings
	 while (m.find()) {
		 Integer index = Integer.parseInt(m.group(2));
		 _meaningMap.put(index ,new StarlingMeaning (index, m.group(3).trim(), null,null));
	 }
	 
	 // Single meaning
	 if (_meaningMap.isEmpty()) {
		 _meaningMap.put(1 ,new StarlingMeaning (1, s.trim(), null,null));
	 }
	 
	 }
	 
	 
	 /**
	  * Parse REFERENCE element for bibliographic references.<p>
	  * Example 1 :<br>
	  * "VEWT 293, ЭСТЯ 5, 118, Лексика 414-415."<p>
	  * 
	  * Example 2 :<br>
	  * "VEWT 107, 110; EDT 404, 412. Turk. &gt;
	  * MMong. (HY 20) čix, WMong. čig,
	  * Khalkha čig, Ord. čīg 'bamboo screen'. 
	  * The Nogh. form may be a compound; for the second part cf. *bɨńan."
	  * 
	  * @param s Complex XML element string
	  * @return Map with abbreviation of author/source e.g (Qutb) as key
	  * and StarlingReference object as value.
	  * @author frank
	  */
	 
	 public HashMap <Integer, StarlingReference> parseREFERENCE(String s) {
		 
	 // clear reference map
	 referenceMap.clear();
	 
	 // Split string by ','
	 print ("Splits");
	 boolean startsWithDigit;
	 boolean containsSpace;
	 String head = "";
	 String pages = " ";
	 String comment = "";
	 int ix,iy;
	 String [] splits = s.split("[,;]");
	 print ("Element value : "+s);
	 int counter = 0;
	 for (String x : splits) {
		 
		 startsWithDigit = false;
		 containsSpace = true;
		 
		 // Remove trailing space
		 x = x.trim();
		 
		 // Property 1 : Starts with number => not head
		 if (x.substring(0,1).matches("\\d")) startsWithDigit = true;
		 
		 // Property 2 : contains not multiple tokens => not head
		 ix = x.indexOf(" ");
		 if (ix == -1) containsSpace = false;
		 
		 if (!startsWithDigit && containsSpace) {
		
		 // Save reference
		 if (pages.substring(0,1).matches("\\d") || pages.equals("no")) {
			if (pages.equals("no")) pages = "";
			else
			// remove unwanted characters in page enumeration
			pages = pages.replaceAll("[.()]", "");
			referenceMap.put(counter, new StarlingReference(head,pages,comment));
		    print ("Head : "+ head);
			print ("Pages : "+ pages);
			counter++;
		 }
		 
		 head = x.substring(0,ix).trim();
		 pages = x.substring(ix).trim();
		 
		 // skip head if first no. not number
		 //if (x.substring(0,1).matches("!\\d"))
		 
		 iy = pages.indexOf(" ");
		 if (iy > -1) pages = pages.substring(0,iy);
		 
		 //print ("Head : "+ head);
		 //print ("First no. : "+ pages);
		 } else {
		
		 // expected number but got text -> skip
		 if (!startsWithDigit) {
			 head = x;
			 pages = "no";
			 continue;
		 }
		 
		 // remove trailing text
		 if (containsSpace) x = x.substring(0,ix);
		 
		 // Append
		 pages = pages+","+x;
		 //print ("Pages : " + pages);
		 }
		 
	 }
	 
	 // Save last reference
	 if (pages.substring(0,1).matches("\\d") || pages.equals("no")) {
		// remove unwanted characters in page enumeration
		if (pages.equals("no")) pages = "";
		else 
		pages = pages.replaceAll("[.()]", "");
		referenceMap.put(counter, new StarlingReference(head,pages,comment));
		print ("Head : "+ head);
		print ("Pages : "+ pages);
		}
	 
	 return referenceMap;
	 }
	 
	 
	 /**
	  * Read list of integers into ArrayList
	  * @param s String
	  * @return ArrayList with integer values
	  * @author frank
	  */
	 public static ArrayList <Integer> intStringToArray(String s) {
		 
		 ArrayList <Integer> ints = new ArrayList <Integer> ();
		 // remove leading/trailing space,;
		 s = trimAll(s);
		 if (s.isEmpty()) return ints;
		 
		 for (String a : s.split(",")) {
			 ints.add(Integer.parseInt(trimAll(a)));
		 }
		 return ints;
	 }
	 
	 
	 /**
	  * Remove leading, trailing space, comma, semicolon from string.
	  * @param s String
	  * @return converted string
	  * @author frank
	  */
	 private static String trimAll(String s) {
		 
		 if (s.length() == 0) return s;
		 
		 char t;
		 int from = 0;
		 int to = s.length()-1;
		 while (from < s.length()) {
			 t = s.charAt(from);
			 if (t == ' ' || t == ',' || t == ';') {from+=1;continue;}
			 else break;
		 }
		 
		 while (to >= 0) {
			 t = s.charAt(to);
			 if (t == ' ' || t == ',' || t == ';') {to-=1;continue;}
			 else break;
		 }
		 
		 if (from < s.length()) {return s.substring(from,to+1);}
		 else {return "";}
	 }
	 
	 /**
	  * Remove font info from string
	  * @param s String
	  * @return String without font info
	  * @author frank
	  */
	 public static String removeFontInfo (String s) {
		 return s.replace("\\I", "").replace("\\i", "");
	 }
	 
	 /**
	  * Remove colon from string.
	  * @param word
	  * @return String without colon.
	  * @author frank
	  */
	 private static String removeColon(String word) {
		return word.replace(",", "");
	 }
	 
	 /**
	  * Method provides quick fix for unparseable language element fields. 
	  * @param elementValue
	  * @return Does string contain characters that are covered by the
	  * parser ?
	  */
	 public boolean checkValid(String elementValue) {
		Matcher m = langFail.matcher(elementValue);
		while (m.find()) {
			return false;
			}
		return true;
	 }
	 

}



