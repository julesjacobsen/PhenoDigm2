/*
 * Copyright © 2011-2013 EMBL - European Bioinformatics Institute
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import uk.ac.sanger.phenodigm2.model.*;
import uk.ac.sanger.phenodigm2.web.AssociationSummary;
import uk.ac.sanger.phenodigm2.web.DiseaseAssociationSummary;
import uk.ac.sanger.phenodigm2.web.DiseaseGeneAssociationDetail;
import uk.ac.sanger.phenodigm2.web.GeneAssociationSummary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
@Repository
public class PhenoDigmWebDaoJdbcImpl implements PhenoDigmWebDao {

    private static final Logger logger = LoggerFactory.getLogger(PhenoDigmWebDaoJdbcImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<GeneAssociationSummary> getDiseaseToGeneAssociationSummaries(DiseaseIdentifier diseaseId, double minRawScoreCutoff) {

        logger.info("Getting associated genes for disease {} with raw score cutoff >= {}", diseaseId, minRawScoreCutoff);

        String cutOffClause = "";
        
        if (minRawScoreCutoff > 0.0) {
             cutOffClause = String.format("and ((human_curated is true or mod_curated is true) or (mod_raw_score >= %f or htpc_raw_score >= %f)) ", minRawScoreCutoff, minRawScoreCutoff);
        }

        String sql = "select " 
                + "mdgs.model_gene_id, mgo.model_gene_symbol, mgo.hgnc_id, mgo.hgnc_gene_symbol, "
                + "mdgs.human_curated, mdgs.mod_curated, mdgs.in_locus, mdgs.max_mod_disease_to_model_perc_score as max_mod_score, mdgs.max_htpc_disease_to_model_perc_score as max_htpc_score "
                + "from mouse_disease_gene_summary mdgs "
                + "join mouse_gene_ortholog mgo on mgo.model_gene_id = mdgs.model_gene_id "
                + "where mdgs.disease_id = ? "
                + cutOffClause
                + "order by in_locus desc, max_mod_disease_to_model_perc_score desc;";

        List<GeneAssociationSummary> diseaseToGeneAssociationSummaries = jdbcTemplate.query(sql, getArgs(diseaseId.getCompoundIdentifier()), new DiseaseToGeneAssociationSummariesResultSetExtractor());

        if (diseaseToGeneAssociationSummaries.isEmpty()) {
            logger.info("No GeneAssociations found for {}", diseaseId);            
        }
        return diseaseToGeneAssociationSummaries;
    }

    private Object[] getArgs(Object... args) {
        return new Object[]{args};
    }
    
    @Override
    public List<DiseaseAssociationSummary> getGeneToDiseaseAssociationSummaries(GeneIdentifier geneId, double minRawScoreCutoff) {

        logger.info("Getting associated diseases for gene {} with raw score cutoff >= {}", geneId, minRawScoreCutoff);

        String cutOffClause = "";

        if (minRawScoreCutoff > 0.0) {
            cutOffClause = String.format("and ((human_curated is true or mod_curated is true) or (mod_raw_score >= %f or htpc_raw_score >= %f)) ", minRawScoreCutoff, minRawScoreCutoff);
        }

        String sql = "select mdgs.disease_id, d.disease_term, "
                + "mdgs.human_curated, mdgs.mod_curated, mdgs.in_locus, mdgs.max_mod_model_to_disease_perc_score as max_mod_score, "
                + "mdgs.max_htpc_model_to_disease_perc_score as max_htpc_score "
                + "from mouse_disease_gene_summary mdgs "
                + "join disease d on d.disease_id = mdgs.disease_id "
                + "where mdgs.model_gene_id = ? "
                + cutOffClause
                + "order by in_locus desc, max_mod_model_to_disease_perc_score desc;";

        List<DiseaseAssociationSummary> geneToDiseaseAssociationSummaries = jdbcTemplate.query(sql, getArgs(geneId.getCompoundIdentifier()), new GeneToDiseaseAssociationSummariesResultSetExtractor());

        return geneToDiseaseAssociationSummaries;
    }

    @Override
    public DiseaseGeneAssociationDetail getDiseaseGeneAssociationDetail(DiseaseIdentifier diseaseId, GeneIdentifier geneId) {

        logger.info("Getting disease-gene association details for {} {}", diseaseId, geneId);

        String sql = "select disease_id, model_gene_id, dmma.model_id, source, lit_model, allelic_composition, allele_ids, genetic_background, disease_to_model_perc_score, model_to_disease_perc_score, "
                + "mp.mp_id, mp.term "
                + "from mouse_disease_model_association dmma "
                + "join mouse_model mm on mm.model_id = dmma.model_id "
                + "join mouse_model_gene_ortholog mmgo on mm.model_id = mmgo.model_id "
                + "join mouse_model_mp mmp on mmp.model_id = mm.model_id "
                + "join mp mp on mp.mp_id = mmp.mp_id "
                + "where disease_id = ? and model_gene_id = ?;";
       
        Object[] args = new Object[]{diseaseId.getCompoundIdentifier(), geneId.getCompoundIdentifier()};

        List<DiseaseModelAssociation> diseaseModelAssociations = jdbcTemplate.query(sql, args, new DiseaseModelAssociationResultSetExtractor());
        //Remember to add the disease phenotype terms too.
        List<PhenotypeTerm> diseasePhenotypes = getDiseasePhenotypes(diseaseId);

        return new DiseaseGeneAssociationDetail(diseaseId, diseasePhenotypes, getGene(geneId), diseaseModelAssociations);
    }

    @Override
    public Disease getDisease(DiseaseIdentifier diseaseId) {
        String sql = "select disease_id, disease_term, disease_alts, ifnull(disease_locus, '') as disease_locus "
                + "from disease d "
                + "where d.disease_id = ?;";
        
        Disease disease = jdbcTemplate.query(sql, getArgs(diseaseId.getCompoundIdentifier()),  new DiseaseResultSetExtractor());
        
        if (disease.getDiseaseId() == null) {
            disease = new Disease(diseaseId);
        }
        
        return disease;
    }
    
    @Override
    public List<PhenotypeTerm> getDiseasePhenotypes(DiseaseIdentifier diseaseId) {
        
        String sql = "select hp.hp_id as term_id, hp.term as term "
                + "from hp hp "
                + "join disease_hp d on d.hp_id = hp.hp_id "
                + "where d.disease_id = ?;";
        
        List<PhenotypeTerm> diseasePhenotypes = jdbcTemplate.query(sql, getArgs(diseaseId.getCompoundIdentifier()), new DiseasePhenotypesResultSetExtractor());

        logger.info("Got disease phenotypes for {} {}", diseaseId, diseasePhenotypes);
        
        return diseasePhenotypes;
    }

    @Override
    public Gene getGene(GeneIdentifier geneIdentifier) {
        String sql = "select model_gene_id, model_gene_symbol, hgnc_id, hgnc_gene_symbol, hgnc_gene_locus "
                + "from mouse_gene_ortholog "
                + "where model_gene_id = ?;";

        return jdbcTemplate.query(sql, getArgs(geneIdentifier.getCompoundIdentifier()), new GeneResultSetExtractor());
    }

    private class DiseaseToGeneAssociationSummariesResultSetExtractor implements ResultSetExtractor<List<GeneAssociationSummary>> {

        @Override
        public List<GeneAssociationSummary> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<GeneAssociationSummary> results = new ArrayList<>();
            
            while (rs.next()) {
                //make the gene identifier
                GeneIdentifier mouseIdentifier = new GeneIdentifier(rs.getString("model_gene_symbol"), rs.getString("model_gene_id"));
//                logger.info("Made {}", mouseIdentifier);
                GeneIdentifier humanIdentifier = new GeneIdentifier(rs.getString("hgnc_gene_symbol"), rs.getString("hgnc_id"));

                //make the association summary
                double bestModScore = rs.getDouble("max_mod_score");
                double bestHtpcScore = rs.getDouble("max_htpc_score");
                boolean associatedInHuman = rs.getBoolean("human_curated");
                boolean hasLiteratureEvidence = rs.getBoolean("mod_curated");
                boolean inLocus = rs.getBoolean("in_locus");
                AssociationSummary associationSummary = new AssociationSummary(associatedInHuman, hasLiteratureEvidence, inLocus, bestModScore, bestHtpcScore);
                
                //put genes and associations together
                GeneAssociationSummary geneAssociationSummary = new GeneAssociationSummary(humanIdentifier, mouseIdentifier, associationSummary);
                results.add(geneAssociationSummary);
                logger.debug("Made {}", geneAssociationSummary);
            }
            
            return results;
        }

    }

    private class GeneToDiseaseAssociationSummariesResultSetExtractor implements ResultSetExtractor<List<DiseaseAssociationSummary>> {

        @Override
        public List<DiseaseAssociationSummary> extractData(ResultSet rs) throws SQLException, DataAccessException {
            final List<DiseaseAssociationSummary> results = new ArrayList<>();

            while (rs.next()) {
                ///make the Disease details                
                DiseaseIdentifier diseaseId = new DiseaseIdentifier(rs.getString("disease_id"));
                String diseaseTerm = rs.getString("disease_term");
                
                //make the Association summary 
                double bestModScore = rs.getDouble("max_mod_score");
                double bestHtpcScore = rs.getDouble("max_htpc_score");
                boolean associatedInHuman = rs.getBoolean("human_curated");
                boolean hasLiteratureEvidence = rs.getBoolean("mod_curated");
                boolean inLocus = rs.getBoolean("in_locus");
                AssociationSummary associationSummary = new AssociationSummary(associatedInHuman, hasLiteratureEvidence, inLocus, bestModScore, bestHtpcScore);
  
                //add them together and we have a DiseaseAssociaionSummary
                DiseaseAssociationSummary diseaseAssociationSummary = new DiseaseAssociationSummary(diseaseId, diseaseTerm, associationSummary);
                results.add(diseaseAssociationSummary);
                logger.debug("Made {}", diseaseAssociationSummary);
            }
            return results;
        }
    }

    private class DiseaseModelAssociationResultSetExtractor implements ResultSetExtractor<List<DiseaseModelAssociation>> {

        @Override
        public List<DiseaseModelAssociation> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<DiseaseModelAssociation> diseaseAssociations = new ArrayList<>();

            Integer currentModelId = 0;
            List<PhenotypeTerm> modelPhenotypes = new ArrayList();
            while (rs.next()) {
                Integer mouseModelId = rs.getInt("model_id");
                if (!mouseModelId.equals(currentModelId)) {
                    currentModelId = mouseModelId;

                    DiseaseModelAssociation diseaseAssociation = new DiseaseModelAssociation();
                    diseaseAssociation.setDiseaseIdentifier(new DiseaseIdentifier(rs.getString("disease_id")));
                    diseaseAssociation.setDiseaseToModelScore(rs.getDouble("disease_to_model_perc_score"));
                    diseaseAssociation.setModelToDiseaseScore(rs.getDouble("model_to_disease_perc_score"));
                    diseaseAssociation.setHasLiteratureEvidence(rs.getBoolean("lit_model"));

                    //make the mouseModel (several rows)
                    MouseModel model = new MouseModel();
                    model.setMgiModelId(mouseModelId);
                    model.setAllelicComposition(rs.getString("allelic_composition"));
                    model.setAlleleIds(rs.getString("allele_ids"));
                    model.setGeneticBackground(rs.getString("genetic_background"));
                    model.setMgiGeneId(rs.getString("model_gene_id"));
                    model.setSource(rs.getString("source"));
                    model.setAllelicCompositionLink(ExternalLinkBuilder.buildLink(model));

                    modelPhenotypes = new ArrayList();
                    model.setPhenotypeTerms(modelPhenotypes);
                    
                    diseaseAssociation.setMouseModel(model);

                    diseaseAssociations.add(diseaseAssociation);

                    logger.info("Made new {}", diseaseAssociation);
                }
                //make the MP terms and attach to the diseaseAssociation (each row)
                modelPhenotypes.add(new PhenotypeTerm(rs.getString("mp_id"), rs.getString("term")));
            }

            return diseaseAssociations;
        }
    }

    private class DiseasePhenotypesResultSetExtractor implements ResultSetExtractor<List<PhenotypeTerm>> {

        @Override
        public List<PhenotypeTerm> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<PhenotypeTerm> phenotypes = new ArrayList<>();
        
            while(rs.next()) {
                PhenotypeTerm term = new PhenotypeTerm(rs.getString("term_id"), rs.getString("term"));
                phenotypes.add(term);
            }
            
            return phenotypes;
        }
    }

    
    private class DiseaseResultSetExtractor implements ResultSetExtractor<Disease> {

        @Override
        public Disease extractData(ResultSet rs) throws SQLException, DataAccessException {
            Disease disease = new Disease();
            
            while (rs.next()) {
                    disease = new Disease(rs.getString("disease_id"));
                    disease.setTerm(rs.getString("disease_term"));
                    String locus = rs.getString("disease_locus");
                    disease.setLocus(locus);

                    List<String> altTerms = new ArrayList<String>();
                    String synonyms = rs.getString("disease_alts");
                    altTerms.addAll(Arrays.asList(synonyms.split("\\|")));
                    disease.setAlternativeTerms(altTerms);
                    logger.debug("Made {}", disease);
            }
            
            return disease;
        }
    
    }

    private class GeneResultSetExtractor implements ResultSetExtractor<Gene> {

        @Override
        public Gene extractData(ResultSet rs) throws SQLException, DataAccessException {
            Gene gene = null;
            while (rs.next()) {
                GeneIdentifier modelGeneIdentifier = new GeneIdentifier(rs.getString("model_gene_symbol"), rs.getString("model_gene_id"));
                
                GeneIdentifier humanGeneIdentifier = new GeneIdentifier(rs.getString("hgnc_gene_symbol"), rs.getString("hgnc_id"));
                        
                gene = new Gene(modelGeneIdentifier, humanGeneIdentifier);
            }
            return gene;
        }
    }
}
