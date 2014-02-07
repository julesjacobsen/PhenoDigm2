/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.graph;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;

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

    static BatchUniqueFactory<Node> hpTermFactory;
    static BatchUniqueFactory<Node> mpTermFactory;
    static BatchUniqueFactory<Node> mouseModelFactory;
    static BatchUniqueFactory<Node> geneFactory;
    static BatchUniqueFactory<Node> diseaseFactory;

    static BatchInserter batchInserter;

    public PhenoDigmGraphBuilder() {
    }
    
    public void buildGraphDatabase() {
        logger.info(jdbcTemplate.toString());
        logger.info(graphDb.toString());

        jdbcTemplate.setFetchSize(Integer.MIN_VALUE);
        startDb(graphDb);
//        clearDb();
        //set up the unique node factories
        initUniqueBatchNodeFactories();

        //load in the nodes
        loadGeneOrthologs();
        loadMouseModels();
        loadDiseases();
        loadHpTerms();
        loadMpTerms();
        //now load in the relationships
        loadDiseasePhenotypeRelationships();
        loadMouseModelPhenotypeRelationships();
        loadMouseModelGeneRelationships();
        //this is a big one
        loadHpMpRelationships();
        //this is THE biggie - 118M relationships 
        loadDiseaseMouseModelRelationships();
//        loadOrthologousDiseases();
//        loadDiseaseGeneAssociations();
        shutDown();

    }

//    private void initUniqueNodeFactories() {
//        logger.info("Initializing unique node factories");
//        try (Transaction tx = graphDb.beginTx()) {
//
//            hpTermFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "hp") {
//                @Override
//                protected void initialize(Node created, Map<String, Object> properties) {
//                    created.setProperty("hp_id", properties.get("hp_id"));
//                    created.addLabel(PhenoDigmLabel.HP);
//                }
//            };
//
//            mpTermFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "mp") {
//                @Override
//                protected void initialize(Node created, Map<String, Object> properties) {
//                    created.setProperty("mp_id", properties.get("mp_id"));
//                    created.addLabel(PhenoDigmLabel.MP);
//                }
//            };
//
//            mouseModelFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "mouseModels") {
//                @Override
//                protected void initialize(Node created, Map<String, Object> properties) {
//                    created.setProperty("model_id", properties.get("model_id"));
//                    created.addLabel(PhenoDigmLabel.MouseModel);
//                }
//            };
//
//            geneFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "genes") {
//                @Override
//                protected void initialize(Node created, Map<String, Object> properties) {
//                    created.setProperty("gene_id", properties.get("gene_id"));
//                    created.addLabel(PhenoDigmLabel.Gene);
//                }
//            };
//
//            diseaseFactory = new UniqueFactory.UniqueNodeFactory(graphDb, "diseases") {
//                @Override
//                protected void initialize(Node created, Map<String, Object> properties) {
//                    created.setProperty("disease_id", properties.get("disease_id"));
//                    created.addLabel(PhenoDigmLabel.Disease);
//                }
//            };
//            tx.success();
//        }
//        logger.info("Unique node factories - initialized.");
//
//    }

    private void initUniqueBatchNodeFactories() {
        logger.info("Initializing unique node factories");
            hpTermFactory = new BatchUniqueFactory.UniqueNodeFactory(graphDb, new ConcurrentHashMap<Object, Node>()) {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("hp_id", properties.get("hp_id"));
                    created.addLabel(PhenoDigmLabel.HP);
                }
            };

            mpTermFactory = new BatchUniqueFactory.UniqueNodeFactory(graphDb, new ConcurrentHashMap<Object, Node>()) {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("mp_id", properties.get("mp_id"));
                    created.addLabel(PhenoDigmLabel.MP);
                }
            };

            mouseModelFactory = new BatchUniqueFactory.UniqueNodeFactory(graphDb, new ConcurrentHashMap<Object, Node>()) {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("model_id", properties.get("model_id"));
                    created.addLabel(PhenoDigmLabel.MouseModel);
                }
            };

            geneFactory = new BatchUniqueFactory.UniqueNodeFactory(graphDb, new ConcurrentHashMap<Object, Node>()) {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("gene_id", properties.get("gene_id"));
                    created.addLabel(PhenoDigmLabel.Gene);
                }
            };

            diseaseFactory = new BatchUniqueFactory.UniqueNodeFactory(graphDb, new ConcurrentHashMap<Object, Node>()) {
                @Override
                protected void initialize(Node created, Map<String, Object> properties) {
                    created.setProperty("disease_id", properties.get("disease_id"));
                    created.addLabel(PhenoDigmLabel.Disease);
                }
            };
           
        logger.info("Unique node factories - initialized.");

    }

    private void startDb(GraphDatabaseService graphDb) {
        logger.info("Starting up database...");
        registerShutdownHook(graphDb);
        System.out.println("Started database: " + graphDb.toString());
    }

    private void shutDown() {
        logger.info("Shutting down database ...");
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

    public void loadGeneOrthologs() {
        logger.info("Loading gene ortholog nodes");

        try (Transaction tx = graphDb.beginTx()) {

            String sql = "select model_gene_id, model_gene_symbol, hgnc_id, hgnc_gene_symbol, ifnull(hgnc_gene_locus, '-') as hgnc_gene_locus from mouse_gene_ortholog";

            this.jdbcTemplate.query(new StreamingStatementCreator(sql), new GeneOrthologRowCallbackHandler());

            tx.success();
        }
    }

    public void loadHpTerms() {
        logger.info("Loading HP nodes");

        try (Transaction tx = graphDb.beginTx()) {

            String sql = "select hp_id, term from hp";

            this.jdbcTemplate.query(new StreamingStatementCreator(sql), new PhenotypeRowCallbackHandler(hpTermFactory, "hp_id", "hp_id"));

            tx.success();
        }
    }

    public void loadMpTerms() {
        logger.info("Loading MP nodes");

        try (Transaction tx = graphDb.beginTx()) {

            String sql = "select mp_id, term from mp";

            this.jdbcTemplate.query(new StreamingStatementCreator(sql), new PhenotypeRowCallbackHandler(mpTermFactory, "mp_id", "mp_id"));

            tx.success();
        }
    }

    public void loadMouseModels() {
        logger.info("Loading mouse models nodes");
        try (Transaction tx = graphDb.beginTx()) {

            String sql = "select model_id, source, allelic_composition, genetic_background, ifnull(allelic_composition_link, '') as  allelic_composition_link, hom_het from mouse_model;";

            this.jdbcTemplate.query(new StreamingStatementCreator(sql), new MouseModelRowCallbackHandler());

            tx.success();

        }
    }

    public void loadDiseases() {
        logger.info("Loading Diseases nodes");
        try (Transaction tx = graphDb.beginTx()) {

            String sql = "select disease_id, disease_term, disease_alts, ifnull(disease_locus, '') as disease_locus, disease_classes from disease;";

            this.jdbcTemplate.query(new StreamingStatementCreator(sql), new DiseasRowCallbackHandler());

            tx.success();
        }
    }

    public void loadDiseasePhenotypeRelationships() {
        logger.info("Loading disease-HP relationships");
        try (Transaction tx = graphDb.beginTx()) {

            String sql = "select disease_id, hp_id from disease_hp";
            jdbcTemplate.query(new StreamingStatementCreator(sql), new DiseasePhenotypeRowCallbackHandler());

            tx.success();
        }
    }

    public void loadMouseModelPhenotypeRelationships() {
        logger.info("Loading mouse model-MP relationships");

        try (Transaction tx = graphDb.beginTx()) {

            String sql = "select model_id, mp_id from mouse_model_mp";
            jdbcTemplate.query(new StreamingStatementCreator(sql), new MouseModelPhenotypeRowCallbackHandler());

            tx.success();
        }
    }

    private void loadMouseModelGeneRelationships() {
        logger.info("Loading mouse model-gene relationships");

        try (Transaction tx = graphDb.beginTx()) {

            String sql = "select model_id, model_gene_id from mouse_model_gene_ortholog";
            jdbcTemplate.query(new StreamingStatementCreator(sql), new MouseModelGeneOrthologRowCallbackHandler());

            tx.success();
        }
    }

    private void loadHpMpRelationships() {
        logger.info("Loading hp-mp relationships");
        
        String sql = "select hp_id, mp_id, ifnull(simJ, 0.0) as simJ, ifnull(ic, 0.0) as ic, ifnull(lcs, '') as lcs from hp_mp_mapping";
        jdbcTemplate.query(new StreamingStatementCreator(sql), new HpMpRowCallbackHandler());

    }

    private void loadDiseaseMouseModelRelationships() {
        logger.info("Loading disease-mouse model relationships");
        
        String sql = "select disease_id, model_id, lit_model, disease_to_model_perc_score, model_to_disease_perc_score, raw_score, hp_matched_terms, mp_matched_terms from mouse_disease_model_association";
        jdbcTemplate.query(new StreamingStatementCreator(sql), new DiseaseMouseModelRowCallbackHandler());
    }
    
    /**
     * Class for getting around the problem of the MySQL JDBC driver not streaming
     * results when using Spring. 
     * 
     * This is due to the driver not taking the hint Statement#setFetchSize() 
     * which results in an OutOfMemory exception when trying to do a select * from
     * large tables.
     * 
     * This code is lifted from here: 
     * http://stackoverflow.com/a/2834590
     * 
     */
    protected class StreamingStatementCreator implements PreparedStatementCreator {

        private final String sql;

        public StreamingStatementCreator(String sql) {
            this.sql = sql;
        }

        @Override
        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
            final PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            statement.setFetchSize(Integer.MIN_VALUE);
            return statement;
        }
    }

    private static class DiseasRowCallbackHandler implements RowCallbackHandler {

        public DiseasRowCallbackHandler() {
        }

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            //add the new nodes to the graph database
            Node diseaseNode = diseaseFactory.getOrCreate("disease_id", rs.getString("disease_id"));

            diseaseNode.setProperty("disease_term", rs.getString("disease_term"));
            diseaseNode.setProperty("disease_alts", rs.getString("disease_alts"));
            diseaseNode.setProperty("disease_locus", rs.getString("disease_locus"));
            diseaseNode.setProperty("disease_classes", rs.getString("disease_classes"));

            logger.debug("Made Disease node {} {}", diseaseNode.getProperty("disease_id"), diseaseNode.getProperty("disease_term"));
        }
    }

    private static class MouseModelRowCallbackHandler implements RowCallbackHandler {

        public MouseModelRowCallbackHandler() {
        }

        @Override
        public void processRow(ResultSet rs) throws SQLException {

            Node modelNode = mouseModelFactory.getOrCreate("model_id", rs.getInt("model_id"));
            modelNode.setProperty("source", rs.getString("source"));
            modelNode.setProperty("allelic_composition", rs.getString("allelic_composition"));
            modelNode.setProperty("genetic_background", rs.getString("genetic_background"));
            modelNode.setProperty("allelic_composition_link", rs.getString("allelic_composition_link"));
            modelNode.setProperty("hom_het", rs.getString("hom_het"));

        }
    }

    private static class GeneOrthologRowCallbackHandler implements RowCallbackHandler {

        public GeneOrthologRowCallbackHandler() {
        }

        @Override
        public void processRow(ResultSet rs) throws SQLException {

            Node mouseGene = geneFactory.getOrCreate("gene_id", rs.getString("model_gene_id"));
            mouseGene.setProperty("gene_symbol", rs.getString("model_gene_symbol"));

            String hgnc_id = rs.getString("hgnc_id");
            if (hgnc_id != null) {
                Node humanGene = geneFactory.getOrCreate("gene_id", hgnc_id);
                humanGene.setProperty("gene_symbol", rs.getString("hgnc_gene_symbol"));
                humanGene.setProperty("gene_locus", rs.getString("hgnc_gene_locus"));
                mouseGene.createRelationshipTo(humanGene, PhenoDigmRelationshipType.ORTHOLOG_OF);
            }
        }
    }

    private static class PhenotypeRowCallbackHandler implements RowCallbackHandler {

        BatchUniqueFactory<Node> termFactory;
        String uniqueId;
        String columnName;

        public PhenotypeRowCallbackHandler(BatchUniqueFactory<Node> termFactory, String uniqueId, String columnName) {
            this.termFactory = termFactory;
            this.uniqueId = uniqueId;
            this.columnName = columnName;
        }

        @Override
        public void processRow(ResultSet rs) throws SQLException {

            logger.debug("{} {} {}", termFactory, uniqueId, rs.getString(columnName));

            String term_id = rs.getString(columnName);
            Node phenotypeNode = termFactory.getOrCreate(uniqueId, term_id);
            phenotypeNode.setProperty("term", rs.getString("term"));
            
            logger.debug("Made {} node {} {}", phenotypeNode.getId(), phenotypeNode.getProperty(uniqueId), phenotypeNode.getProperty("term"));
        }
    }

    private static class DiseasePhenotypeRowCallbackHandler implements RowCallbackHandler {

        public DiseasePhenotypeRowCallbackHandler() {
        }

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            Node phenotypeNode = hpTermFactory.getOrCreate("hp_id", rs.getString("hp_id"));
            Node diseaseNode = diseaseFactory.getOrCreate("disease_id", rs.getString("disease_id"));

            phenotypeNode.createRelationshipTo(diseaseNode, PhenoDigmRelationshipType.PHENOTYPE_OF);
        }
    }

    private static class MouseModelPhenotypeRowCallbackHandler implements RowCallbackHandler {

        public MouseModelPhenotypeRowCallbackHandler() {
        }

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            Node phenotypeNode = mpTermFactory.getOrCreate("mp_id", rs.getString("mp_id"));
            Node modelNode = mouseModelFactory.getOrCreate("model_id", rs.getInt("model_id"));

            phenotypeNode.createRelationshipTo(modelNode, PhenoDigmRelationshipType.PHENOTYPE_OF);
        }
    }

    private static class MouseModelGeneOrthologRowCallbackHandler implements RowCallbackHandler {

        public MouseModelGeneOrthologRowCallbackHandler() {
        }

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            Node geneNode = geneFactory.getOrCreate("gene_id", rs.getString("model_gene_id"));
            Node modelNode = mouseModelFactory.getOrCreate("model_id", rs.getInt("model_id"));

            modelNode.createRelationshipTo(geneNode, PhenoDigmRelationshipType.MOUSE_MODEL_OF);
        }
    }

    private static class HpMpRowCallbackHandler implements RowCallbackHandler {
        private static int rowsProcessed = 0;
        
        public HpMpRowCallbackHandler() {
        }

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            String mp_id = rs.getString("mp_id");
            String hp_id = rs.getString("hp_id");
                        
            Node mpNode = mpTermFactory.getOrCreate("mp_id", rs.getString("mp_id"));
            if (mpNode == null) {
                logger.info("{} not found in phenotypeNodeIndex", mp_id);
                return;
            }
            Node hpNode = hpTermFactory.getOrCreate("hp_id", rs.getString("hp_id"));
            if (hpNode == null) {
                logger.info("{} not found in phenotypeNodeIndex", hp_id);
                return;
            }
            Relationship relationship = hpNode.createRelationshipTo(mpNode, PhenoDigmRelationshipType.PHENOTYPE_MATCH);
            relationship.setProperty("simJ", rs.getDouble("simJ"));
            relationship.setProperty("ic", rs.getDouble("ic"));
            relationship.setProperty("lcs", rs.getString("lcs"));

//            logger.info("Made relationship {} {} {} {} {} '{}'", rowsProcessed++, hpNode.getProperty("hp_id"), mpNode.getProperty("mp_id"), relationship.getProperty("simJ"), relationship.getProperty("ic"), relationship.getProperty("lcs"));
        }
    }
    
    private static class DiseaseMouseModelRowCallbackHandler implements RowCallbackHandler {
        private static int rowsProcessed = 0;
        
        public DiseaseMouseModelRowCallbackHandler() {
        }

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            
            Node diseaseNode = diseaseFactory.getOrCreate("disease_id", rs.getString("disease_id"));
            
            Node mouseModelNode = mouseModelFactory.getOrCreate("model_id", rs.getInt("model_id"));
            
            Relationship relationship = mouseModelNode.createRelationshipTo(diseaseNode, PhenoDigmRelationshipType.PREDICTED_MODEL_OF);
            relationship.setProperty("d2m", rs.getDouble("disease_to_model_perc_score"));
            relationship.setProperty("m2d", rs.getDouble("model_to_disease_perc_score"));
            relationship.setProperty("raw_score", rs.getDouble("raw_score"));
            relationship.setProperty("hp_matched_terms", rs.getString("hp_matched_terms"));
            relationship.setProperty("mp_matched_terms", rs.getString("mp_matched_terms"));
//            logger.info("Created relationship {} {} {}", mouseModelNode.getProperty("model_id"), diseaseNode.getProperty("disease_id"), PhenoDigmRelationshipType.PREDICTED_MODEL_OF);
            boolean lit_model = rs.getBoolean("lit_model");
            
            if (lit_model) {
                mouseModelNode.createRelationshipTo(diseaseNode, PhenoDigmRelationshipType.CURATED_LITERATURE_MODEL_OF);
//                logger.info("Created relationship {} {} {}", mouseModelNode.getProperty("model_id"), diseaseNode.getProperty("disease_id"), PhenoDigmRelationshipType.CURATED_LITERATURE_MODEL_OF);
            }
//            logger.info("Made relationship {} {} {} {} {} '{}'", rowsProcessed++, hpNode.getProperty("hp_id"), mpNode.getProperty("mp_id"), relationship.getProperty("simJ"), relationship.getProperty("ic"), relationship.getProperty("lcs"));
        }
    }
}
