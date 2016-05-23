package de.unifrankfurt.informatik.acoli.starling.converter;

import static de.unifrankfurt.informatik.acoli.starling.util.Utils.print;

import java.util.ArrayList;
import java.util.Map;

import de.unifrankfurt.informatik.acoli.starling.model.StarlingRDFModelBuilder;
import de.unifrankfurt.informatik.acoli.starling.parser.StarlingParseException;
import de.unifrankfurt.informatik.acoli.starling.parser.StarlingParser;
import de.unifrankfurt.informatik.acoli.starling.parser.StarlingXMLParserI;
import de.unifrankfurt.informatik.acoli.starling.util.Utils;

/**
 * This class provides an implementation for the conversion of 
 * dictionaries of the 'Altaic etymology' found at<p>
 * see <a href="http://starling.rinet.ru/cgi-bin/main.cgi?flags=eygtnnl">Starling Altaic etymology</a>
 * <p>
 * and especially for the Turkic etymology.<p>
 *  
 * In the 'convert' method records are pulled from the XML file which
 * is the output of the downloaded dictionary file which can be obtained
 * from <p>
 * <a href="http://starling.rinet.ru/download/ALTAIC.exe">http://starling.rinet.ru/download/ALTAIC.exe</a>
 * <p>
 * For the needed conversion to XML format the Windows application
 * 'star4win' also available on the same page <p>
 * <a href="http://starling.rinet.ru/download/star4win-2.4.2.exe">http://starling.rinet.ru/download/star4win-2.4.2.exe</a>
 * <p>
 * is required !<p> 
 * Finally you need to install the XML-Export plugin for star4win which
 * can be found at<p>
 * <a href="http://starling.rinet.ru/startrac/starling/wiki/StarLingPlugins">http://starling.rinet.ru/startrac/starling/wiki/StarLingPlugins</a>
 * <p>
 * The XML file is processed record by record. Depending on the field
 * type in a record an appropriate conversion function is called.
 * <p>
 * Since element names in a record are not equal across different
 * Starling dictionaries the case statement in the convert-method
 * may be modified appropriately, if you try to convert other
 * dictionaries than those from the Altaic etymology.
 * <p>
 * The conversion algorithm assumes that the fields are read in the
 * following order from a XML record :<p>
 * 1. Proto field<br>
 * 2. Meaning field<br>
 * 3. Rusmean field<br>
 * 4. Language fields<br>
 * 5. Reference field<br>
 * <p>
 * FYI :<br>
 * In case the fields in the xml file appear in a different order the
 * conversion algorithm will probably not work anymore !
 * The XML fields Number, Prnum and Comments are not relevant and are
 * skipped. 
 * 
 * @author frank
 *
 */
public class TurkicXML2RDFConverter extends StarlingXML2RDFConverter {

	// Constructor
	public TurkicXML2RDFConverter(StarlingXMLParserI xmlReader, StarlingParser parser, StarlingRDFModelBuilder modelBuilder) {
		super(xmlReader.getInputPath(),xmlReader, parser, modelBuilder);
	}
	

	/**
	 * In this method the logic for the conversion is implemented
	 * @throws StarlingParseException 
	 */
	public boolean convert() {
		
		  modelBuilder.initializeModel();
		
		  // Setup variables
		  int cognateCount = 0;
		  int convertedCognates = 0;
		  int convertedRecords = 0;
		  Integer error;
		  //int[] testRecords = {7};
		  //int[] testRecords = {961}; turcet
		  //int[] testRecords = {1032};
		  //int[] testRecords = {471};
		  //int[] testRecords = {957};
		  int[] testRecords = {687};
		  ArrayList <Integer> recordList = new ArrayList <Integer> ();
		  int i = 1;
		  int recordCount = getXMLParser().getXMLRecordCount();
		  while (i <= recordCount) {
			  recordList.add(i);
			 i++;
		  }
		  
		  
		  
		  //// Record loop ////
		  for (Integer recordNr : recordList) {
			print ("Record : "+recordNr);
			
			// Get fields of record with recordNr
			Map<String, String> recordFields = getXMLParser().getXMLRecord(recordNr);
			error = 0;
			
			// Read fields of record
			for (String elementName : recordFields.keySet()) {
				
				elementValue = recordFields.get(elementName);
				
				// Invoke specific actions on field type
				switch (elementName) {
				
					case "MEANING" : {
						error = modelBuilder.buildMEANING(recordNr, elementValue);
						break;
					}
					case "RUSMEAN" : 	{
						modelBuilder.buildRUSMEAN(elementValue);
						break;
					}
					case "REFERENCE" : 	{
						modelBuilder.buildREFERENCE(elementValue);
						break;
					}
					case "NUMBER" : 	break;	// database index not used !
					case "PRNUM" : 		break;  // database index not used !
					case "COMMENTS" : 		break;  // database index not used !
					
					case "PROTO" : {
						error = modelBuilder.buildPROTO(recordNr, elementName, elementValue);
						break;
					}
					
					// Any other element type than PROTO, MEANING, RUSMEAN or REFERENCE is
					// a language translation element
					default : {
						cognateCount++;
						error = modelBuilder.buildWORDS(recordNr, elementName, elementValue);
						if (error == Utils.NO_PARSE_ERROR) convertedCognates++;
					}
				}
				
				// In case of an error in PROTO or MEANING field skip the whole record.
				if (error == Utils.MEANING_PARSE_ERROR ||
					error == Utils.PROTO_PARSE_ERROR) break;
			}
			
			// Count successful converted records (records may be incomplete because of cognate parse errors) 
			if (error == Utils.NO_PARSE_ERROR || 
				error == Utils.COGNATE_PARSE_ERROR) convertedRecords++;
		}
		
		print ("Records # : "+convertedRecords+"/"+recordCount);
		print ("Cognate # : "+convertedCognates +"/"+cognateCount);
		return true;
	}

	
	
}
