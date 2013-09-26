/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.graph;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.UniqueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.sanger.phenodigm2.dao.PhenoDigmDao;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseAssociation;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.model.MouseModel;
import uk.ac.sanger.phenodigm2.model.PhenotypeMatch;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;

/**
 * Builds the PhenoDigm graph database.
 * @author jj8
 */
public class PhenoDigmGraphBuilder {
    
    private static final Logger logger = Logger.getLogger(PhenoDigmGraphBuilder.class);

//    @Autowired
    PhenoDigmDao phenoDao;

    GraphDatabaseService graphDb;

    UniqueFactory<Node> ontologyTermFactory;
    UniqueFactory<Node> mouseModelFactory;
    UniqueFactory<Node> geneFactory;
    UniqueFactory<Node> diseaseFactory;
    
    //TODO: use proper Builder pattern to enable selective build and test of database
    
    public void buildGraphDatabase(PhenoDigmDao phenoDao, String dbPath) {
        
        this.phenoDao = phenoDao;
        
        startDb(dbPath);
//        clearDb();
        loadGeneIdentifiers();
        loadMouseModels();
        loadDiseases();
        loadOrthologousDiseases();
        loadPredictedDiseaseAssociations();
        //these next ones might make things really slow...
        loadDiseasePhenotypeTerms();
        loadMouseModelPhenotypeTerms();
        //this could taka long time too...
        loadHpMpMappings();
        
        shutDown();

    }
    
    private void loadGeneIdentifiers() {
        logger.info("Loading gene orthologs...");
        Transaction tx = graphDb.beginTx();
        try {
//            
            ontologyTermFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "ontology") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("termId", properties.get("termId"));
                }
            };

            mouseModelFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "mouseModels") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("modelId", properties.get("modelId"));
                }
            };

            geneFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "genes") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("geneId", properties.get("geneId"));
                }
            };

            diseaseFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "diseases") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("diseaseId", properties.get("diseaseId"));
                }
            };

            //make the ortholog associations
            for (GeneIdentifier mouseGeneIdentifier : phenoDao.getAllMouseGeneIdentifiers()) {
                //get the humn ortholog
                GeneIdentifier humanGeneIdentifier = phenoDao.getHumanOrthologIdentifierForMgiGeneId(mouseGeneIdentifier.getCompoundIdentifier());
                //add the new nodes to the graph database
                Node mouseGene = geneFactory.getOrCreate("geneId", mouseGeneIdentifier.getCompoundIdentifier());
                mouseGene.setProperty("geneSymbol", mouseGeneIdentifier.getGeneSymbol());
//                Label geneLabel = new DynamicNodeLabels(Fgfr2NodeId, null);

                Node humanGene = geneFactory.getOrCreate("geneId", humanGeneIdentifier.getCompoundIdentifier());
                humanGene.setProperty("geneSymbol", humanGeneIdentifier.getGeneSymbol());

                mouseGene.createRelationshipTo(humanGene, PhenoDigmRelationshipType.IS_ORTHOLOG_OF);
//                humanGene.createRelationshipTo(mouseGene, PhenoDigmRelationshipType.IS_HUMAN_ORTHOLOG_OF);

            }
            tx.success();
            logger.info("Done loading gene orthologs");
        } finally {
            tx.finish();
        }
        // END SNIPPET: transaction
    }

    private void loadMouseModels() {
        logger.info("Loading mouse models...");
        Transaction tx = graphDb.beginTx();
        try {
            ontologyTermFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "ontology") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("termId", properties.get("termId"));
                }
            };

            mouseModelFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "mouseModels") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("modelId", properties.get("modelId"));
                }
            };

            geneFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "genes") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("geneId", properties.get("geneId"));
                }
            };

            diseaseFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "diseases") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("diseaseId", properties.get("diseaseId"));
                }
            };

            Set<MouseModel> mouseModels = phenoDao.getAllMouseModels();
            for (MouseModel mouseModel : mouseModels) {

                Node modelNode = mouseModelFactory.getOrCreate("modelId", mouseModel.getMgiModelId());
                modelNode.setProperty("geneId", mouseModel.getMgiGeneId());
                //link the model to the gene 
                Node geneNode = geneFactory.getOrCreate("geneId", mouseModel.getMgiGeneId());
                modelNode.createRelationshipTo(geneNode, PhenoDigmRelationshipType.IS_MOUSE_MODEL_OF);
//                geneNode.createRelationshipTo(modelNode, PhenoDigmRelationshipType.HAS_MOUSE_MODEL);

            }
            tx.success();

        } finally {
            tx.finish();
        }

        logger.info("Done loading mouse models");
    }

    private void loadDiseases() {
        logger.info("Loading Diseases...");
        Transaction tx = graphDb.beginTx();
        try {
            ontologyTermFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "ontology") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("termId", properties.get("termId"));
                }
            };

            mouseModelFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "mouseModels") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("modelId", properties.get("modelId"));
                }
            };

            geneFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "genes") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("geneId", properties.get("geneId"));
                }
            };

            diseaseFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "diseases") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("diseaseId", properties.get("diseaseId"));
                }
            };
            for (Disease disease : phenoDao.getAllDiseses()) {
                //add the new nodes to the graph database
                Node diseaseNode = diseaseFactory.getOrCreate("diseaseId", disease.getDiseaseId());

                if (!diseaseNode.hasProperty("diseaseTerm")) {
                    diseaseNode.setProperty("diseaseTerm", disease.getTerm());
                }

                System.out.println("Made Disease node " + diseaseNode.getProperty("diseaseTerm"));

            }

            tx.success();
        } finally {
            tx.finish();
        }
        logger.info("Done loading diseases");

    }

    private void loadOrthologousDiseases() {
        logger.info("Loading orthologous gene disease associations...");
        Transaction tx = graphDb.beginTx();
        try {
            ontologyTermFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "ontology") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("termId", properties.get("termId"));
                }
            };

            mouseModelFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "mouseModels") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("modelId", properties.get("modelId"));
                }
            };

            geneFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "genes") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("geneId", properties.get("geneId"));
                }
            };

            diseaseFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "diseases") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("diseaseId", properties.get("diseaseId"));
                }
            };
            //make the ortholog associations
            for (Disease disease : phenoDao.getAllDiseses()) {
                //add the new nodes to the graph database
                Node diseaseNode = diseaseFactory.getOrCreate("diseaseId", disease.getDiseaseId());

                for (GeneIdentifier mouseGeneIdentifier : disease.getAssociatedMouseGenes()) {
                    //add the new nodes to the graph database
                    Node mouseGene = geneFactory.getOrCreate("geneId", mouseGeneIdentifier.getCompoundIdentifier());
                    if (!mouseGene.hasProperty("geneSymbol")) {
                        mouseGene.setProperty("geneSymbol", mouseGeneIdentifier.getGeneSymbol());
                        System.out.println("Made mouseGene node " + mouseGene.getProperty("geneSymbol"));
                    }

                    GeneIdentifier humanGeneIdentifier = phenoDao.getHumanOrthologIdentifierForMgiGeneId(mouseGeneIdentifier.getCompoundIdentifier());

                    Node humanGene = geneFactory.getOrCreate("geneId", humanGeneIdentifier.getCompoundIdentifier());
                    if (!humanGene.hasProperty("geneSymbol")) {
                        humanGene.setProperty("geneSymbol", humanGeneIdentifier.getGeneSymbol());
                        System.out.println("Made humanGene node " + humanGene.getProperty("geneSymbol"));
                    }

                    if (!mouseGene.hasRelationship(PhenoDigmRelationshipType.IS_ORTHOLOG_OF)) {
                        mouseGene.createRelationshipTo(humanGene, PhenoDigmRelationshipType.IS_ORTHOLOG_OF);
//                        humanGene.createRelationshipTo(mouseGene, PhenoDigmRelationshipType.IS_HUMAN_ORTHOLOG_OF);
                    }

                    mouseGene.createRelationshipTo(diseaseNode, PhenoDigmRelationshipType.IS_ASSOCIATED_WITH_BY_ORTHOLOGY);
                    humanGene.createRelationshipTo(diseaseNode, PhenoDigmRelationshipType.IS_ASSOCIATED_WITH);

//                    diseaseNode.createRelationshipTo(mouseGene, PhenoDigmRelationshipType.HAS_ORTHOLOGOUS_GENE_ASSOCIATION);
//                    diseaseNode.createRelationshipTo(humanGene, PhenoDigmRelationshipType.HAS_GENE_ASSOCIATION);
                }

            }
            tx.success();
            logger.info("Done loading orthologous gene disease associations.");
        } finally {
            tx.finish();
        }
    }

    private void loadDiseasePhenotypeTerms() {
        logger.info("Loading Disease phenotype terms...");
        Transaction tx = graphDb.beginTx();
        try {
            ontologyTermFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "ontology") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("termId", properties.get("termId"));
                }
            };

            mouseModelFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "mouseModels") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("modelId", properties.get("modelId"));
                }
            };

            geneFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "genes") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("geneId", properties.get("geneId"));
                }
            };

            diseaseFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "diseases") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("diseaseId", properties.get("diseaseId"));
                }
            };
            for (Disease disease : phenoDao.getAllDiseses()) {
                //add the new nodes to the graph database
                Node diseaseNode = diseaseFactory.getOrCreate("diseaseId", disease.getDiseaseId());

                if (!diseaseNode.hasProperty("diseaseTerm")) {
                    diseaseNode.setProperty("diseaseTerm", disease.getTerm());
                }
                //make disease ontology terms - SLOW!!!
                for (PhenotypeTerm term : phenoDao.getDiseasePhenotypeTerms(disease.getDiseaseId())) {
                    Node termNode = ontologyTermFactory.getOrCreate("termId", term.getTermId());
                    if (!termNode.hasProperty("termName")) {
                        termNode.setProperty("termName", term.getName());
                    }
                    termNode.createRelationshipTo(diseaseNode, PhenoDigmRelationshipType.IS_PHENOTYPE_OF);
//                    diseaseNode.createRelationshipTo(termNode, PhenoDigmRelationshipType.HAS_PHENOTYPE);
                }

            }

            tx.success();
        } finally {
            tx.finish();
        }
        logger.info("Done loading disease phenotype terms");

    }

    private void loadMouseModelPhenotypeTerms() {
        logger.info("Loading mouse model phenotypes...");
        Transaction tx = graphDb.beginTx();
        try {
            ontologyTermFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "ontology") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("termId", properties.get("termId"));
                }
            };

            mouseModelFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "mouseModels") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("modelId", properties.get("modelId"));
                }
            };

            geneFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "genes") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("geneId", properties.get("geneId"));
                }
            };

            diseaseFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "diseases") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("diseaseId", properties.get("diseaseId"));
                }
            };

            Set<MouseModel> mouseModels = phenoDao.getAllMouseModels();
            for (MouseModel mouseModel : mouseModels) {

                Node modelNode = mouseModelFactory.getOrCreate("modelId", mouseModel.getMgiModelId());
                if (!modelNode.hasProperty("geneId")) {
                    modelNode.setProperty("geneId", mouseModel.getMgiGeneId());
                }

                //make the phenotype term nodes -- SLOW
                List<PhenotypeTerm> mousePhenotypes = phenoDao.getMouseModelPhenotypeTerms(mouseModel.getMgiModelId());
                for (PhenotypeTerm term : mousePhenotypes) {
                    Node termNode = ontologyTermFactory.getOrCreate("termId", term.getTermId());
                    if (!termNode.hasProperty("termName")) {
                        termNode.setProperty("termName", term.getName());
                    }
                    //link model to phenotype
                    termNode.createRelationshipTo(modelNode, PhenoDigmRelationshipType.IS_PHENOTYPE_OF);
//                    modelNode.createRelationshipTo(termNode, PhenoDigmRelationshipType.HAS_PHENOTYPE);
                }
            }
            tx.success();

        } finally {
            tx.finish();
        }

        logger.info("Done loading mouse model phenotypes");
    }

    private void loadPredictedDiseaseAssociations() {
        logger.info("Loading predicted gene disease associations...");
        Transaction tx = graphDb.beginTx();
        try {
            ontologyTermFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "ontology") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("termId", properties.get("termId"));
                }
            };

            mouseModelFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "mouseModels") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("modelId", properties.get("modelId"));
                }
            };

            geneFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "genes") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("geneId", properties.get("geneId"));
                }
            };

            diseaseFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "diseases") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("diseaseId", properties.get("diseaseId"));
                }
            };
            for (Disease disease : phenoDao.getAllDiseses()) {
                //add the new nodes to the graph database
                Node diseaseNode = diseaseFactory.getOrCreate("diseaseId", disease.getDiseaseId());

                Map<GeneIdentifier, Set<DiseaseAssociation>> predictedDiseaseAssociations = phenoDao.getPredictedDiseaseAssociationsForDiseaseId(disease.getDiseaseId());
//                logger.info("Making Predicted disease associations for node " + diseaseNode.getProperty("diseaseTerm"));
                for (GeneIdentifier mouseGeneIdentifier : predictedDiseaseAssociations.keySet()) {

                    Node mouseGene = geneFactory.getOrCreate("geneId", mouseGeneIdentifier.getCompoundIdentifier());
                    if (!mouseGene.hasProperty("geneSymbol")) {
                        mouseGene.setProperty("geneSymbol", mouseGeneIdentifier.getGeneSymbol());
                        logger.info("Made mouseGene node " + mouseGene.getProperty("geneSymbol"));
                    }

                    GeneIdentifier humanGeneIdentifier = phenoDao.getHumanOrthologIdentifierForMgiGeneId(mouseGeneIdentifier.getCompoundIdentifier());
                    if (humanGeneIdentifier != null) {
                        Node humanGene = geneFactory.getOrCreate("geneId", humanGeneIdentifier.getCompoundIdentifier());
                        if (!humanGene.hasProperty("geneSymbol")) {
                            humanGene.setProperty("geneSymbol", humanGeneIdentifier.getGeneSymbol());
                            logger.info("Made humanGene node " + humanGene.getProperty("geneSymbol"));
                        }

                        if (!mouseGene.hasRelationship(PhenoDigmRelationshipType.IS_ORTHOLOG_OF)) {
                            mouseGene.createRelationshipTo(humanGene, PhenoDigmRelationshipType.IS_ORTHOLOG_OF);
    //                        humanGene.createRelationshipTo(mouseGene, PhenoDigmRelationshipType.IS_HUMAN_ORTHOLOG_OF);
                        }  
                    } else {
                        logger.info("No human ortholog found for " + mouseGeneIdentifier);
                    }
                    
                    //add the predicted disease associations
                    for (DiseaseAssociation disAssoc : predictedDiseaseAssociations.get(mouseGeneIdentifier)) {

                        Node predictedDisease = diseaseFactory.getOrCreate("diseaseId", disAssoc.getDiseaseIdentifier().getCompoundIdentifier());
                        //connect the predicted disease with the gene
//                        predictedDisease.createRelationshipTo(mouseGene, PhenoDigmRelationshipType.HAS_PREDICTED_GENE_ASSOCIATION);
                        //only want one IS_ASSOCIATED_WITH_BY_PREDICTION otherwise there will be as many of these as there are models
                        boolean hasPredictedRelationshipToDisease = false;
                        for (Relationship predictedRelationship : mouseGene.getRelationships(PhenoDigmRelationshipType.IS_ASSOCIATED_WITH_BY_PREDICTION)) {
                            if (predictedRelationship.getEndNode().equals(predictedDisease)) {
                                hasPredictedRelationshipToDisease = true;
                                break;
                            }
                        }
                        
                        if (! hasPredictedRelationshipToDisease) {
                            mouseGene.createRelationshipTo(predictedDisease, PhenoDigmRelationshipType.IS_ASSOCIATED_WITH_BY_PREDICTION);                        
                        }

                        //connect the predicted disease with the underlying mouse models for that gene
                        Node mouseModelNode = mouseModelFactory.getOrCreate("modelId", disAssoc.getMouseModel().getMgiModelId());
                        mouseModelNode.setProperty("geneSymbol", mouseGeneIdentifier.getGeneSymbol());
                        Relationship rel = mouseModelNode.createRelationshipTo(predictedDisease, PhenoDigmRelationshipType.IS_PREDICTED_MODEL_OF);
                        rel.setProperty("modelToDiseaseScore", disAssoc.getModelToDiseaseScore());
                        rel.setProperty("diseaseToModelScore", disAssoc.getDiseaseToModelScore());
//                        predictedDisease.createRelationshipTo(mouseModelNode, PhenoDigmRelationshipType.HAS_PREDICTED_MODEL).setProperty("diseaseToModelScore", disAssoc.getDiseaseToModelScore());
                    }
                }
            }
            tx.success();
        } finally {
            tx.finish();
        }

        logger.info("Done loading predicted diseases");
    }

    private void loadHpMpMappings() {
        logger.info("Loading HP - MP mappings...");
        Transaction tx = graphDb.beginTx();
        try {
            ontologyTermFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "ontology") {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("termId", properties.get("termId"));
                }
            };

            //make the HP-MP associations TODO: This is NOT the way to do it efficiently - this should be a single query off the HP-MP mappings table which I can't see yet.
            for (Disease disease : phenoDao.getAllDiseses()) {
                //add the new nodes to the graph database
                //make predicted HP-MP mappings
                Map<GeneIdentifier, Set<DiseaseAssociation>> predictedDiseaseAssociations = phenoDao.getPredictedDiseaseAssociationsForDiseaseId(disease.getDiseaseId());
                for (GeneIdentifier mouseGeneIdentifier : predictedDiseaseAssociations.keySet()) {                    
                    //add the predicted disease associations
                    for (DiseaseAssociation disAssoc : predictedDiseaseAssociations.get(mouseGeneIdentifier)) {
//                        logger.info("Loading predicted disease HP-MP mappings " + disease.getDiseaseId() + " -> " + disAssoc.getMouseModel().getMgiModelId());
                        
                        List<PhenotypeMatch> phenotypeMatchList = phenoDao.getPhenotypeMatches(disease.getDiseaseId(), disAssoc.getMouseModel().getMgiModelId());
                        for (PhenotypeMatch phenotypeMatch : phenotypeMatchList) {
                            Node hp = ontologyTermFactory.getOrCreate("termId", phenotypeMatch.getHumanPhenotype().getTermId());
                            Node mp = ontologyTermFactory.getOrCreate("termId", phenotypeMatch.getMousePhenotype().getTermId());
                            //matches between MP and HP nodes can be many to many so check that the match exists from the mp node
                            boolean hasMatch = false;
                            for (Relationship relationship : hp.getRelationships(PhenoDigmRelationshipType.PHENOTYPE_MATCH)) {
                                if (relationship.getEndNode().equals(mp)) {
                                    hasMatch = true;
                                    break;
                                }
                            }
                            if (! hasMatch) {
                                Relationship rel = hp.createRelationshipTo(mp, PhenoDigmRelationshipType.PHENOTYPE_MATCH);
                                rel.setProperty("IC", phenotypeMatch.getIc());
                                rel.setProperty("SimJ", phenotypeMatch.getSimJ());
                            }
                        }
                    }
                }
                
                logger.info("Loading known disease HP-MP mappings for " + disease.getDiseaseId());

                //make known HP-MP mappings
                Map<GeneIdentifier, Set<DiseaseAssociation>> knownDiseaseAssociations = phenoDao.getKnownDiseaseAssociationsForDiseaseId(disease.getDiseaseId());
                for (GeneIdentifier mouseGeneIdentifier : knownDiseaseAssociations.keySet()) {                    
                    //add the predicted disease associations
                    for (DiseaseAssociation disAssoc : knownDiseaseAssociations.get(mouseGeneIdentifier)) {
//                        logger.info("Loading known disease HP-MP mappings " + disease.getDiseaseId() + " -> " + disAssoc.getMouseModel().getMgiModelId());
                        
                        List<PhenotypeMatch> phenotypeMatchList = phenoDao.getPhenotypeMatches(disease.getDiseaseId(), disAssoc.getMouseModel().getMgiModelId());
                        for (PhenotypeMatch phenotypeMatch : phenotypeMatchList) {
                            Node hp = ontologyTermFactory.getOrCreate("termId", phenotypeMatch.getHumanPhenotype().getTermId());
                            Node mp = ontologyTermFactory.getOrCreate("termId", phenotypeMatch.getMousePhenotype().getTermId());
                            //matches between MP and HP nodes can be many to many so check that the match exists from the hp node
                            boolean hasMatch = false;
                            for (Relationship relationship : hp.getRelationships(PhenoDigmRelationshipType.PHENOTYPE_MATCH)) {
                                if (relationship.getEndNode().equals(mp)) {
                                    hasMatch = true;
                                    break;
                                }
                            }
                            if (! hasMatch) {
                                Relationship rel = hp.createRelationshipTo(mp, PhenoDigmRelationshipType.PHENOTYPE_MATCH);
                                rel.setProperty("IC", phenotypeMatch.getIc());
                                rel.setProperty("SimJ", phenotypeMatch.getSimJ());
                            }
                        }
                    }
                }
            }
            tx.success();
        } finally {
            tx.finish();
        }
        logger.info("Done loading HP - MP mappings.");
    }
    
    private void startDb(String dbPath) {
        System.out.println("Starting up database...");
        graphDb = new EmbeddedNeo4j().getDatbase(dbPath, null);
        registerShutdownHook(graphDb);
        System.out.println("Started database: " + graphDb.toString());
    }

    private void shutDown() {
        System.out.println();
        System.out.println("Shutting down database ...");

        graphDb.shutdown();

    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }
}
