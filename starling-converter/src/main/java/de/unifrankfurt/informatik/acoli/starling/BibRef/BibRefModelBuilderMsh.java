package de.unifrankfurt.informatik.acoli.starling.BibRef;

import static de.unifrankfurt.informatik.acoli.starling.util.Utils.createOutputstreamWriter;

import java.util.HashMap;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import de.unifrankfurt.informatik.acoli.starling.types.StarlingBibRef;


/**
 * Jena model builder which uses the Miskinhill bibliographic ontology
 * see <a href="https://miskinhill.com.au/rdfschema/1.0/">https://miskinhill.com.au/rdfschema/1.0/</a> 
 * @author frank
 */
public class BibRefModelBuilderMsh {
	
	// Variables used by the model builder
	HashMap <String, StarlingBibRef> bibRefMap;

	// Jena model, ontology model and ontology manager
	Model model = ModelFactory.createDefaultModel();
	OntModel ontolexModel = ModelFactory.createOntologyModel();
	//OntDocumentManager ontManager = ontolexModel.getDocumentManager();
	
	// define namespaces
	final String rdf_ = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	final String rdfs_ = "http://www.w3.org/2000/01/rdf-schema#";
	final String owl_ = "http://www.w3.org/2002/07/owl#";
	final String skos_ = "http://www.w3.org/2004/02/skos/core#";
	final String dc_ = "http://purl.org/dc/elements/1.1/";
	final String dct_ = "http://purl.org/dc/terms/";
	final String base_ = "http://acoli.cs.uni-frankfurt.de/starling/bib/";
	final String msh_ = "http://miskinhill.com.au/rdfschema/1.0/";
	final String talis_ = "http://schemas.talis.com/2005/address/schema#";
	Property a = model.createProperty(rdf_+"type");
	Property date = model.createProperty(dct_+"date");
	Property title = model.createProperty(dc_+"title");
	Property identifier = model.createProperty(dc_+"identifier");
	Property creator = model.createProperty(dc_+"creator");
	Property isReferencedBy = model.createProperty(dct_+"isReferencedBy");
	Property localityName = model.createProperty(talis_+"localityName");

	
	// constructor
	public BibRefModelBuilderMsh (HashMap <String, StarlingBibRef> bibRefMap) {
		
		this.bibRefMap = bibRefMap;

		// Define prefix names for used namespaces
		model.setNsPrefix("rdf",rdf_);
		model.setNsPrefix("rdfs",rdfs_);
		model.setNsPrefix("owl",owl_);
		model.setNsPrefix("skos",skos_);
		model.setNsPrefix("dc",dc_);
		model.setNsPrefix("dct",dct_);
		model.setNsPrefix("bib", base_);
		model.setNsPrefix("msh",msh_);
		model.setNsPrefix("talis", talis_);

	}
	
	
	/**
	 * Build the RDF model. Use hardcoded base url
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
		// (subclass of cito:book, bibo:book, dct:BibliographicResource
		r.addProperty(a, model.createResource(msh_+"Book"));
		
		// author
		// TODO : split multiple authors 
		// range type of bibo/authorList
		r.addProperty(creator, bibRef.getAuthor());
		
		// title
		if (!bibRef.getTitle().isEmpty())
		r.addProperty(title, bibRef.getTitle());
		
		// used in bases
		// TODO : add starling uri here instead of txt
		if (!bibRef.getUsedBy().isEmpty()) {
		r.addProperty(isReferencedBy, bibRef.getUsedBy());
		}
		
		// abreviated name (is key)
		if (!bibRef.getShortName().isEmpty())
		r.addProperty(identifier, bibRef.getShortName());
		
		// place
		if (!bibRef.getPlace().isEmpty())
		r.addProperty(localityName, bibRef.getPlace());
		
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
		RDFDataMgr.write(createOutputstreamWriter(outputPath,"UTF-8"), model, format);		
		return true;
		} catch (Exception e) {
		return false;
		}
	}
	

}