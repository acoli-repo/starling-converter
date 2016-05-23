package de.unifrankfurt.informatik.acoli.starling.BibRef;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import de.unifrankfurt.informatik.acoli.starling.types.StarlingBibRef;
import de.unifrankfurt.informatik.acoli.starling.util.Utils;

/**
 * Read bibliographic references from a text file into StarlingBibRef objects.
 * Bibliographic references taken from the Starling website 
 * <a href="http://starling.rinet.ru/cgi-bin/response.cgi?root=config&morpho=0&basename=\data\biblio\biblio.dbf&first=1&off=&text_author=&method_author=substring&ic_author=on&text_title=&method_title=substring&ic_title=on&text_db=&method_db=substring&ic_db=on&text_abbreviati=&method_abbreviati=substring&ic_abbreviati=on&text_publisher=&method_publisher=substring&ic_publisher=on&text_place=&method_place=substring&ic_place=on&text_year=&method_year=substring&ic_year=on&text_any=&method_any=substring&sort=author&ic_any=on">Bibliographic References</a>
 * .<p>
 * Reader for references txt file with this structure :<br>
 * Element names : Author, Title, Used in bases, Abbreviated name,
 * Place, Year<br>
 * (Author element required, other elements optional)<p>
 * Example :<br>
 * Author: Zajączkowski A.<br>
 * Title: Najstarsza wersja turecka Husräv u šīrīn Qutba<br>
 * Used in bases: Turkic etymology<br>
 * Abbreviated name: Qutb<br>
 * Place: Warszawa<br>
 * Year: 1961<br>
 * Author: Adams<br>
 * Used in bases: Indo-European etymology<br>
 * Author: Bailey H. W.<br>
 * Title: Dictionary of Khotan Saka<br>
 * Used in bases: Indo-European etymology, Turkic etymology<br>
 * Abbreviated name: Bailey<br>
 * Place: Cambridge<br>
 * Year: 1979<br>
 * Author: Bang W.<br>
 * ...<br>
 * &ltEnd:&gt<br>
 * @author Frank
 *
 */
public class BibRefFileReader {
	
	// Save bibliographic references in hashmap
	private HashMap <String,StarlingBibRef> bibRefMap = 
			new HashMap <String,StarlingBibRef> ();
	

	/**
	 * Parser for text file with biliographic references
	 * @param filePath
	 * @return success True / false
	 */
	public boolean readCP(String filePath) {
		
	int state = -1;	// start = 0; end = 1
	int lfdnr = 0;
	String author, authorN, title, used, abrv, place, year;
	author = authorN = title = used = abrv = place = year = "";
	String prefix = "";
	String postfix = "";
	String hash = "";
	
	try {
	
	Path file = Utils.getResourcePath(filePath);
	List<String> lines;
	lines = Files.readAllLines(file, StandardCharsets.UTF_8);	

	
	for(String line: lines) {
		
		
		prefix = line.split(":")[0];
		postfix = line.substring(prefix.length()+1).trim();
		
		/*
		System.out.println(line);
		System.out.println(prefix);
		System.out.println(postfix);
		*/
		
		switch (prefix) {
        case "Author":
        	author = new String(authorN);
            authorN = postfix;
            state++;
            break;
        case "Title":
            title = postfix;
            break;
        case "Used in bases":
        	used = postfix;
        	break;
        case "Abbreviated name":
        	abrv = postfix;
            break;
        case "Place":
            place = postfix;
            break;
        case "Year":
            year = postfix;
            break;
        case "<END":
        	author = new String(authorN);
            state++;
            break;
		}
	
	// Author missing -> skip record
	if (state == 1 && !author.isEmpty()) {
		

		if (!abrv.isEmpty()) {
			hash = abrv;
			} else {
			// if abrev. name is missing then use
			// author as hash
			hash = author;
			}
		
		// Create new BibRef
		StarlingBibRef bibRef = 
		new StarlingBibRef (lfdnr, hash, author,
				title, used, abrv, place,
				year);

		//Add BibRef to map
		bibRefMap.put(hash, bibRef);
		
	// Reset variables
	title = used = abrv = place = year = "";
	state = 0;
	}
	
	
	// Counter
	lfdnr++;
	}
	
	
	} catch (IOException e) {
		e.printStackTrace();
		return false;
	}
	return true;
	}

	
	/**
	 * 
	 * @return Map for bibliographic references
	 */
	public HashMap <String,StarlingBibRef> getMap() {
		return bibRefMap;
	}

}
