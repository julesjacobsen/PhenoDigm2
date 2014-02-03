/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.graph;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.neo4j.graphdb.GraphDatabaseService;
//import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.UniqueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
//import uk.ac.sanger.phenodigm2.dao.PhenoDigmDao;
//import uk.ac.sanger.phenodigm2.model.Disease;
//import uk.ac.sanger.phenodigm2.model.DiseaseModelAssociation;
//import uk.ac.sanger.phenodigm2.model.Gene;
//import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
//import uk.ac.sanger.phenodigm2.model.MouseModel;
//import uk.ac.sanger.phenodigm2.model.PhenotypeMatch;
//import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;
//import uk.ac.sanger.phenodigm2.web.AssociationSummary;
//import uk.ac.sanger.phenodigm2.web.GeneAssociationSummary;

/**
 * Builds the PhenoDigm graph database.
 *
 * @author jj8
 */
public class PhenoDigmGraphBuilder {

    private static final Logger logger = LoggerFactory.getLogger(PhenoDigmGraphBuilder.class);

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    GraphDatabaseService graphDb;

    static UniqueFactory<Node> hpTermFactory;
    static UniqueFactory<Node> mpTermFactory;
    static UniqueFactory<Node> mouseModelFactory;
    static UniqueFactory<Node> geneFactory;
    static UniqueFactory<Node> diseaseFactory;

    public void buildGraphDatabase() {
        logger.info(jdbcTemplate.toString());
        logger.info(graphDb.toString());
        startDb(graphDb);
//        clearDb();
        loadGeneOrthologs();
        loadMouseModels();
        loadDiseases();
        loadHpTerms();
        loadMpTerms();        
//        loadOrthologousDiseases();
//        loadDiseaseGeneAssociations();
//        //these next ones might make things really slow...
//        loadDiseasePhenotypeTerms();
//        loadMouseModelPhenotypeTerms();
//        //this could take a long time too...
//        loadHpMpMappings();

        shutDown();

    }

    public void loadGeneOrthologs() {
        logger.info("Loading gene orthologs...");

        try (Transaction tx = graphDb.beginTx()) {

            initUniqueNodeFactories();

            String sql = "select model_gene_id, model_gene_symbol, hgnc_id, hgnc_gene_symbol, ifnull(hgnc_gene_locus, '-') as hgnc_gene_locus from mouse_gene_ortholog";
            
            this.jdbcTemplate.query(sql, new GeneOrthologRowCallbackHandler());
            
            tx.success();
            logger.info("Done loading gene orthologs");
        }
    }
    
    public void loadHpTerms() {
        logger.info("Loading HP terms...");

        try (Transaction tx = graphDb.beginTx()) {

            initUniqueNodeFactories();

            String sql = "select hp_id, term from hp";
            
            this.jdbcTemplate.query(sql, new PhenotypeRowCallbackHandler(hpTermFactory, "hpId", "hp_id"));
            
            tx.success();
            logger.info("Done loading HP terms");
        }
    }
    
        public void loadMpTerms() {
        logger.info("Loading MP terms...");

        try (Transaction tx = graphDb.beginTx()) {

            initUniqueNodeFactories();

            String sql = "select mp_id, term from mp";
            
            this.jdbcTemplate.query(sql, new PhenotypeRowCallbackHandler(mpTermFactory, "mpId", "mp_id"));
            
            tx.success();
            logger.info("Done loading MP terms");
        }
    }


    public void loadMouseModels() {
        logger.info("Loading mouse models...");
        try (Transaction tx = graphDb.beginTx()) {
            initUniqueNodeFactories();

            String sql = "select model_id, source, allelic_composition, genetic_background, ifnull(allelic_composition_link, '') as  allelic_composition_link, hom_het from mouse_model;";

            this.jdbcTemplate.query(sql, new MouseModelRowCallbackHandler());

                //link the model to the gene 
//                Node geneNode = geneFactory.getOrCreate("geneId", mouseModel.getMgiGeneId());
//                modelNode.createRelationshipTo(geneNode, PhenoDigmRelationshipType.IS_MOUSE_MODEL_OF);
            
            tx.success();

        }

        logger.info("Done loading mouse models");
    }

    public void loadDiseases() {
        logger.info("Loading Diseases...");
        try (Transaction tx = graphDb.beginTx()) {
            initUniqueNodeFactories();

            String sql = "select disease_id, disease_term, disease_alts, ifnull(disease_locus, '') as disease_locus, disease_classes from disease;";

            this.jdbcTemplate.query(sql, new DiseasRowCallbackHandler());

            tx.success();
        }
        logger.info("Done loading diseases");

    }

    private void initUniqueNodeFactories() {
        hpTermFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "hp") {
            @Override
            protected void initialize(Node created, Map<String, Object> properties) {
                created.setProperty("hpId", properties.get("hpId"));
                created.addLabel(PhenoDigmLabel.HP);
            }
        };
        
        mpTermFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "mp") {
            @Override
            protected void initialize(Node created, Map<String, Object> properties) {
                created.setProperty("mpId", properties.get("mpId"));
                created.addLabel(PhenoDigmLabel.MP);
            }
        };

        mouseModelFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "mouseModels") {
            @Override
            protected void initialize(Node created, Map<String, Object> properties) {
                created.setProperty("modelId", properties.get("modelId"));
                created.addLabel(PhenoDigmLabel.MouseModel);
            }
        };

        geneFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "genes") {
            @Override
            protected void initialize(Node created, Map<String, Object> properties) {
                created.setProperty("geneId", properties.get("geneId"));
                created.addLabel(PhenoDigmLabel.Gene);
            }
        };

        diseaseFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "diseases") {
            @Override
            protected void initialize(Node created, Map<String, Object> properties) {
                created.setProperty("diseaseId", properties.get("diseaseId"));
                created.addLabel(PhenoDigmLabel.Disease);
            }
        };
    }

    private void startDb(GraphDatabaseService graphDb) {
        System.out.println("Starting up database...");
//        graphDb = new EmbeddedNeo4j().getDatbase(dbPath, null);
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

    private static class DiseasRowCallbackHandler implements RowCallbackHandler {

        public DiseasRowCallbackHandler() {
        }

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int nodes = 0;
            while (rs.next()) {
                //add the new nodes to the graph database
                Node diseaseNode = diseaseFactory.getOrCreate("diseaseId", rs.getString("disease_id"));

                diseaseNode.setProperty("diseaseTerm", rs.getString("disease_term"));
                diseaseNode.setProperty("diseaseAlts", rs.getString("disease_alts"));
                diseaseNode.setProperty("diseaseLocus", rs.getString("disease_locus"));
                diseaseNode.setProperty("diseaseClasses", rs.getString("disease_classes"));

                nodes++;
                logger.debug("Made Disease node {} {}", diseaseNode.getProperty("diseaseId"), diseaseNode.getProperty("diseaseTerm"));

            }
            logger.info("Made {} disease nodes", nodes);

        }
    }

    private static class MouseModelRowCallbackHandler implements RowCallbackHandler {

        public MouseModelRowCallbackHandler() {
        }

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int nodes = 0;
            logger.info("Creating mouse_model nodes");

            while (rs.next()) {
                Node modelNode = mouseModelFactory.getOrCreate("modelId", rs.getInt("model_id"));
                modelNode.setProperty("source", rs.getString("source"));
                modelNode.setProperty("allelicComposition", rs.getString("allelic_composition"));
                modelNode.setProperty("geneticBackground", rs.getString("genetic_background"));
                modelNode.setProperty("allelicCompositionLink", rs.getString("allelic_composition_link"));
                modelNode.setProperty("homHet", rs.getString("hom_het"));
                nodes++;
            }
            logger.info("Made {} mouse_model nodes", nodes);
        }
    }

    private static class GeneOrthologRowCallbackHandler implements RowCallbackHandler {

        public GeneOrthologRowCallbackHandler() {
        }

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int mouseNodes = 0;
            int humanNodes = 0;
            logger.info("Making gene orthologs");
            while(rs.next()) {
                
                Node mouseGene = geneFactory.getOrCreate("geneId", rs.getString("model_gene_id"));
                mouseGene.setProperty("geneSymbol", rs.getString("model_gene_symbol"));
                mouseNodes++;
                
                String hgnc_id = rs.getString("hgnc_id");
                if (hgnc_id != null) {
                    Node humanGene = geneFactory.getOrCreate("geneId", hgnc_id);
                    humanGene.setProperty("geneSymbol", rs.getString("hgnc_gene_symbol"));
                    humanGene.setProperty("geneLocus", rs.getString("hgnc_gene_locus"));
                    humanNodes++;
                    mouseGene.createRelationshipTo(humanGene, PhenoDigmRelationshipType.IS_ORTHOLOG_OF);
                }                
            }
            logger.info("Made {} mouse gene nodes mapped to {} human gene nodes", mouseNodes, humanNodes);

        }
    }
    
    private static class PhenotypeRowCallbackHandler implements RowCallbackHandler {
        
        UniqueFactory<Node> termFactory;
        String uniqueId;
        String columnName;
        
        public PhenotypeRowCallbackHandler(UniqueFactory<Node> termFactory, String uniqueId, String columnName) {
            this.termFactory = termFactory;
            this.uniqueId = uniqueId;
            this.columnName = columnName;
        }

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int nodes = 0;
            while (rs.next()) {
                //add the new nodes to the graph database
                logger.debug("{} {} {}",termFactory, uniqueId, rs.getString(columnName));
                
                Node phenotypeNode = termFactory.getOrCreate(uniqueId, rs.getString(columnName));

                phenotypeNode.setProperty("term", rs.getString("term"));
                
                nodes++;
                logger.debug("Made {} node {} {}", uniqueId, phenotypeNode.getProperty(uniqueId), phenotypeNode.getProperty("term"));

            }
            logger.info("Made {} {} nodes", nodes, uniqueId);

        }
    }
}
