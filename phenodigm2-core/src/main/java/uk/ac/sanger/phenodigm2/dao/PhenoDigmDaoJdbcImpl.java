/*
 * Copyright Â© 2011-2013 EMBL - European Bioinformatics Institute
 * and Genome Research Limited
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.sanger.phenodigm2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import uk.ac.sanger.phenodigm2.model.CurationStatus;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseIdentifier;
import uk.ac.sanger.phenodigm2.model.Gene;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.model.MouseModel;
import uk.ac.sanger.phenodigm2.model.PhenotypeMatch;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;
import uk.ac.sanger.phenodigm2.model.ProjectStatus;
import uk.ac.sanger.phenodigm2.web.DiseaseAssociationSummary;
import uk.ac.sanger.phenodigm2.web.DiseaseGeneAssociationDetail;
import uk.ac.sanger.phenodigm2.web.GeneAssociationSummary;

/**
 *
 * Disease data access manager implementation.
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
@Repository
public class PhenoDigmDaoJdbcImpl implements PhenoDigmDao {

    private static final Logger logger = LoggerFactory.getLogger(PhenoDigmDaoJdbcImpl.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //caches these are constantly cross-referenced when building objects
    private static OrthologCache orthologCache;
    private static DiseaseCache diseaseCache;
    private static MouseModelCache mouseModelCache;
    
    public PhenoDigmDaoJdbcImpl() {
    }
  
    private void setUpCaches() {
        //don't change the order - the orthologs are needed first off
        setUpOrthologCache();
        setUpDiseaseCache();
        setUpMouseModelsCache();
    }

    private void setUpOrthologCache() {

        if (orthologCache == null) {
            logger.info("Setting up ortholog cache...");

//            String sql = "select mgi_gene_id as mgi_gene_id, mgi_gene_symbol as mouse_gene_symbol, human_gene_symbol as human_gene_symbol, hgnc_id as hgnc_id, human_curated, mouse_curated, mgi_predicted, impc_predicted, mgi_mouse, impc_mouse, impc_pheno \n" +
//                            "from gene_summary;";
            
            String sql = "select mgo.model_gene_id, mgo.model_gene_symbol, ifnull(mgo.hgnc_id, '') as hgnc_gene_id, ifnull(mgo.hgnc_gene_symbol, '') as hgnc_gene_symbol, ifnull(mgo.hgnc_gene_locus,  '') as hgnc_gene_locus "
                    + "from mouse_gene_ortholog mgo";
         
            Map<GeneIdentifier, Gene> orthologMap = this.jdbcTemplate.query(sql, new OrthologGeneResultSetExtractor());
            
            orthologCache = new OrthologCache(orthologMap);
        }
    }

    private void setUpDiseaseCache() {
        if (diseaseCache == null) {
            logger.info("Setting up disease cache...");
            
            String sql = "select * from disease;";
            
            Map<String, Disease> diseaseMap = this.jdbcTemplate.query(sql, new DiseaseResultSetExtractor());
            logger.info("Made diseases.");
            
            diseaseCache = new DiseaseCache(diseaseMap, new HashMap());
        }
    }

    private void setUpMouseModelsCache() {
        if (mouseModelCache == null) {
            logger.info("Setting up mouse model cache...");

//            String sql = "select mgm.mgi_gene_id, mm.mouse_model_id, mm.allelic_composition, mm.genetic_background, mm.allcomp_link, mm.source from mouse_models mm join mgi_gene_models mgm on mgm.mouse_model_id = mm.mouse_model_id order by mgm.mgi_gene_id;";
            String sql = "select mmgo.model_gene_id, mm.model_id, mm.allelic_composition, mm.genetic_background, mm.allelic_composition_link, mm.source "
                    + "from mouse_model mm "
                    + "join mouse_model_gene_ortholog mmgo on mmgo.model_id = mm.model_id "
                    + "order by mmgo.model_gene_id;";

            Map<String, MouseModel> mouseModels = this.jdbcTemplate.query(sql, new MouseModelResultSetExtractor());
            mouseModelCache = new MouseModelCache(mouseModels);
        }
    }

    @Override
    public Disease getDisease(DiseaseIdentifier diseaseId) {
        return diseaseCache.getDisease(diseaseId.toString());
    }
    
    @Override
    public List<PhenotypeTerm> getDiseasePhenotypes(DiseaseIdentifier diseaseId) {
        List<PhenotypeTerm> phenotypeList;
        String sql = "select hp.hp_id as id, hp.term as term from hp join disease_hp d on d.hp_id = hp.hp_id where d.disease_id = ?;";
        
        PreparedStatementCreator prepStatmentCreator = new SingleValuePreparedStatementCreator(diseaseId.toString(), sql);

        phenotypeList = this.jdbcTemplate.query(prepStatmentCreator, new PhenotypeResultSetExtractor());
        
        return phenotypeList;    
    }
    
    @Override
    public List<PhenotypeTerm> getMouseModelPhenotypes(String mouseModelId) {
        List<PhenotypeTerm> phenotypeList;
        String sql = "select mp.mp_id as id, mp.term as term from mouse_model_mp mm join mp on mp.mp_id = mm.mp_id where mm.model_id = ?;";
        
        PreparedStatementCreator prepStatmentCreator = new SingleValuePreparedStatementCreator(mouseModelId, sql);

        phenotypeList = this.jdbcTemplate.query(prepStatmentCreator, new PhenotypeResultSetExtractor());
        
        return phenotypeList;    
    }

    @Override
    public List<PhenotypeMatch> getPhenotypeMatches(String diseaseId, String mouseModelId) {
        List<PhenotypeMatch> phenotypeMatchList;
        String sql = "select phenomap.disease_id as disease, phenomap.model_id as model_id, phenomap.ic as ic, phenomap.simJ as simJ, phenomap.mp_id as mp_term_id, mp.term as mp_term, phenomap.hp_id as hp_term_id, hp.term as hp_term, phenomap.lcs as lcs "
                + "from mouse_disease_model_association_detail phenomap "
                + "join hp on hp.hp_id = phenomap.hp_id "
                + "join mp on mp.mp_id = phenomap.mp_id "
                + "where disease_id = ? and model_id = ?;";
        
        PreparedStatementCreator prepStatmentCreator = new TwoValuePreparedStatementCreator(diseaseId, mouseModelId, sql);
        
        phenotypeMatchList = this.jdbcTemplate.query(prepStatmentCreator, new PhenotypeMatchesResultSetExtractor());
        
        return phenotypeMatchList;
    }

    @Override
    public Set<Disease> getAllDiseses() {
        return diseaseCache.getAllDiseses();
    }

    @Override
    public Set<MouseModel> getAllMouseModels() {
        return mouseModelCache.getAllMouseModels();
    }

    @Override
    public Gene getGene(GeneIdentifier geneIdentifier) {
        return orthologCache.getGene(geneIdentifier);
    }

    @Override
    public Set<Gene> getAllGenes() {
        return orthologCache.getGenes();
    }

    @Override
    public Map<Disease, List<GeneAssociationSummary>> getDiseaseToGeneAssociationSummaries(DiseaseIdentifier diseaseId, double minScoreCutoff) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<Gene, List<DiseaseAssociationSummary>> getGeneToDiseaseAssociationSummaries(GeneIdentifier geneId, double minScoreCutoff) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public DiseaseGeneAssociationDetail getDiseaseGeneAssociationDetail(DiseaseIdentifier diseaseId, GeneIdentifier geneId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
    private static class OrthologGeneResultSetExtractor implements ResultSetExtractor<Map<GeneIdentifier, Gene>> {

        private Map<GeneIdentifier, Gene> resultsMap;

        public OrthologGeneResultSetExtractor() {
            resultsMap = new HashMap<GeneIdentifier, Gene>();
        }

        @Override
        public Map<GeneIdentifier, Gene> extractData(ResultSet rs) throws SQLException, DataAccessException {
            while (rs.next()) {
                GeneIdentifier mouseGeneIdentifier = new GeneIdentifier(rs.getString("model_gene_symbol"), rs.getString("model_gene_id"));
//                System.out.println(String.format("Made new Mouse GeneIdentifier: %s %s", mouseGeneIdentifier.getGeneSymbol(), mouseGeneIdentifier.getCompoundIdentifier()));

                GeneIdentifier humanGeneIdentifier;
                //there might not be any disease orthologs mapped to a mouse gene in OMIM so we'll need to make up the Human ortholog gene
                //luckily the mouse and human gene symbols are simply case variants of the same string (apart from when they aren't, which is quite often).
                String humanGeneSymbol = rs.getString("hgnc_gene_symbol");
                String hgncGeneId = rs.getString("hgnc_gene_id");
//                System.out.println(String.format("Making new Human GeneIdentifier: %s %s", humanGeneSymbol, hgncGeneId));
                if (humanGeneSymbol.isEmpty()) {
                    humanGeneIdentifier = null;
                }
                //there are cases still there is a gene symbol, but we don't have a HGNC geneId
                else if (hgncGeneId.isEmpty()) {
                    humanGeneIdentifier = new GeneIdentifier(humanGeneSymbol, "HGNC", "");
                }
                else {
                    humanGeneIdentifier = new GeneIdentifier(humanGeneSymbol, hgncGeneId);
                }
//                System.out.println(String.format("Made new Human GeneIdentifier: %s %s", humanGeneIdentifier.getGeneSymbol(), humanGeneIdentifier.getCompoundIdentifier()));

                Gene geneOrthologs = new Gene(mouseGeneIdentifier, humanGeneIdentifier);
                              
                resultsMap.put(mouseGeneIdentifier, geneOrthologs);
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
                    disease = new Disease(omimDiseaseId);
                    disease.setTerm(rs.getString("disease_term"));
                    String altTerms = rs.getString("disease_alts");
                    //add alternative disease terms - if there are any
                    if (!altTerms.isEmpty()) {
                        disease.setAlternativeTerms(makeStringListFromDelimitedString(altTerms, "\\|"));
                    }
                    String loci = rs.getString("disease_locus");
                    //add known disease loci - if there are any
                    if (!loci.isEmpty()) {
                        disease.setLocations(makeStringListFromDelimitedString(loci, ","));
                    }
                    
                    String classes = rs.getString("disease_classes");
                    //add disease classes - if there are any
                    if (!classes.isEmpty()) {
                        disease.setClasses(makeStringListFromDelimitedString(classes, ","));
                    }

                    diseaseMap.put(disease.getDiseaseId(), disease);
                }
            }

            return diseaseMap;
        }
    }

    private static CurationStatus makeCurationStatus(int human_curated, int mouse_curated, int mgi_phenotype, int impc_phenotype) {
        
        CurationStatus curationStatus = new CurationStatus();
        
        if (human_curated == 1) {
            curationStatus.setIsAssociatedInHuman(true);
        }
        if (mouse_curated == 1) {
            curationStatus.setHasMgiLiteratureEvidence(true);
        }
        if (mgi_phenotype == 1) {
            curationStatus.setHasMgiPhenotypeEvidence(true);
        }
        if (impc_phenotype == 1) {
            curationStatus.setHasImpcPhenotypeEvidence(true);
        }
        
        return curationStatus;
    }

    private static ProjectStatus makeProjectStatus(int mgi_mouse, int impc_mouse, int impc_pheno){
        ProjectStatus projectStatus = new ProjectStatus();
        
        if (mgi_mouse == 1) {
            projectStatus.setHasMgiMouse(true);
        }
        if (impc_mouse == 1) {
            projectStatus.setHasImpcMouse(true);        
        }
        if (impc_pheno == 1) {
            projectStatus.setHasImpcPhenotypeData(true);
        }
        
        return projectStatus;
    }
    
    private static List<String> makeStringListFromDelimitedString(String otherTerms, String delimiter) {
        List<String> alternativeTerms = new ArrayList<String>();
        String[] altTerms = otherTerms.split(delimiter);
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
                mouseModel.setMgiGeneId(rs.getString("model_gene_id"));
                mouseModel.setMgiModelId(rs.getString("model_id"));
                mouseModel.setSource(rs.getString("source"));
                mouseModel.setAllelicComposition(rs.getString("allelic_composition"));
                mouseModel.setGeneticBackground(rs.getString("genetic_background"));
                mouseModel.setAllelicCompositionLink(rs.getString("allelic_composition_link"));
                mouseModel.setPhenotypeTerms(new ArrayList<PhenotypeTerm>());

                mouseModelMap.put(mouseModel.getMgiModelId(), mouseModel);
            }
            return mouseModelMap;
        }
    }
        
    private static class PhenotypeResultSetExtractor implements ResultSetExtractor<List<PhenotypeTerm>> {

        public PhenotypeResultSetExtractor() {
        }

        @Override
        public List<PhenotypeTerm> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<PhenotypeTerm> phenotypes = new ArrayList<PhenotypeTerm>();
        
            while(rs.next()) {
                PhenotypeTerm term = new PhenotypeTerm();
                term.setId(rs.getString("id"));
                term.setTerm(rs.getString("term"));
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
                mouseTerm.setId(rs.getString("mp_term_id"));
                mouseTerm.setTerm(rs.getString("mp_term"));
                phenoMatch.setMousePhenotype(mouseTerm);
                
                PhenotypeTerm humanTerm = new PhenotypeTerm();
                humanTerm.setId(rs.getString("hp_term_id"));
                humanTerm.setTerm(rs.getString("hp_term"));
                phenoMatch.setHumanPhenotype(humanTerm);
                
                
                results.add(phenoMatch);
            }
            return results;
        }
    }


}
