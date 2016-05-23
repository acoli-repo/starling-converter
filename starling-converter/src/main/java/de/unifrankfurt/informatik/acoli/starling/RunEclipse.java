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

/**
 * Run the conversion from eclipse
 * @author frank
 *
 */
public class RunEclipse {
	
	
	public static void main(String[] args) {

		
		String inputXML = System.getProperty("user.dir")+"/input-files/turkic-demo.xml";
		String rdfBaseUri = "http://acoli.cs.uni-frankfurt.de/starling";
		Path inputLangMapCSV = Paths.get(System.getProperty("user.dir")+"/input-files/codeMappingTurkic.csv");
		String bibRDF = System.getProperty("user.dir")+"/output-files/bib.ttl";
		String outputRDF = System.getProperty("user.dir")+"/output-files/turkic-demo";
		
		
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
