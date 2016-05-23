package de.unifrankfurt.informatik.acoli.starling.BibRef;

import org.apache.jena.riot.RDFFormat;



/**
 * Start conversion of bibliographic references to RDF
 * @author frank
 *
 */
public class BibRefRun {
	

	/**
	 * Convert bibliographic references to RDF file.
	 * References in a RDF Starling dictionary point to entities in
	 * this file !
	 * @return BibRefReader instance
	 */
	public static BibRefFileReader main(String[] args) {
		
		// Bibref input file is constant
		String inputFile = "/input-files/starbib.txt";
		//String inputFile = System.getProperty("user.dir")+"/input-files/starbib_edited.txt";
		//String inputFile = System.getProperty("user.dir")+"/input-files/empty.txt";

		// BibRef RDF output file
		String outputFile = args[0];
		//String outputFile = Utils.resourceDir+"/bibref";
		//String outputFile = System.getProperty("user.dir")+"/output-files/bibref";
		
		
		// Read file and create Jena model from it
		BibRefFileReader reader = new BibRefFileReader();
		boolean ok = reader.readCP(inputFile);
		
		if (ok) {
		BibRefModelBuilderMsh modelBuilder = 
				new BibRefModelBuilderMsh(reader.getMap());
		modelBuilder.buildModel();
		
		// Serialize the model
		modelBuilder.serialize(outputFile, RDFFormat.TURTLE_BLOCKS);
		} else {
			System.out.println("Error reading bib file");
		}
		
		return reader;
		
	}
}

