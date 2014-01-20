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

import java.util.ArrayList;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseIdentifier;
import uk.ac.sanger.phenodigm2.model.Gene;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;
import uk.ac.sanger.phenodigm2.web.AssociationSummary;
import uk.ac.sanger.phenodigm2.web.DiseaseAssociationSummary;
import uk.ac.sanger.phenodigm2.web.DiseaseGeneAssociationDetail;
import uk.ac.sanger.phenodigm2.web.GeneAssociationSummary;

/**
 * Implementation of PhenoDigmWebDao which uses Solr as the datasource. This 
 * enables both fast free-text searching of PhenoDigm and also means the data 
 * can be accessed using Solr as a web-service.   
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
@Repository
public class PhenoDigmWebDaoSolrImpl implements PhenoDigmWebDao {

    private static final Logger logger = LoggerFactory.getLogger(PhenoDigmWebDaoSolrImpl.class);
    
    @Autowired
    private SolrServer solrServer;

    @Override
    public Disease getDisease(DiseaseIdentifier diseaseId) {
        
        String query = String.format("\"%s\"", diseaseId.getCompoundIdentifier());
        
        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.addFilterQuery("type:\"disease\"");
        solrQuery.addField("disease_id");
        solrQuery.addField("disease_term");
        solrQuery.addField("disease_alts");
        solrQuery.addField("disease_locus");
        solrQuery.addField("disease_classes");
        solrQuery.addField("phenotypes");

        SolrDocumentList resultsDocumentList;
        
        Disease disease = null;
        
        try {
            resultsDocumentList = solrServer.query(solrQuery).getResults();
            
            if (resultsDocumentList.isEmpty()) {
                logger.info("Uh-oh! Query for disease {} was not found.", diseaseId);
                return disease;
            }
            if (resultsDocumentList.size() > 1) {
                logger.info("Uh-oh! Query for disease {} returned more than one result.", diseaseId);
            }
            SolrDocument solrDocument = resultsDocumentList.get(0);
            disease = new Disease(diseaseId);
            disease.setTerm((String) solrDocument.getFieldValue("disease_term"));
            disease.setAlternativeTerms((List<String>) solrDocument.getFieldValue("disease_alts"));
            disease.setLocations((List<String>) solrDocument.getFieldValue("disease_locus"));
            disease.setClasses((List<String>) solrDocument.getFieldValue("disease_classes"));

//            logger.info("Made {}", disease);
            
        } catch (SolrServerException ex) {
            logger.error(ex.getMessage());
        }
          
        return disease;
    }

    @Override
    public List<PhenotypeTerm> getDiseasePhenotypes(DiseaseIdentifier diseaseId) {
        String query = String.format("\"%s\"", diseaseId.getCompoundIdentifier());
        
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(query);
        solrQuery.addFilterQuery("type:\"disease\"");
        solrQuery.addField("phenotypes");
        SolrDocumentList resultsDocumentList;
        
        List<PhenotypeTerm> phenotypeList = new ArrayList<>();
        try {
            resultsDocumentList = solrServer.query(solrQuery).getResults();

            if (resultsDocumentList.isEmpty()) {
                logger.info("Uh-oh! Query for disease {} phenotypes was not found.", diseaseId);
                return phenotypeList;
            }
            if (resultsDocumentList.size() > 1) {
                logger.info("Uh-oh! Query for disease {} phenotypes returned more than one result.", diseaseId);
            }
            SolrDocument solrDocument = resultsDocumentList.get(0);
            List<String> phenotypes = (List<String>) solrDocument.getFieldValue("phenotypes");
            for (String string : phenotypes) {
                String[] splitString = string.split("_");
                PhenotypeTerm phenotype = new PhenotypeTerm();
                phenotype.setId(splitString[0]);
                phenotype.setTerm(splitString[1]);
                phenotypeList.add(phenotype);
            }

        } catch (SolrServerException ex) {
            logger.error(ex.getMessage());
        }
        
        return phenotypeList;
    }

    @Override
    public Gene getGene(GeneIdentifier geneIdentifier) {

        String query = String.format("\"%s\"", geneIdentifier.getCompoundIdentifier());
        
        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.addFilterQuery("type:\"gene\"");
        solrQuery.addField("marker_accession");
        solrQuery.addField("marker_symbol");
        solrQuery.addField("hgnc_id");
        solrQuery.addField("human_marker_symbol");
        solrQuery.addField("hgnc_gene_locus");

        SolrDocumentList resultsDocumentList;
        
        Gene gene = null;
        try {
            resultsDocumentList = solrServer.query(solrQuery).getResults();

            if (resultsDocumentList.isEmpty()) {
                logger.info("Uh-oh! Query for gene {} was not found.", geneIdentifier);
                return gene;
            }
            if (resultsDocumentList.size() > 1) {
                logger.info("Uh-oh! Query for gene {} returned more than one result.", geneIdentifier);
            }
            SolrDocument solrDocument = resultsDocumentList.get(0);

            String modGenId = (String) solrDocument.getFieldValue("marker_accession");
            String modGenSymbol = (String) solrDocument.getFieldValue("marker_symbol");
            String humanGenId = (String) solrDocument.getFieldValue("hgnc_id");
            String humanGenSymbol = (String) solrDocument.getFieldValue("human_marker_symbol");
            
            GeneIdentifier modelGeneId = new GeneIdentifier(modGenSymbol, modGenId);
            GeneIdentifier humanGeneId = new GeneIdentifier(humanGenSymbol, humanGenId);
            gene = new Gene(modelGeneId, humanGeneId);
        
//            logger.info("Made {}", gene);

        } catch (SolrServerException ex) {
            logger.error(ex.getMessage());
        }
          
        return gene;
    }

    @Override
    public List<GeneAssociationSummary> getDiseaseToGeneAssociationSummaries(DiseaseIdentifier diseaseId, double minRawScoreCutoff) {
        
        String query = String.format("\"%s\" AND raw_mod_score:[%s TO *] ", diseaseId.getCompoundIdentifier(), minRawScoreCutoff);
        //if there is no cutoff then don't put it in the query as it will take a long time (a few seconds) to collect the results
        //rather than a few tens of ms   
        if (minRawScoreCutoff == 0) {
            query = String.format("\"%s\"", diseaseId.getCompoundIdentifier(), minRawScoreCutoff);
        }
        
        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.addFilterQuery("type:\"disease_gene_summary\"");
        solrQuery.addField("marker_accession");
        solrQuery.addField("marker_symbol");
        solrQuery.addField("hgnc_id");
        solrQuery.addField("human_marker_symbol");
        solrQuery.addField("hgnc_gene_locus");
        solrQuery.addField("in_locus");
        //common fields
        solrQuery.addField("human_curated");
        solrQuery.addField("mouse_curated");
        solrQuery.addField("max_mod_score");
        solrQuery.addField("max_htpc_score");

        solrQuery.addSort("in_locus", SolrQuery.ORDER.desc);
        solrQuery.addSort("max_mod_score", SolrQuery.ORDER.desc);      
                
        //there will be more than 10 results for this - we want them all.
        solrQuery.setRows(1000);
        
        SolrDocumentList resultsDocumentList;
        
        List<GeneAssociationSummary> geneAssociationSummaryList = new ArrayList<>();
        try {
            resultsDocumentList = solrServer.query(solrQuery).getResults();
            for (SolrDocument solrDocument : resultsDocumentList) {
//                logger.info("{}", solrDocument.getFieldValuesMap() );
                //make the geneIdentifiers
                String modGenId = (String) solrDocument.getFieldValue("marker_accession");
                String modGenSymbol = (String) solrDocument.getFieldValue("marker_symbol");
                String humanGenId = (String) solrDocument.getFieldValue("hgnc_id");
                String humanGenSymbol = (String) solrDocument.getFieldValue("human_marker_symbol");
                
                GeneIdentifier modelGeneId = new GeneIdentifier(modGenSymbol, modGenId);
                GeneIdentifier hgncGeneId = new GeneIdentifier(humanGenSymbol, humanGenId);
                          
                AssociationSummary associationSummary = makeAssociationSummary(solrDocument);
                
                GeneAssociationSummary geneAssociationSummary = new GeneAssociationSummary(hgncGeneId, modelGeneId, associationSummary);
//                logger.info("Made {}", geneAssociationSummary );
                
                geneAssociationSummaryList.add(geneAssociationSummary);
            }
        } catch (SolrServerException ex) {
            logger.error(ex.getMessage());
        }
        return geneAssociationSummaryList;
    }

    @Override
    public List<DiseaseAssociationSummary> getGeneToDiseaseAssociationSummaries(GeneIdentifier geneId, double minRawScoreCutoff) {
        
        String query = String.format("\"%s\" AND raw_mod_score:[%s TO *] ", geneId.getCompoundIdentifier(), minRawScoreCutoff);
        //if there is no cutoff then don't put it in the query as it will take a long time (a few seconds) to collect the results
        //rather than a few tens of ms   
        if (minRawScoreCutoff == 0) {
            query = String.format("\"%s\"", geneId.getCompoundIdentifier(), minRawScoreCutoff);
        }

        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.addFilterQuery("type:\"gene_disease_summary\"");
        solrQuery.addField("disease_id");
        solrQuery.addField("disease_term");
        //common fields
        solrQuery.addField("in_locus");
        solrQuery.addField("human_curated");
        solrQuery.addField("mouse_curated");
        solrQuery.addField("max_mod_score");
        solrQuery.addField("max_htpc_score");

        solrQuery.addSort("in_locus", SolrQuery.ORDER.desc);
        solrQuery.addSort("max_mod_score", SolrQuery.ORDER.desc);      
                
        //there will be more than 10 results for this - we want them all.
        solrQuery.setRows(1000);
        
        SolrDocumentList resultsDocumentList;
        
        List<DiseaseAssociationSummary> diseaseAssociationSummaryList = new ArrayList<>();
        try {
            resultsDocumentList = solrServer.query(solrQuery).getResults();
            for (SolrDocument solrDocument : resultsDocumentList) {
//                logger.info("{}", solrDocument.getFieldValuesMap() );
                //make the geneIdentifiers
                String diseaseId = (String) solrDocument.getFieldValue("disease_id");
                String diseaseTerm = (String) solrDocument.getFieldValue("disease_term");
                           
                //make the association summary details
                AssociationSummary associationSummary = makeAssociationSummary(solrDocument);
                
                DiseaseAssociationSummary diseaseAssociationSummary = new DiseaseAssociationSummary(new DiseaseIdentifier(diseaseId), diseaseTerm, associationSummary);
//                logger.info("Made {}", diseaseAssociationSummary );
                
                diseaseAssociationSummaryList.add(diseaseAssociationSummary);
            }
        } catch (SolrServerException ex) {
            logger.error(ex.getMessage());
        }
        return diseaseAssociationSummaryList;
    }

    @Override
    public DiseaseGeneAssociationDetail getDiseaseGeneAssociationDetail(DiseaseIdentifier diseaseId, GeneIdentifier geneId) {
        return new DiseaseGeneAssociationDetail(diseaseId);
    }

    /**
     * Creates an AssociationSummary object from a solrDocument. Don't feed this
     * any old document otherwise there will be null pointer exceptions.
     * 
     * @param solrDocument
     * @return 
     */
    private AssociationSummary makeAssociationSummary(SolrDocument solrDocument) {
        //make the association summary details
        boolean associatedInHuman = Boolean.valueOf( (Boolean) solrDocument.getFieldValue("human_curated")).booleanValue();
        boolean hasLiteratureEvidence = Boolean.valueOf( (Boolean) solrDocument.getFieldValue("mouse_curated")).booleanValue();
        boolean inLocus = Boolean.valueOf( (Boolean) solrDocument.getFieldValue("in_locus")).booleanValue();
        
        float bestModScore = 0;
        if (solrDocument.getFieldValue("max_mod_score") != null) {
            bestModScore = (float) solrDocument.getFieldValue("max_mod_score");
        }
        
        float bestHtpcScore = 0;
        if (solrDocument.getFieldValue("max_htpc_score") != null) {
            bestHtpcScore = (float) solrDocument.getFieldValue("max_htpc_score");  
        }

        AssociationSummary associationSummary = new AssociationSummary(associatedInHuman, hasLiteratureEvidence, inLocus, bestModScore, bestHtpcScore);
//        System.out.println(associationSummary);
        return associationSummary;
    }
    
}
