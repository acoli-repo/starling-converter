package de.unifrankfurt.informatik.acoli.starling.model;

import static de.unifrankfurt.informatik.acoli.starling.util.Utils.createOutputstreamWriter;
import static de.unifrankfurt.informatik.acoli.starling.util.Utils.print;
import static de.unifrankfurt.informatik.acoli.starling.util.Utils.printMeanings;
import static de.unifrankfurt.informatik.acoli.starling.util.Utils.printStarlingWords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import de.unifrankfurt.informatik.acoli.starling.types.LanguageCodeNotFoundException;
import de.unifrankfurt.informatik.acoli.starling.types.StarlingBibRef;
import de.unifrankfurt.informatik.acoli.starling.types.StarlingLanguage;
import de.unifrankfurt.informatik.acoli.starling.types.StarlingLexicon;
import de.unifrankfurt.informatik.acoli.starling.types.StarlingMeaning;
import de.unifrankfurt.informatik.acoli.starling.types.StarlingReference;
import de.unifrankfurt.informatik.acoli.starling.types.StarlingWord;
import de.unifrankfurt.informatik.acoli.starling.util.Utils;


/**
 * This class implements the RDF conversion of the Turkic Starling dictionary.<p>
 * <a href="http://starling.rinet.ru/cgi-bin/response.cgi?root=config&morpho=0&basename=\data\alt\turcet&first=1">Turkic Starling dictionary</a>
 * <p>
 * by using a Jena model. It may be also well suited for converting other dictionaries of the Starling Altaic etymology for
 * Mongolian, Tungus, Korean and Japanese.
 * @author frank
 */
public class TurkicRDFModelBuilder extends StarlingRDFModelBuilder {
	
	// Variables used by the model builder
	HashMap <String, StarlingLexicon> lexiconMap = new HashMap <String, StarlingLexicon> ();
	ArrayList <Resource> protoLexicalEntries = new ArrayList <Resource> ();
		
	// Jena model, ontology model
	Model model = ModelFactory.createDefaultModel();
	OntModel ontolexModel = ModelFactory.createOntologyModel();
	

	final String rdf_ = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	final String rdfs_ = "http://www.w3.org/2000/01/rdf-schema#";
	final String owl_ = "http://www.w3.org/2002/07/owl#";
	final String skos_ = "http://www.w3.org/2004/02/skos/core#";
	final String dct_ = "http://purl.org/dc/terms/";
	final String ontolex_ = "http://www.w3.org/ns/lemon/ontolex#";
	final String lime_ = "http://www.w3.org/ns/lemon/lime#";
	final String vartrans_ = "http://www.w3.org/ns/lemon/vartrans#";
	final String lemonet_ = "http://www.lemon-model.net/lemon/etym#";
	final String msh_ = "https://miskinhill.com.au/rdfschema/1.0/";
	final String lexvo_ = "http://www.lexvo.org/page/iso639-3/";
	//final String bibo_ = "http://purl.org/ontology/bibo/";
	String base_ = "";
	
	
	// Create objects for ontolex classes & properties
	OntClass LexicalEntry = ontolexModel.createClass(ontolex_+"LexicalEntry");
	OntClass Lexicon = ontolexModel.createClass(lime_+"Lexicon");
	OntClass Form = ontolexModel.createClass(ontolex_+"Form");
	OntClass LexicalSense = ontolexModel.createClass(ontolex_+"LexicalSense");
	Property entry = ontolexModel.createProperty(lime_+"entry");
	Property language = ontolexModel.createProperty(lime_+"language");
	Property canonicalForm = ontolexModel.createProperty(ontolex_+"canonicalForm");
	Property otherForm = ontolexModel.createProperty(ontolex_+"otherForm");
	Property writtenRep = ontolexModel.createProperty(ontolex_+"writtenRep");
	Property reference = ontolexModel.createProperty(ontolex_+"reference");
	Property sense = ontolexModel.createProperty(ontolex_+"sense");
	Property references = ontolexModel.createProperty(dct_+"references");
	
	OntClass objectProperty = ontolexModel.createClass(owl_+"ObjectProperty");
	OntClass transitiveProperty = ontolexModel.createClass(owl_+"TransitiveProperty");
	Property comment = ontolexModel.createProperty(rdfs_+"comment");
	Property domain = ontolexModel.createProperty(rdfs_+"domain");
	Property label = ontolexModel.createProperty(rdfs_+"label");
	Property range = ontolexModel.createProperty(rdfs_+"range");
	Property subPropertyOf = ontolexModel.createProperty(rdfs_+"subPropertyOf");
	Property lexicalRel = ontolexModel.createProperty(vartrans_+"lexicalRel");

	Property a = model.createProperty(rdf_+"type");
	Property dc_language = model.createProperty(dct_+"language");
	Property skos_definition = model.createProperty(skos_+"definition");
	Property derivedFrom = model.createProperty(lemonet_+"derivedFrom");
	Property citesM = model.createProperty(msh_+"cites");
	//Property cites = model.createProperty(bibo_+"cites");
	//Property pages = model.createProperty(bibo_+"pages");
	
	// buffer variable
	String protoUri = "";
	
	/**
	 * 
	 * @param isoLanguageMap Code mapping from Starling language codes to iso-codes
	 * @param base_ Prefix URL used in RDF for lexicon resources
	 */
	public TurkicRDFModelBuilder (String rdfBaseUri, HashMap<String, StarlingLanguage> languageCodeMap) {
		
		this.isoLanguageMap = languageCodeMap;
		
		if (!rdfBaseUri.endsWith("/")) rdfBaseUri += "/";
		this.base_ = rdfBaseUri;
	
		
		// Define prefix names for used namespaces
		model.setNsPrefix("rdf",rdf_);
		model.setNsPrefix("rdfs",rdfs_);
		model.setNsPrefix("owl",owl_);
		model.setNsPrefix("skos",skos_);
		model.setNsPrefix("dct",dct_);
		model.setNsPrefix("lexvo",lexvo_);
		model.setNsPrefix("ontolex", ontolex_);
		model.setNsPrefix("lime", lime_);
		model.setNsPrefix("vartrans", vartrans_);
		model.setNsPrefix("lemonet", lemonet_);
		model.setNsPrefix("star", base_);
		//model.setNsPrefix("bibo", bibo_);
		model.setNsPrefix("msh",msh_);
		
		// Define lemonet:derivedFrom
		Resource r = model.createResource(derivedFrom.getURI());
		r.addProperty(a, objectProperty);
		r.addProperty(a, transitiveProperty);
		r.addProperty(comment,"The ’derivedFrom’ property relates two lexical entries that stand in a etymological relation where source and target are known. The subject position holds the source whereas the object position holds the derived word","en");
		r.addProperty(a, ontolexModel.getOntClass(owl_+"TransitiveProperty"));
		r.addProperty(domain, LexicalEntry);
		r.addProperty(label, "directed etymological relationship","en");
		r.addProperty(range, LexicalEntry);
		r.addProperty(subPropertyOf, lexicalRel);
		
	}
	
	/**
	 * Create a lime:Lexicon for each language found in the dictionary. Do this at the beginning of the conversion process.
	 * Afterwards add lexical entries (words) to each lexicon.
	 */
	public void buildLexica() {
		
		// Create lexicon for each language found in the XML.
		ArrayList <String> tags = converter.getXMLParser().getXMLElementTypes();
		
		Iterator <String> t = tags.iterator();
		String elementName;
		
		
		while (t.hasNext()) {
			
			elementName = t.next();
			
			switch (elementName) {
			
			// skip meaning and other elements not used for  lexicon creation
			// TODO : add elements to skip for other lexicon files later
			case "MEANING" : 	break; case "RUSMEAN" : break; case "REFERENCE" :	break;
			case "NUMBER" : 	break; case "PRNUM" : 	break; case "COMMENTS" :	break;
			
			
			default : 				// Create lexicon for language
			String languageAcronym;	// iso language string later used in lexicon
				
			try {
				
				languageAcronym = isoLanguageMap.get(elementName).getAcronym();

				// Create lexicon
				Resource lexicon = model.createResource(base_+converter.getLexiconprefix()+languageAcronym);
				lexicon.addProperty (a,Lexicon);
				
				
				// Add lexicon language as string and as lexvo link 
				if (!elementName.equals("PROTO")) {
					lexicon.addProperty(language, languageAcronym);
					lexicon.addProperty(dc_language, model.createResource(isoLanguageMap.get(elementName).getLink().toString()));
				}
				
				// Add lexicon to lexiconMap which is later used to add lexical entries
				lexiconMap.put(
						elementName,
						new StarlingLexicon(
								"",
								base_+converter.getLexiconprefix()+languageAcronym,
								lexicon,
								isoLanguageMap.get(elementName)
								));
				}
			catch (Exception e){
				print("Error while creating ontolex lexicon");
				e.printStackTrace();
				}
		}
		}
		}

	
	@Override
	public Integer buildPROTO(Integer recordNr, String elementName,
			String elementValue) {
		// Filters also unwanted elements with syntax not covered by the parser
		// TODO extend parser s.t. filtering is obsolete
	
		String parsedProto = converter.starlingParser.parsePROTO (elementValue);
		
		// Skip whole record if parse of PROTO yields nothing usable 
		if (parsedProto.equals("")) {
			
			// Skip the whole record
				Utils.logErrorProto("Record : "+(recordNr));
				Utils.logErrorProto(elementName+" : "+elementValue);
				return Utils.PROTO_PARSE_ERROR;
			}
		
	
		print (converter.getXMLParser().getProtoLanguage()+" : "+parsedProto);
		// A proto elelemt marks the start of a record. In protoLexicalEntries the created
		// lemon entry for a PROTO word is buffered. This is then used in the buildWORDS method
		// when the translation a processed.
		protoLexicalEntries.clear();
		
		return buildWORDS (recordNr, elementName, elementValue);
	}


	@Override
	public Integer buildMEANING(Integer recordNr, String elementValue) {
		meaningMap = converter.starlingParser.parseMEANING(elementValue);
		
		// Test for unreconized meaning pattern
		if (meaningMap.isEmpty()) return Utils.MEANING_PARSE_ERROR;
		
		print ("");
		print ("elementValue : "+elementValue);
		printMeanings(meaningMap);
		
		// Use only first proto entry for now
		Resource e = protoLexicalEntries.get(0);
			for (StarlingMeaning m : meaningMap.values()) {
			
				//// Sense path
				// http://www.example.com/starling/lexicon_Proto-Turkic/*Kuĺ/sense1
				String senseUri = e.toString()+"/sense";
				
				// Add an index to the meaning if more than one meaning was found
				if (meaningMap.size() > 1) {
					senseUri += m.getMeaningID();
				}
				
				// Create resource for sense
				Resource r = model.createResource(senseUri);
				
				// Make it a LexicalSense
				r.addProperty(a, LexicalSense);
	
				// Add language
				// <skos:definition xml:lang="en">bird</skos:definition>
				String mString = m.getMeaning();
				r.addProperty(skos_definition,mString,"en");
				
				// Add rdfs label as well in split notation
				for (String sub : mString.split(",")) {
				r.addProperty(label,sub.trim(),"en");
				}
				
				// add sense to proto LexicalEntry
				e.addProperty(sense,r);
				
				// remember senseUri in StarlingMeaning
				m.setLink(senseUri);
			}
			return Utils.NO_PARSE_ERROR;
	}
	

	@Override
	public Integer buildWORDS (Integer recordNr, String elementName, String elementValue) {
		
			 // PROTO language and cognate languages are handled equally
			 // (i.e. PROTO can have several words and (bracket, quoted) comments as well)
			try {

			print("");
			print ("Language : "+elementName);
			try {
				print ("IsoLanguage : "+starlingToISOLanguage(elementName, converter.getXMLParser().getProtoLanguage()));
			} catch (LanguageCodeNotFoundException e) {
				e.printStackTrace();
			}
		
			Resource firstEntry = null; 
			
			ArrayList <Resource> lexicalEntryBuffer = new ArrayList <Resource> ();
			
			// Read language element value into objects of type StarlingWord
			languageWords = converter.starlingParser.parseLanguageElement(elementValue);
			// check error in elementValue
			if (languageWords.isEmpty()) return Utils.COGNATE_PARSE_ERROR;
			cognates += languageWords.size();
			for (StarlingWord sw : languageWords) {

				String extWord = DigestUtils.md5Hex(sw.getWord()+"_"+recordNr);
				String uri  = lexiconMap.get(elementName).getEndpointPrefix()+"/"+extWord;
				String curi = lexiconMap.get(elementName).getEndpointPrefix()+"/"+extWord+"/canForm";
				String ouri = lexiconMap.get(elementName).getEndpointPrefix()+"/"+extWord+"/otherForm";

				
				/* Alternativly encode URL because of special characters in words
				String extWord = sw.getWord()+"_"+recordNr;
				String uri  = URLEncoder.encode(lexiconMap.get(elementName).getEndpointPrefix()+"/"+extWord);
				String curi = URLEncoder.encode(lexiconMap.get(elementName).getEndpointPrefix()+"/"+extWord+"/canForm");
				String ouri = URLEncoder.encode(lexiconMap.get(elementName).getEndpointPrefix()+"/"+extWord+"/otherForm");
				*/
				
				// Make LexicalEntry
				Resource e = model.createResource(uri);
				e.addProperty(a, LexicalEntry);
				
				// Make LexicalForm & canonicalForm
				Resource f;
				if (firstEntry == null) {
					f = model.createResource(curi);
					f.addProperty (writtenRep, sw.getWord(),starlingToISOLanguage(elementName, converter.getXMLParser().getProtoLanguage()));
					e.addProperty(canonicalForm,f);
					firstEntry = e;
				} else {
					f = model.createResource(ouri);
					f.addProperty (writtenRep, sw.getWord(),starlingToISOLanguage(elementName,converter.getXMLParser().getProtoLanguage()));
					e.addProperty(otherForm,f);
				}
				
				
				// Add derivedFrom property to non-PROTO elements
				if (elementName.equals("PROTO")) {
					// remember uri of proto word
					protoUri = uri;
									}
				else 
					{
					// Add derivedFrom property to lexical entry
					e.addProperty(derivedFrom, model.createResource(protoUri));
					
					String bracketGloss = "";
					// check if bracket comment is bibliographic reference
					// TODO : only bibliographic references of type (AuthorShortName)
					// are recognized
					if (!sw.bracketComment.isEmpty()) {
						String temp = String.join(" and ", sw.bracketComment).trim();
						temp = temp.substring(1, temp.length()-1);
						
						if (buildReferenceImpl(e, temp) == 0)
							// if bracket comment not identified as citation 
							// then put into gloss
							bracketGloss= String.join(" and ", sw.bracketComment);
						}
					
					// Add gloss as rdfs:comment if present				
					if (!bracketGloss.isEmpty()
						|| !sw.quotedComment.isEmpty())
					e.addProperty(comment, "gloss : "+bracketGloss+String.join(" and ", sw.quotedComment));
					}


				// Add sense(s) to LexicalEntry by getting the meaning with the
				// meaning indices in StarlingWord
				if (!elementName.equals("PROTO")) {
					
					// (For single meaning add id 1)
					ArrayList <Integer> ids = sw.getMeaningIDs();
					if (ids.isEmpty()) ids.add(1);
					
					for (Integer id : ids) {
						
						// <lemon:LexicalSense rdf:nodeID="n5622821665199449046">
						Resource r = model.createResource(uri+"/sense");
						
						// Make it a LexicalSense
						r.addProperty(a, LexicalSense);
						
						// Reference meaning of proto word (by index)
						r.addProperty(reference, model.createResource(meaningMap.get(id).getLink()));
						e.addProperty(sense,r);
					}
					
				
				} else {
				// Store lexical entries of proto for creating adding the LexicalSense
				// later in the buildMEANING step
					protoLexicalEntries.add(e);
				}
				
				// Add LexicalEntry to buffer (delayed write)
				lexicalEntryBuffer.add(e);
			}
			
			// Write all lexical entries of the record
			for (Resource le : lexicalEntryBuffer) {
				
				// add lexical entry to lexicon
				Resource lex = (Resource) lexiconMap.get(elementName).getLexicon();
				lex.addProperty(entry,le);
			}
			
			printStarlingWords(languageWords);
			return Utils.NO_PARSE_ERROR;
			} 
			catch (Exception e) {
				
				// In case of error in a language element skip only this language element
				// (do not write lexical entries for it)
					Utils.logErrorLanguage("Record : "+(recordNr));
					Utils.logErrorLanguage(elementName+" : "+elementValue);
				return Utils.COGNATE_PARSE_ERROR;
			}
	}


	@Override
	public void buildRUSMEAN(String elementValue) {
		
	if (meaningMap.isEmpty()) return;
	HashMap<Integer, StarlingMeaning> rusmean = converter.starlingParser.parseRUSMEAN(elementValue);
	
	for (Integer k : meaningMap.keySet()){
		
		// Create resource for sense
		Resource r = model.createResource(meaningMap.get(k).getLink());
		
		// Add rus language
		// <skos:definition xml:lang="ru">xyz</skos:definition>
		StarlingMeaning m = rusmean.get(k);
		// Russian meaning not always defined !
		if (m != null) {
		String mString = m.getMeaning();
		r.addProperty(skos_definition,mString,"ru");
		
		// Add rdfs label as well in split notation
		for (String sub : mString.split(",")) {
		r.addProperty(label,sub.trim(),"ru");
		}
		}
	}
	}

	
	@Override
	public void buildREFERENCE(String elementValue) {
		
		buildReferenceImpl(protoLexicalEntries.get(0), elementValue);
	}
	
	/**
	 * Parse string and create citations
	 * @param r Resource which is citing (proto/cognate)
	 * @param s String value to be parsed
	 */
	
	public Integer buildReferenceImpl(Resource r, String s) {
		
		int createdReferences = 0;
		print ("#REF");
		print (s);
		HashMap<Integer, StarlingReference> referenceMap = converter.starlingParser.parseREFERENCE(s);
		for (int i : referenceMap.keySet()) {
		
		// Test valid reference shortName
		StarlingReference ref = referenceMap.get(i);
		print (ref.getShortNameId());
		
		// TODO : keys can be lists of 'shortNameId's
		if (bibRefMap.containsKey(ref.getShortNameId())) {
			
			StarlingBibRef bibRef = bibRefMap.get(ref.getShortNameId());
		
			// 1. generate citation
			Resource C = generateCitation (
					r.getURI()+"/comment/"+ref.getShortNameId(),
					bibRef.getUrl(),
					ref.getPageRange());
		
			// 2. link citation with resource (proto/cognate)
			//    where citation was found 
			r.addProperty(references, model.createResource(C.getURI()));

			
			print ("========");
			print (ref.getShortNameId());
			print (ref.getPageRange());
			print ("========");
			
			// count created references
			createdReferences++;
		}
		}
		return createdReferences;
	}
	
	/**
	 * Create a Citation
	 * @param citationURL URI for new created Citation resource
	 * @param referenceURL URL for bibliographic entitiy which is referenced
	 * @param pages String with single page or page range
	 */
	public Resource generateCitation (String citationURL, String referenceURL, String pages) {
		// 1. create resource
		Resource C = model.createResource(citationURL);
		// 2. make Citation
		C.addProperty(a,model.createResource(msh_+"Citation"));
		// 3. add comment
		if (!pages.trim().equals(""))
			C.addProperty(comment, "pages : "+ pages);
		// 4. cites BibliographicResource
		C.addProperty(citesM, model.createResource(referenceURL));
		
		return C;
	}
	
	
	
	@Override
	public boolean initializeModel() {
							
		buildLexica();
		return false;
	}

	@Override
	public boolean serialize(String outputPath, RDFFormat format) {
		try {
		RDFDataMgr.write(createOutputstreamWriter(outputPath+".ttl","UTF-8"), model, format);
		return false;
		}
		catch (Exception e) {
		return true;
		}
	}
	
	
	@Override
	public Integer getCognateCount() {
		// TODO Auto-generated method stub
		return null;
	}

}