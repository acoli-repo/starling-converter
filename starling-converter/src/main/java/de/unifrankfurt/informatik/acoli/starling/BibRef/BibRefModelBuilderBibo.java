package de.unifrankfurt.informatik.acoli.starling.BibRef;

import static de.unifrankfurt.informatik.acoli.starling.util.Utils.createOutputstreamWriter;

import java.util.HashMap;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import de.unifrankfurt.informatik.acoli.starling.types.StarlingBibRef;


/**
 * Jena model builder which uses the bibo ontology
 * see <a href="http://bibliontology.com/">http://bibliontology.com</a>
 * @author frank
 */
public class BibRefModelBuilderBibo {
	
	// Variables used by the model builder
	HashMap <String, StarlingBibRef> bibRefMap;

	// Jena model, ontology model and ontology manager
	Model model = ModelFactory.createDefaultModel();
	OntModel ontolexModel = ModelFactory.createOntologyModel();
	OntDocumentManager ontManager = ontolexModel.getDocumentManager();
	
	// path to local ontology file
	final String biboPath = System.getProperty("user.dir")+"/owl/bibo.php.rdf"; 
	
	// define namespaces
	final String rdf_ = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	final String rdfs_ = "http://www.w3.org/2000/01/rdf-schema#";
	final String owl_ = "http://www.w3.org/2002/07/owl#";
	final String skos_ = "http://www.w3.org/2004/02/skos/core#";
	final String dct_ = "http://purl.org/dc/terms/";
	final String base_ = "http://acoli.cs.uni-frankfurt.de/starling/bib/";
	final String talis_ = "http://schemas.talis.com/2005/address/schema#";
	final String bibo_ = "http://purl.org/ontology/bibo/";
	Property a = model.createProperty(rdf_+"type");
	Property date = model.createProperty(dct_+"date");
	Property title = model.createProperty(dct_+"title");
	Property place = model.createProperty(talis_+"localityName");
	Property authorList = model.createProperty(bibo_+"authorList");
	Property shortDescription = model.createProperty(bibo_+"shortDescription");
	Property isReferencedBy = model.createProperty(dct_+"isReferencedBy");
	Property cites = model.createProperty(bibo_+"cites");
	Property citedBy = model.createProperty(bibo_+"citedBy");
	OntClass BiboDocument;

	
	
	// constructor
	public BibRefModelBuilderBibo (HashMap <String, StarlingBibRef> bibRefMap) {
		
		this.bibRefMap = bibRefMap;
		
		// Add bibo ontology to ontology manager
		ontManager.addAltEntry( "bibo", "file:" + biboPath);
		
		// Read the ontologies
		ontolexModel.read("bibo");
		

		// Create objects for ontolex classes & properties
		BiboDocument = ontolexModel.getOntClass(bibo_+"Document");


		// Define prefix names for used namespaces
		model.setNsPrefix("rdf",rdf_);
		model.setNsPrefix("rdfs",rdfs_);
		model.setNsPrefix("owl",owl_);
		model.setNsPrefix("skos",skos_);
		model.setNsPrefix("dct",dct_);
		model.setNsPrefix("bib", base_);
		model.setNsPrefix("bibo", bibo_);
		model.setNsPrefix("talis", talis_);
	}
	
	
	/**
	 * Build the RDF model. Use hardcoded base URL
	 * @return success True / false
	 */
	public boolean buildModel() {
		return buildModel(base_);
	}
	
	/**
	 * Implementation of method buildModel
	 * @param base Base URL for entries
	 * @return success True / false
	 */
	public boolean buildModel(String base) {
		
		/*
		<urn:isbn:23983498> a bibo:Book ;
	    dc:creator <http://examples.net/contributors/2> ;
	    dc:title "Book Title"@en ;
	    dc:date "2000" ;
	    dc:publisher <http://ex.net/agents/1> .
		*/
		
		
		
		for(String hash: bibRefMap.keySet()) {
			
		//System.out.println(hash);
		
		try {
		// build url & save to bibRef object
		String bibUrl = base+hash.replaceAll(" ", "_");
		StarlingBibRef bibRef = bibRefMap.get(hash);
		bibRef.setUrl(bibUrl);
		
		// Create book resource
		Resource r = model.createResource(bibUrl);
		
		// add properties
		// because type of bib. (book, articel, etc.) not known
		r.addProperty(a, BiboDocument);
		
		// author
		// TODO : check conformity with rdf:List, rdf:Sequence 
		// range type of bibo/authorList
		r.addProperty(authorList, bibRef.getAuthor());
		
		// title
		if (!bibRef.getTitle().isEmpty())
		r.addProperty(title, bibRef.getTitle());
		
		// used in bases
		// TODO : add starling uri here instead of txt
		if (!bibRef.getUsedBy().isEmpty()) {
		//r.addProperty(isReferencedBy, bibRef.getUsedBy());
		r.addProperty(citedBy, bibRef.getUsedBy());
		}
		
		// abreviated name
		// TODO : use other property more appropriate
		if (!bibRef.getShortName().isEmpty())
		r.addProperty(shortDescription, bibRef.getShortName());
		
		// place
		if (!bibRef.getPlace().isEmpty())
		r.addProperty(place, bibRef.getPlace());
		
		// year
		if (!bibRef.getYear().isEmpty())
		r.addProperty(date, bibRef.getYear());
		
		} catch (Exception e) {
			e.printStackTrace();
			return false;}
		}
		return true;
		
		//System.out.println(bibRefMap.size());
		
	}
	
	/**
	 * Method not used
	 * @return false
	 */
	public boolean inititializeModel() {
		return false;
	}

	/**
	 * Serialize Jena model to RDF file
	 * @param outputPath
	 * @param format
	 * @return success True / false
	 */
	public boolean serialize(String outputPath, RDFFormat format) {
		
		try {
			RDFDataMgr.write(createOutputstreamWriter(outputPath,"UTF-8"), model,format);		
			return true;
			} catch (Exception e) {
			return false;
			}
	}
	

}