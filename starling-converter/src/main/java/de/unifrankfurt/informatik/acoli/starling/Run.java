package de.unifrankfurt.informatik.acoli.starling;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.jena.riot.RDFFormat;

import de.unifrankfurt.informatik.acoli.starling.BibRef.BibRefFileReader;
import de.unifrankfurt.informatik.acoli.starling.BibRef.BibRefRun;
import de.unifrankfurt.informatik.acoli.starling.converter.StarlingXML2RDFConverter;
import de.unifrankfurt.informatik.acoli.starling.converter.TurkicXML2RDFConverter;
import de.unifrankfurt.informatik.acoli.starling.model.StarlingRDFModelBuilder;
import de.unifrankfurt.informatik.acoli.starling.model.TurkicRDFModelBuilder;
import de.unifrankfurt.informatik.acoli.starling.parser.StarlingJAXBReader;
import de.unifrankfurt.informatik.acoli.starling.parser.StarlingParser;
import de.unifrankfurt.informatik.acoli.starling.parser.StarlingXMLParserI;
import de.unifrankfurt.informatik.acoli.starling.types.StarlingLanguage;
import de.unifrankfurt.informatik.acoli.starling.util.LanguageMappingReader;
import de.unifrankfurt.informatik.acoli.starling.util.Utils;

/**
 * Run the conversion from the cmdline
 * @author frank
 *
 */
public class Run {
	
	static String inputXML = "";
	static String outputRDF = "";
	static String bibRDF = "";
	static Path inputLangMapCSV;
	static String rdfBaseUri = "";
	
	
	public static void main(String[] args) {
		
		
		if (args.length < 2 || args.length > 3) {
			
		if (args.length == 1 && args[0].equals("demo")) {
				
		  inputXML = "/input-files/turkic-demo.xml";
		  inputLangMapCSV = Utils.getResourcePath("/input-files/codeMappingTurkic.csv");
		  rdfBaseUri = "http://www.example.com";
		  bibRDF = "bib.ttl";
		  outputRDF = "turcet-demo";
		
		  } else {
			
		  System.out.println ("\nUsage : java -jar jarFile inputFile.xml languageCodeMap.csv [rdfBaseUri]\n "
					+ "For a demo please run 'java -jar jarFile demo' \n");
		  return;
		  }
		}
		
		if (args.length == 2) {
			inputXML = args[0];
			inputLangMapCSV = Paths.get(args[1]);
			rdfBaseUri = "http://www.example.com";
			bibRDF = "bib.ttl";
			outputRDF = inputXML;
		}
		
		if (args.length == 3) {
			inputXML = args[0];
			inputLangMapCSV = Paths.get(args[1]);
			rdfBaseUri = args[2];
			bibRDF = "bib.ttl";
			outputRDF = inputXML;
		}

		
		// 0. Read language code mapping from file
		LanguageMappingReader lmr = new LanguageMappingReader();
		  HashMap<String, StarlingLanguage> isoLanguageMap = lmr.read(inputLangMapCSV);
		
		// 1. Build bibliographic references
		String [] a = new String [1];a[0]=bibRDF;
		BibRefFileReader bibRefReader = BibRefRun.main(a);
		
		// 2. XML reader (jaxb)
		StarlingXMLParserI xmlParser = new StarlingJAXBReader(inputXML);
		
		// 3. Starling parser (default)
		StarlingParser parser = new StarlingParser();
		
		// 4. RDF model builder
		StarlingRDFModelBuilder modelBuilder = new TurkicRDFModelBuilder(rdfBaseUri, isoLanguageMap);
		
		// 5. Use map for bibliographic references from 1.
		modelBuilder.setBibRefMap(bibRefReader.getMap());
		
		// 6. Create converter instance
		StarlingXML2RDFConverter converter = new TurkicXML2RDFConverter(xmlParser, parser, modelBuilder);
		
		// 7. Start the conversion
		converter.convert();
		
		// 8. Serialize Jena model to RDF file		
		modelBuilder.serialize(outputRDF, RDFFormat.TURTLE_BLOCKS);
		//modelBuilder.serialize(outputRDF, RDFFormat.RDFXML_PRETTY);
		
		System.out.println("Conversion finished!");

	}

}
