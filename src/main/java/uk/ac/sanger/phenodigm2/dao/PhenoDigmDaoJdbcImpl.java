/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseAssociation;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.model.MouseModel;
import uk.ac.sanger.phenodigm2.model.PhenotypeMatch;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;

/**
 *
 * Disease data access manager implementation.
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
@Repository
public class PhenoDigmDaoJdbcImpl implements PhenoDigmDao, InitializingBean {

    private static final Logger logger = Logger.getLogger(PhenoDigmDaoJdbcImpl.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;
    //caches these are constantly cross-referenced when building objects
    private static OrthologCache orthologCache;
    private static DiseaseCache diseaseCache;
    private static MouseModelCache mouseModelCache;

    @Override
    public void afterPropertiesSet() throws Exception {
        setUpCaches();
    }

    private void setUpCaches() {
        //don't change the order - the orthologs are needed first off
        setUpOrthologCache();
        setUpDiseaseCache();
        setUpMouseModelsCache();
    }

    private void setUpOrthologCache() {

        if (orthologCache == null) {
            System.out.println("Setting up ortholog cache...");

//            String sql = "select mgi_gene_id, mgi_gene_symbol as mouse_gene_symbol, human_gene_symbol, hgnc_id as hgnc_id from human2mouse_orthologs;";
            String sql = "select mgi.mgi_gene_id as mgi_gene_id, mgi.mgi_gene_symbol as mouse_gene_symbol, human_gene_symbol as human_gene_symbol, hgnc_id as hgnc_id from mgi_genes mgi left join human2mouse_orthologs h2mo on mgi.mgi_gene_id = h2mo.mgi_gene_id;";

            Map<GeneIdentifier, GeneIdentifier> orthologMap = this.jdbcTemplate.query(sql, new OrthologResultSetExtractor());
            orthologCache = new OrthologCache(orthologMap);
        }
    }

    private void setUpDiseaseCache() {
        if (diseaseCache == null) {
            System.out.println("Setting up disease cache...");

            String sql = "select distinct h2mo.*, d.*  from disease d left join disease_genes og on og.disease_id = d.disease_id left join human2mouse_orthologs h2mo on og.omim_gene_id = h2mo.omim_gene_id where d.type = 'disease';";

            Map<String, Disease> diseaseMap = this.jdbcTemplate.query(sql, new DiseaseResultSetExtractor());
            diseaseCache = new DiseaseCache(diseaseMap);
        }
    }

    private void setUpMouseModelsCache() {
        if (mouseModelCache == null) {
            System.out.println("Setting up mouse model cache...");

            String sql = "select mgm.mgi_gene_id, mm.mouse_model_id, mm.allelic_composition, mm.genetic_background, mm.allcomp_link, mm.source from mouse_models mm join mgi_gene_models mgm on mgm.mouse_model_id = mm.mouse_model_id order by mgm.mgi_gene_id;";
            Map<String, MouseModel> mouseModels = this.jdbcTemplate.query(sql, new MouseModelResultSetExtractor());
            mouseModelCache = new MouseModelCache(mouseModels);
        }
    }

    
    @Override
    public GeneIdentifier getGeneIdentifierForMgiGeneId(String mgiGeneId) {
        return orthologCache.getMouseGeneIdentifier(mgiGeneId);
    }

    @Override
    public Disease getDiseaseByDiseaseId(String omimDiseaseId) {
        return diseaseCache.getDiseaseForDiseaseId(omimDiseaseId);
    }

    @Override
    public Set<Disease> getDiseasesByHgncGeneId(String omimGeneId) {
        return diseaseCache.getDiseasesByHgncGeneId(omimGeneId);
    }

    @Override
    public Set<Disease> getDiseasesByMgiGeneId(String mgiGeneId) {
        GeneIdentifier humanOrtholog = orthologCache.getHumanOrthologOfMouseGene(mgiGeneId);
        logger.info(String.format("%s maps to human ortholog %s", mgiGeneId, humanOrtholog));
        
        if (humanOrtholog.getDatabaseAcc().isEmpty()) {
            logger.info(String.format("%s has no known gene association", humanOrtholog ));
            return new TreeSet<Disease>();            
        }
        return diseaseCache.getDiseasesByHgncGeneId(humanOrtholog.getCompoundIdentifier());

    }

    @Override
    public Map<Disease, Set<DiseaseAssociation>> getKnownDiseaseAssociationsForMgiGeneId(String mgiGeneId) {

        Map<Disease, Set<DiseaseAssociation>> diseaseAssociationsMap;
        String sql = "select od.disease_id, ifnull(mouse_to_disease_perc_score, 0.0) as mouse2disease_score, ifnull(disease_to_mouse_perc_score, 0.0) as disease2mouse_score, mgm.mgi_gene_id, od.mouse_model_id "
                + "from disease_2_mgi_mouse_models od "
                + "join mgi_gene_models mgm on mgm.mouse_model_id = od.mouse_model_id "
                + "left join disease_mouse_genotype_associations dmga on dmga.disease_id = od.disease_id and dmga.mouse_model_id = od.mouse_model_id "
                + "where mgm.mgi_gene_id = ? order by od.disease_id;";
        
        PreparedStatementCreator prepStatmentCreator = new SingleValuePreparedStatementCreator(mgiGeneId, sql);

        diseaseAssociationsMap = this.jdbcTemplate.query(prepStatmentCreator, new DiseaseAssociationResultSetExtractor());
        
        System.out.println(String.format("%s has %d known disease associations", mgiGeneId, diseaseAssociationsMap.size()));

        //also add in the diseases with no known disease associations other than by gene orthology.
        for (Disease disease : getDiseasesByMgiGeneId(mgiGeneId)) {
            if (!diseaseAssociationsMap.keySet().contains(disease)) {
                diseaseAssociationsMap.put(disease, new TreeSet<DiseaseAssociation>());
            }
        }
        
        return diseaseAssociationsMap;
    }

    @Override
    public Map<Disease, Set<DiseaseAssociation>> getPredictedDiseaseAssociationsForMgiGeneId(String mgiGeneId) {

        Map<Disease, Set<DiseaseAssociation>> diseaseAssociationsMap;
        
        String sql = "select disease_id as disease_id, ifnull(mouse_to_disease_perc_score, 0.0) as mouse2disease_score, ifnull(disease_to_mouse_perc_score, 0.0) as disease2mouse_score, mgi_gene_id, mouse_model_id "
                + "from disease_mouse_genotype_associations d where mgi_gene_id = ?;";
        
        PreparedStatementCreator prepStatmentCreator = new SingleValuePreparedStatementCreator(mgiGeneId, sql);

        diseaseAssociationsMap = this.jdbcTemplate.query(prepStatmentCreator, new DiseaseAssociationResultSetExtractor());

        return diseaseAssociationsMap;

    }

    @Override
    public GeneIdentifier getHumanOrthologIdentifierForMgiGeneId(String mgiGeneId) {
        return orthologCache.getHumanOrthologOfMouseGene(mgiGeneId);
    }
    
    @Override
    public List<PhenotypeTerm> getDiseasePhenotypeTerms(String diseaseId) {
        List<PhenotypeTerm> phenotypeList;
        String sql = "select d.evidence, hp.term_id as term_id, hp.name as name, hp.definition as definition, hp.comment as comment from hp_term_infos hp join disease_hp d on d.hp_id = hp.term_id where d.disease_id = ?;";
        
        PreparedStatementCreator prepStatmentCreator = new SingleValuePreparedStatementCreator(diseaseId, sql);

        phenotypeList = this.jdbcTemplate.query(prepStatmentCreator, new DiseasePhenotypesResultSetExtractor());
        
        return phenotypeList;    
    }
    
    @Override
    public List<PhenotypeTerm> getMouseModelPhenotypeTerms(String mouseModelId) {
        List<PhenotypeTerm> phenotypeList;
        String sql = "select mp.term_id as term_id, mp.name as name, mp.definition as definition, mp.comment as comment from mp_mouse_models mm join mp_term_infos mp on mp.term_id = mm.mp_id where mm.mouse_model_id = ?;";
        
        PreparedStatementCreator prepStatmentCreator = new SingleValuePreparedStatementCreator(mouseModelId, sql);

        phenotypeList = this.jdbcTemplate.query(prepStatmentCreator, new DiseasePhenotypesResultSetExtractor());
        
        return phenotypeList;    
    }

    @Override
    public List<PhenotypeMatch> getPhenotypeMatches(String diseaseId, String mouseModelId) {
        List<PhenotypeMatch> phenotypeMatchList;
        String sql = "select phenomap.disease_id as disease, phenomap.mouse_model_id as mouse_model_id, phenomap.ic as ic, phenomap.simJ as simJ, phenomap.mp_id as mp_term_id, phenomap.mp_term as mp_term, phenomap.hp_id as hp_term_id, phenomap.hp_term as hp_term, phenomap.lcs as lcs from disease_mouse_genotype_mappings phenomap where disease_id = ? and mouse_model_id = ?;";
        
        PreparedStatementCreator prepStatmentCreator = new TwoValuePreparedStatementCreator(diseaseId, mouseModelId, sql);
        
        phenotypeMatchList = this.jdbcTemplate.query(prepStatmentCreator, new PhenotypeMatchesResultSetExtractor());
        
        return phenotypeMatchList;
    }

    @Override
    public Set<Disease> getAllDiseses() {
        return diseaseCache.getAllDiseses();
    }

    @Override
    public Set<GeneIdentifier> getAllMouseGeneIdentifiers() {
        return orthologCache.getAllMouseGenes();
    }

    @Override
    public Map<GeneIdentifier, Set<DiseaseAssociation>> getKnownDiseaseAssociationsForDiseaseId(String diseaseId) {
        
        Map<GeneIdentifier, Set<DiseaseAssociation>> diseaseAssociationsMap = new TreeMap<GeneIdentifier, Set<DiseaseAssociation>>();
                    //disease_2_mgi_mouse_models has literature curated data
        String sql = "select od.disease_id, ifnull(mouse_to_disease_perc_score, 0.0) as mouse2disease_score, ifnull(disease_to_mouse_perc_score, 0.0) as disease2mouse_score, mgi.mgi_gene_symbol, mgm.mgi_gene_id, od.mouse_model_id "
                + "from disease_2_mgi_mouse_models od "
                + "join mgi_gene_models mgm on mgm.mouse_model_id = od.mouse_model_id "
                + "join mgi_genes mgi on mgi.mgi_gene_id = mgm.mgi_gene_id "
                + "left join disease_mouse_genotype_associations dmga on dmga.disease_id = od.disease_id and dmga.mouse_model_id = od.mouse_model_id "
                + "where od.disease_id = ? order by disease2mouse_score desc;";

        PreparedStatementCreator prepStatmentCreator = new SingleValuePreparedStatementCreator(diseaseId, sql);

        diseaseAssociationsMap = this.jdbcTemplate.query(prepStatmentCreator, new GeneAssociationResultSetExtractor());
        
//        Disease disease = diseaseCache.getDiseaseForDiseaseId(diseaseId);
//        logger.info("Looking for associated gene orthologs of " + disease);
//        for (GeneIdentifier geneIdentifier : disease.getAssociatedMouseGenes()) {
//            Set<DiseaseAssociation> diseaseAssociations = getKnownDiseaseAssociationsForMgiGeneId(geneIdentifier.getCompoundIdentifier()).get(disease);
//            knownDiseaseGeneAssociations.put(geneIdentifier, diseaseAssociations);
//        }
        
        return diseaseAssociationsMap;
        
    }

    @Override
    public Map<GeneIdentifier, Set<DiseaseAssociation>> getPredictedDiseaseAssociationsForDiseaseId(String diseaseId) {
        Map<GeneIdentifier, Set<DiseaseAssociation>> diseaseAssociationsMap;
                     //disease2mouse_gene_associations
        String sql = "select dmga2.disease_id as disease_id, ifnull(mouse_to_disease_perc_score, 0.0) as mouse2disease_score, ifnull(disease_to_mouse_perc_score, 0.0) as disease2mouse_score, mgi.mgi_gene_symbol, d.mgi_gene_id, d.mouse_model_id \n" +
                    "from disease_mouse_gene_associations dmga2 \n" +
                    "join mgi_genes mgi on mgi.mgi_gene_id = dmga2.mgi_gene_id " +
                    "join disease_mouse_genotype_associations d on d.disease_id = dmga2.disease_id and d.mgi_gene_id = dmga2.mgi_gene_id\n" +
                    " where dmga2.disease_id= ? order by d.disease_to_mouse_perc_score desc;"; 
        
        PreparedStatementCreator prepStatmentCreator = new SingleValuePreparedStatementCreator(diseaseId, sql);

        diseaseAssociationsMap = this.jdbcTemplate.query(prepStatmentCreator, new GeneAssociationResultSetExtractor());
                
        return diseaseAssociationsMap;
    }

    @Override
    public Set<MouseModel> getAllMouseModels() {
        return mouseModelCache.getAllMouseModels();
    }
    
    private static class OrthologResultSetExtractor implements ResultSetExtractor<Map<GeneIdentifier, GeneIdentifier>> {

        private Map<GeneIdentifier, GeneIdentifier> resultsMap;

        public OrthologResultSetExtractor() {
            resultsMap = new HashMap<GeneIdentifier, GeneIdentifier>();
        }

        @Override
        public Map<GeneIdentifier, GeneIdentifier> extractData(ResultSet rs) throws SQLException, DataAccessException {
            while (rs.next()) {
                GeneIdentifier mouseGeneIdentifier = new GeneIdentifier(rs.getString("mouse_gene_symbol"), rs.getString("mgi_gene_id"));
//                System.out.println(String.format("Made new Mouse GeneIdentifier: %s %s", mouseGeneIdentifier.getGeneSymbol(), mouseGeneIdentifier.getCompoundIdentifier()));

                GeneIdentifier humanGeneIdentifier;
                //there might not be any disease orthologs mapped to a mouse gene in OMIM so we'll need to make up the Human ortholog gene
                //luckily the mouse and human gene symbols are simply case variants of the same string (apart from when they aren't, which is quite often).
                String humanGeneSymbol = rs.getString("human_gene_symbol");
                String hgncGeneId = rs.getString("hgnc_id");
//                System.out.println(String.format("Making new Human GeneIdentifier: %s %s", humanGeneSymbol, hgncGeneId));
                if (humanGeneSymbol == null) {
                    humanGeneIdentifier = new GeneIdentifier(mouseGeneIdentifier.getGeneSymbol().toUpperCase(), "HGNC", "");
                }
                else if (hgncGeneId == null) {
                    humanGeneIdentifier = new GeneIdentifier(humanGeneSymbol, "HGNC", "");
                }
                else {
                    humanGeneIdentifier = new GeneIdentifier(humanGeneSymbol, hgncGeneId);
                }
//                System.out.println(String.format("Made new Human GeneIdentifier: %s %s", humanGeneIdentifier.getGeneSymbol(), humanGeneIdentifier.getCompoundIdentifier()));

                resultsMap.put(mouseGeneIdentifier, humanGeneIdentifier);
            }
//            System.out.println("Made new ortholog map with " + resultsMap.keySet().size() + " orthologs");
            return resultsMap;
        }
    }

    private static class DiseaseResultSetExtractor implements ResultSetExtractor<Map<String, Disease>> {

        private Map<String, Disease> diseaseMap;

        public DiseaseResultSetExtractor() {
            diseaseMap = new TreeMap<String, Disease>();
        }

        @Override
        public Map<String, Disease> extractData(ResultSet rs) throws SQLException, DataAccessException {

            while (rs.next()) {
                String omimDiseaseId = rs.getString("disease_id");
                Disease disease = diseaseMap.get(omimDiseaseId);
                if (disease == null) {
                    String type = rs.getString("type");
                    String fullOmimId = rs.getString("disease_full_id");
                    String term = rs.getString("disease_term");
                    String altTerms = rs.getString("disease_alts");
                    disease = makeDisease(omimDiseaseId, type, fullOmimId, term, altTerms);
                    diseaseMap.put(disease.getDiseaseId(), disease);
                }
                String mgiGeneId = rs.getString("mgi_gene_id");

                if (mgiGeneId != null) {
                    GeneIdentifier humanGene = orthologCache.getHumanOrthologOfMouseGene(mgiGeneId); //new GeneIdentifier(fields[2], fields[3]);
                    GeneIdentifier mouseGene = orthologCache.getMouseGeneIdentifier(mgiGeneId); //new GeneIdentifier(fields[1], fields[0]);
//                    mgiToOmimOrthologMap.put(mouseGene.getCompoundIdentifier(), humanGene.getCompoundIdentifier());
                    disease.getAssociatedHumanGenes().add(humanGene);
                    disease.getAssociatedMouseGenes().add(mouseGene);
                }

//                addDiseaseToOmimGeneIdToDiseaseMap(disease);
            }

            return diseaseMap;
        }
    }

    private static Disease makeDisease(String omimId, String type, String fullOmimId, String term, String altTerms) {
        Disease disease = new Disease(omimId);

        disease.setTerm(term);

        //add alternative disease terms - if there are any
        if (!altTerms.isEmpty()) {
            disease.setAlternativeTerms(makeAlternativeTerms(altTerms));
        }
        //add human gene identifiers
        disease.setAssociatedHumanGenes(new ArrayList<GeneIdentifier>());
        //add mouse gene identifiers
        disease.setAssociatedMouseGenes(new ArrayList<GeneIdentifier>());

        return disease;
    }

    private static List<String> makeAlternativeTerms(String otherTerms) {
        List<String> alternativeTerms = new ArrayList<String>();
        String[] altTerms = otherTerms.split("\\|");
        for (int i = 0; i < altTerms.length; i++) {
            alternativeTerms.add(altTerms[i]);
        }
        return alternativeTerms;
    }

    private class SingleValuePreparedStatementCreator implements PreparedStatementCreator {

        private String mgiId;
        private String sql;
        
        public SingleValuePreparedStatementCreator(String mgiId, String sql) {
            this.mgiId = mgiId;
            this.sql = sql;
        }

        @Override
        public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, mgiId);
            return preparedStatement;
        }
        
    }

    private static class TwoValuePreparedStatementCreator implements PreparedStatementCreator {

        private String sql;
        private String diseaseId;
        private String mouseModelId;
        
        public TwoValuePreparedStatementCreator(String diseaseId, String mouseModelId, String sql) {
            this.sql = sql;
            this.diseaseId = diseaseId;
            this.mouseModelId = mouseModelId;
        }
        
        @Override
        public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, diseaseId);
            preparedStatement.setString(2, mouseModelId);
            return preparedStatement;
        }
    }

    private static class MouseModelResultSetExtractor implements ResultSetExtractor<Map<String, MouseModel>> {

        private Map<String, MouseModel> mouseModelMap;

        public MouseModelResultSetExtractor() {
            mouseModelMap = new HashMap<String, MouseModel>();
        }

        @Override
        public Map<String, MouseModel> extractData(ResultSet rs) throws SQLException, DataAccessException {
            while (rs.next()) {
                MouseModel mouseModel = new MouseModel();
                mouseModel.setMgiGeneId(rs.getString("mgi_gene_id"));
                mouseModel.setMgiModelId(rs.getString("mouse_model_id"));
                mouseModel.setAllelicComposition(rs.getString("allelic_composition"));
                mouseModel.setGeneticBackground(rs.getString("genetic_background"));
                mouseModel.setAllelicCompositionLink(rs.getString("allcomp_link"));
                mouseModel.setPhenotypeTerms(new ArrayList<PhenotypeTerm>());

                mouseModelMap.put(mouseModel.getMgiModelId(), mouseModel);
            }
            return mouseModelMap;
        }
    }

    private static class DiseaseAssociationResultSetExtractor implements ResultSetExtractor<Map<Disease, Set<DiseaseAssociation>>> {

        public DiseaseAssociationResultSetExtractor() {
        }

        @Override
        public Map<Disease, Set<DiseaseAssociation>> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Disease, Set<DiseaseAssociation>> results = new TreeMap<Disease, Set<DiseaseAssociation>>();

            while (rs.next()) {
                DiseaseAssociation diseaseAssociation = new DiseaseAssociation();
                String diseaseId = rs.getString("disease_id");
                Disease disease = diseaseCache.getDiseaseForDiseaseId(diseaseId);
                if (disease == null) {
                    System.out.println("unable to find disease " + diseaseId + " in disease cache. Possibly the OMIM disease id has been moved?");
                } else {
                    diseaseAssociation.setDiseaseIdentifier(disease.getDiseaseIdentifier());
                    diseaseAssociation.setMouseModel(mouseModelCache.getModel(rs.getString("mouse_model_id")));
                    diseaseAssociation.setPubMedId("TODO");
                    diseaseAssociation.setDiseaseToModelScore(Double.parseDouble(rs.getString("disease2mouse_score")));
                    diseaseAssociation.setModelToDiseaseScore(Double.parseDouble(rs.getString("mouse2disease_score")));
                    diseaseAssociation.setPhenotypeMatches(new ArrayList<PhenotypeMatch>());

                    if (results.containsKey(disease)) {
                        results.get(disease).add(diseaseAssociation);
                    } else {
                        Set<DiseaseAssociation> diseaseAssociations = new TreeSet<DiseaseAssociation>();
                        diseaseAssociations.add(diseaseAssociation);
                        results.put(disease, diseaseAssociations);
                    }
                }
            }
//            System.out.println("\nMade disease associations:");
//            for (Disease disease : results.keySet()) {
//                System.out.println("Disease associations for " + disease.getDiseaseId());
//                for (DiseaseAssociation diseaseAssoc : results.get(disease)) {
//                    System.out.println("\t" + diseaseAssoc);
//                }               
//                System.out.println("");
//            }
//                
//            
            return results;
        }
    }
    
    
    private static class GeneAssociationResultSetExtractor implements ResultSetExtractor<Map<GeneIdentifier, Set<DiseaseAssociation>>> {

        public GeneAssociationResultSetExtractor() {
        }

        @Override
        public Map<GeneIdentifier, Set<DiseaseAssociation>> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<GeneIdentifier, Set<DiseaseAssociation>> results = new TreeMap<GeneIdentifier, Set<DiseaseAssociation>>();

            while (rs.next()) {
                DiseaseAssociation diseaseAssociation = new DiseaseAssociation();
                String diseaseId = rs.getString("disease_id");
                Disease disease = diseaseCache.getDiseaseForDiseaseId(diseaseId);
                if (disease == null) {
                    logger.info("unable to find disease " + diseaseId + " in disease cache. Possibly the OMIM disease id has been moved?");
                } else {
                    diseaseAssociation.setDiseaseIdentifier(disease.getDiseaseIdentifier());
                    MouseModel mouseModel = mouseModelCache.getModel(rs.getString("mouse_model_id"));
                    //might not be a known ortholog?
                    String geneSymbol = rs.getString("mgi_gene_symbol");
                    String mgiGeneId = mouseModel.getMgiGeneId();
//                    logger.info(geneSymbol + " " + mgiGeneId);
                    GeneIdentifier mouseGeneId = orthologCache.getMouseGeneIdentifier(mgiGeneId);
//                    logger.info("Found GeneIdentifier in orthologCache: " + mouseGeneId);
                    if (mouseGeneId == null) {
                        mouseGeneId = new GeneIdentifier(geneSymbol, mgiGeneId);
                        logger.info(mouseGeneId + " not found in ortholog cache so made a new one: " + mouseGeneId);
                    }
                    diseaseAssociation.setMouseModel(mouseModel);
                    diseaseAssociation.setPubMedId("TODO");
                    diseaseAssociation.setDiseaseToModelScore(Double.parseDouble(rs.getString("disease2mouse_score")));
                    diseaseAssociation.setModelToDiseaseScore(Double.parseDouble(rs.getString("mouse2disease_score")));
                    diseaseAssociation.setPhenotypeMatches(new ArrayList<PhenotypeMatch>());

                    if (results.containsKey(mouseGeneId)) {
                        results.get(mouseGeneId).add(diseaseAssociation);
                    } else {
                        Set<DiseaseAssociation> diseaseAssociations = new TreeSet<DiseaseAssociation>();
                        diseaseAssociations.add(diseaseAssociation);
                        results.put(mouseGeneId, diseaseAssociations);
                    }
                }
            }
//            System.out.println("\nMade disease associations:");
//            for (Disease disease : results.keySet()) {
//                System.out.println("Disease associations for " + disease.getDiseaseId());
//                for (DiseaseAssociation diseaseAssoc : results.get(disease)) {
//                    System.out.println("\t" + diseaseAssoc);
//                }               
//                System.out.println("");
//            }
//                
//            
            return results;
        }
    }
        
        private static class DiseasePhenotypesResultSetExtractor implements ResultSetExtractor<List<PhenotypeTerm>> {

        public DiseasePhenotypesResultSetExtractor() {
        }

        @Override
        public List<PhenotypeTerm> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<PhenotypeTerm> phenotypes = new ArrayList<PhenotypeTerm>();
        
            while(rs.next()) {
                PhenotypeTerm term = new PhenotypeTerm();
                term.setTermId(rs.getString("term_id"));
                term.setName(rs.getString("name"));
                term.setDefinition(rs.getString("definition"));
                term.setComment(rs.getString("comment"));
                phenotypes.add(term);
            }
            
            return phenotypes;
        }
    }
        
    private static class PhenotypeMatchesResultSetExtractor implements ResultSetExtractor<List<PhenotypeMatch>> {
                
        public PhenotypeMatchesResultSetExtractor() {
        }

        @Override
        public List<PhenotypeMatch> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<PhenotypeMatch> results = new ArrayList<PhenotypeMatch>();
            
            while (rs.next()) {
                PhenotypeMatch phenoMatch = new PhenotypeMatch();
                phenoMatch.setSimJ(rs.getDouble("simJ"));
                phenoMatch.setIc(rs.getDouble("ic"));
                phenoMatch.setLcs(rs.getString("lcs"));
                //TODO: There are only around 20K PhenotypeTerms so these might
                //be best pulled from a cache as they are static objects from 
                //the HPO (human phenotype ontology) and MP (mammalian phenotype ontology)
                
                PhenotypeTerm mouseTerm = new PhenotypeTerm();
                mouseTerm.setTermId(rs.getString("mp_term_id"));
                mouseTerm.setName(rs.getString("mp_term"));
                phenoMatch.setMousePhenotype(mouseTerm);
                
                PhenotypeTerm humanTerm = new PhenotypeTerm();
                humanTerm.setTermId(rs.getString("hp_term_id"));
                humanTerm.setName(rs.getString("hp_term"));
                phenoMatch.setHumanPhenotype(humanTerm);
                
                
                results.add(phenoMatch);
            }
            return results;
        }
    }


}
