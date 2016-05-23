package de.unifrankfurt.informatik.acoli.starling.util;


import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

import de.unifrankfurt.informatik.acoli.starling.types.StarlingLanguage;

public class LanguageMappingReader {
	

	
//public HashMap<String, StarlingLanguage> read(String csvFile) {
//	return read(Paths.get(csvFile));
//}


public HashMap<String, StarlingLanguage> read(Path csvFile) {
		
	HashMap<String, StarlingLanguage> isoLanguageMap = new HashMap <String,StarlingLanguage> ();
		
	   try {
	        //Scanner scanner = new Scanner(new File (csvFile));
		    Scanner scanner = new Scanner (csvFile);
	        String line = "";
	        String[] fields;
	       
	        while(scanner.hasNext()){
	        	line = scanner.nextLine();
	        	//System.out.println(line);
	        	fields = line.split(",");
	        	
	        	
	            if (!fields[0].trim().equals("PROTO"))
	                isoLanguageMap.put(fields[0].trim(),
	                		new StarlingLanguage (fields[1].trim(),URI.create(fields[2].trim())));
	            else
		            isoLanguageMap.put(fields[0].trim(), 
		            		new StarlingLanguage (fields[1].trim(),null,null,null));
	        }
	        scanner.close();
	    } catch (Exception e) {
	    	System.out.println("Error parsing language mapping file !");
	    	e.printStackTrace();
	    }
	   
	return isoLanguageMap;
	}
	
}
