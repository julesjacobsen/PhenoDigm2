/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.graph;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;

/**
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class EmbeddedNeo4j {
    
    private String dbPath;

    private GraphDatabaseService graphDb;

    public EmbeddedNeo4j() {
        
    }

    public GraphDatabaseService getDatbase(String dbPath, GraphDatabaseSettings graphDbSettings) {
        this.dbPath = dbPath;
        System.out.println("Starting database at location " + this.dbPath);
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder( this.dbPath ).
        setConfig( GraphDatabaseSettings.node_keys_indexable, "geneId, geneSymbol, diseaseId, diseaseTerm, termId, termName, modelId" ).
        setConfig( GraphDatabaseSettings.node_auto_indexing, "true" ).
        setConfig( GraphDatabaseSettings.relationship_keys_indexable, "IC, SimJ, diseaseToModelScore, modelToDiseaseScore" ).
        setConfig( GraphDatabaseSettings.relationship_auto_indexing, "true" ).
        newGraphDatabase();
        registerShutdownHook( graphDb );
        return graphDb;
    }
    
    public void shutDown()
    {
        System.out.println("Shutting down database at location " + dbPath);
        graphDb.shutdown();
    }

    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
}