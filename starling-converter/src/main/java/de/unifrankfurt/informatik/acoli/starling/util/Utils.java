package de.unifrankfurt.informatik.acoli.starling.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;

import de.unifrankfurt.informatik.acoli.starling.Run;
import de.unifrankfurt.informatik.acoli.starling.types.StarlingMeaning;
import de.unifrankfurt.informatik.acoli.starling.types.StarlingWord;


/**
 * Some definitions and helper methods
 * 
 * @author frank
 *
 */
public class Utils {
	
	
	// Error numbers
	public static final Integer NO_PARSE_ERROR = 0;
	public static final Integer PROTO_PARSE_ERROR = 1;
	public static final Integer COGNATE_PARSE_ERROR = 2;
	public static final Integer MEANING_PARSE_ERROR = 3;
	
	public static final boolean PrintOutput = false;
	public static final boolean logOutput = false;
	
	// Path definitions for error output
	public static String languageErrorFile="";
	public static String protoErrorFile = "";
	public static String runLog = "";
	public static FileOutputStream errorFileStreamLanguage;
	public static FileOutputStream errorFileStreamProto;
	public static FileOutputStream runLogFileStream;
	public static FileSystem jarFs = getJarFs("starling2rdf-1.0-with-dp.jar");
	
	{
	try {
		languageErrorFile = System.getProperty("user.dir")+"/etc/languageErrors.txt";
		protoErrorFile = System.getProperty("user.dir")+"/etc/protoErrors.txt";
		runLog = System.getProperty("user.dir")+"/etc/runLog.txt";
		errorFileStreamLanguage = createOutputstream(languageErrorFile);
		errorFileStreamProto = createOutputstream(protoErrorFile);
		runLogFileStream = createOutputstream(runLog);
	}
	catch (Exception e) {}
	}
	



	 /**
	  * Short print method
	  * @author frank
	  */
	 public static void print (String s) {
		 	logRun(s);
	    }
	 
	 /**
	  * Short print method
	  * @author frank
	  */
	 public static void print (int n) {
		 	logRun(String.valueOf(n));
	    }

	 /**
	  * Print content of StarlingWord object
	  * @param words
	  * @author frank
	  */
	 public static void printStarlingWords(ArrayList<StarlingWord> words) {
			for (StarlingWord t : words) {
				 logRun("word "+t.getWordID()+": "+ t.getWord());
				 
				 if (!t.getMeaningIDs().isEmpty()) {
				 logRun("meaning indexes : ");
				 for (Integer i : t.getMeaningIDs()) {
					 logRun(Integer.toString(i)+" ");
				 }
				 logRun("");
				 }
				 for (String qc : t.getQuotedComment()) {
					 logRun("quoted comment : "+qc);
				 }
				 for (String bc : t.getBracketComment()) {
				    logRun("bracket comment : "+bc);
				 }
			}
		}
	 
	 /**
	  * Print content of StarlingMeaning object.
	  * @param meaningMap Hashmap stores meanings.
	  * @author frank
	  */
	 public static void printMeanings(HashMap<Integer, StarlingMeaning> meaningMap) {
		 if (!meaningMap.isEmpty()) {
			 for (Integer key : meaningMap.keySet()) {
			 logRun("Meaning "+key.toString()+" : ");
			 logRun(meaningMap.get(key).getMeaning());
			 }
		 }
	 }
	 

	
	 /**
	  * Create FileOutputStream
	  * @param filepath File path
	  * @param return FileOutputStream object
	  * @author frank
	  */
	 public static FileOutputStream createOutputstream (String filepath) {
	 try {
		return new FileOutputStream(filepath);
	 	 } catch (FileNotFoundException e) {
		e.printStackTrace();
		return null;
	 }
	 }
	 
	 
	 /**
	  * Create OutputStreamWriter
	  * @param filepath File path
	  * @param encoding Name of encoding for outputstream
	  * @param return OutputStreamWriter object
	  * @author frank
	  */
	 public static OutputStreamWriter createOutputstreamWriter (String filepath, String charset) {
	 try {
		return new OutputStreamWriter (createOutputstream(filepath), charset);
	 	 } catch (Exception e) {
		e.printStackTrace();
		return null;
	 }
	 }
	 
	 /**
	  * Log function writes parse errors of language XML fields to file
	  * @param errorMessage
	  */
	 public static void logErrorLanguage(String errorMessage) {
		 try {
			 //errorFileStreamLanguage.write((errorMessage+"\n").getBytes());
		} catch (Exception e) {
			//e.printStackTrace();
		}
	 }


	/**
	 * Log function writes parse errors of PROTO XML fields to file
	 * @param errorMessage
	 */
	public static void logErrorProto(String errorMessage) {
		 try {
			 //errorFileStreamProto.write((errorMessage+"\n").getBytes());
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
	}
	
	/**
	 * Write output to file and screen
	 * @param message
	 */
	public static void logRun(String message) {
		 if (PrintOutput) System.out.println(message);
		 if (logOutput) {
		 try {
			 runLogFileStream.write((message+"\n").getBytes());
		} catch (IOException e) {
			//e.printStackTrace();
		}
		}
	}
	
	/**
	 * Get Path object for resource in jar
	 * @return Path object for resource in jar
	 */
	public static Path getResourcePath(String relPath) {
		return jarFs.getPath(relPath);
	}
	
	
	/**
	 * Get filesystem for jar file
	 * @param jarfile Name of jar file
	 * @return FS for jar file
	 */
	public static FileSystem getJarFs(String jarfile) {
		File jarFile = null;
		
		CodeSource codeSource = Run.class.getProtectionDomain().getCodeSource();
		try {
			jarFile = new File(codeSource.getLocation().toURI().getPath());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			return FileSystems.newFileSystem(Paths.get(jarFile.getParentFile().getPath()+"/"+jarfile), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
