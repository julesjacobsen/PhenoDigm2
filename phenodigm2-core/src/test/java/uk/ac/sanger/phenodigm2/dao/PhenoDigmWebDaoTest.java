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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseIdentifier;
import uk.ac.sanger.phenodigm2.model.DiseaseModelAssociation;
import uk.ac.sanger.phenodigm2.model.Gene;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;
import uk.ac.sanger.phenodigm2.web.DiseaseAssociationSummary;
import uk.ac.sanger.phenodigm2.web.DiseaseGeneAssociationDetail;
import uk.ac.sanger.phenodigm2.web.GeneAssociationSummary;

/**
 * Abstract test class for testing any implementations of the PhenoDigmWebDao 
 * interface.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public abstract class PhenoDigmWebDaoTest {
    
    @Autowired
    PhenoDigmWebDao instance;
    
    Logger logger;
    
    /**
     * Test of getDiseaseToGeneAssociationSummaries method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetDiseaseToGeneAssociationSummaries() {
//        logger.info("getDiseaseToGeneAssociationSummaries");
        
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:101600");
        List<GeneAssociationSummary> result = instance.getDiseaseToGeneAssociationSummaries(diseaseId, 0.0);
        for (GeneAssociationSummary geneAssociationSummary : result) {

        }
        assertTrue(result.size() >= 17);

    }
    
    @Test
    public void testGetDiseasePhenotypes() {
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:101200");
        List<PhenotypeTerm> result = instance.getDiseasePhenotypes(diseaseId);
        PhenotypeTerm expectedTerm = new PhenotypeTerm();
        expectedTerm.setId("HP:0000175");
        expectedTerm.setTerm("Cleft palate");
        assertTrue(result.contains(expectedTerm));
        assertTrue(result.size() >= 75);
    }
    
    /**
     * Test of getDiseaseToGeneAssociationSummaries method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetDiseaseToGeneAssociationSummariesWithCutOff() {
//        logger.info("getDiseaseToGeneAssociationSummaries");
        
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:101600");
        List<GeneAssociationSummary> result = instance.getDiseaseToGeneAssociationSummaries(diseaseId, 2.00);

        assertTrue(result.size() >= 17);
        for (GeneAssociationSummary geneAssociationSummary : result) {
//            logger.info(geneAssociationSummary.toString());
        }
    }
        
    /**
     * Test of getDiseaseToGeneAssociationSummariesNoKnownAssociations method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetDiseaseToGeneAssociationSummariesNoKnownAssociations() {
//        logger.info("testGetDiseaseToGeneAssociationSummariesNoKnownAssociations");
        
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("DECIPHER:18");
        Disease expDisease = new Disease(diseaseId);
        expDisease.setTerm("1P36 MICRODELETION SYNDROME");
        
        List<GeneAssociationSummary> result = instance.getDiseaseToGeneAssociationSummaries(diseaseId, 0.0);
        
        assertTrue(result.size() >= 4);
        for (GeneAssociationSummary geneAssociationSummary : result) {
            assertFalse(geneAssociationSummary.getAssociationSummary().isHasLiteratureEvidence());
        }

    }

    /**
     * Test of getDisease method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetDisease() {
        DiseaseIdentifier omimDiseaseId = new DiseaseIdentifier("OMIM:101600");
        Disease expectedResult = new Disease();
        expectedResult.setDiseaseIdentifier(omimDiseaseId);
        expectedResult.setTerm("PFEIFFER SYNDROME");
        List<String> pfeifferAlternativeTerms = new ArrayList<String>();
        pfeifferAlternativeTerms.add("ACROCEPHALOSYNDACTYLY, TYPE V; ACS");
        pfeifferAlternativeTerms.add("ACS V");
        pfeifferAlternativeTerms.add("NOACK SYNDROME");
        pfeifferAlternativeTerms.add("CRANIOFACIAL-SKELETAL-DERMATOLOGIC DYSPLASIA, INCLUDED");
        expectedResult.setAlternativeTerms(pfeifferAlternativeTerms);
        List<String> locations = new ArrayList();
        locations.add("8p11.23-p11.22");
        locations.add("10q26.13");
        expectedResult.setLocations(locations);
        
        Disease result = instance.getDisease(omimDiseaseId);
        logger.info("Made {}", result);

        assertEquals(expectedResult, result);
    }
    
    /**
     * Test of getGene method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetGene() {
        GeneIdentifier modelGeneId = new GeneIdentifier("Fgfr2", "MGI:95523");
        GeneIdentifier humanGeneId = new GeneIdentifier("FGFR2", "HGNC:3689");
        Gene expected = new Gene(modelGeneId, humanGeneId);

        Gene result = instance.getGene(modelGeneId);
        logger.info("Made {}", result);
        
        assertEquals(expected, result);
        //check the model oranism details
        assertEquals("Fgfr2", result.getOrthologGeneId().getGeneSymbol());
        assertEquals("MGI:95523", result.getOrthologGeneId().getCompoundIdentifier());
        //check the human details
        assertEquals("FGFR2", result.getHumanGeneId().getGeneSymbol());
        assertEquals("HGNC:3689", result.getHumanGeneId().getCompoundIdentifier());
    }
    
    /**
     * Test of getGeneToDiseaseAssociationSummaries method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetGeneToDiseaseAssociationSummaries() {
//        logger.info("getGeneToDiseaseAssociationSummaries");
        GeneIdentifier geneId = new GeneIdentifier("Fgfr2", "MGI:95523");
        List<DiseaseAssociationSummary> result = instance.getGeneToDiseaseAssociationSummaries(geneId, 0.0);
        assertTrue("Expected more than 290 results", result.size() > 290);

    }
    
    /**
     * Test of getGeneToDiseaseAssociationSummaries method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetGeneToDiseaseAssociationSummariesWithCutoff() {
//        logger.info("getGeneToDiseaseAssociationSummaries");
        GeneIdentifier geneId = new GeneIdentifier("Fgfr2", "MGI:95523");
        List<DiseaseAssociationSummary> expResult = null;
        List<DiseaseAssociationSummary> result = instance.getGeneToDiseaseAssociationSummaries(geneId, 2.00);
        assertTrue("Expected more than 250 results", result.size() > 250);
        //TODO: Test MGI:1096550 as this has 1:n mouse:human orthologs 
    }

    /**
     * Test of getDiseaseGeneAssociationDetail method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetDiseaseGeneAssociationDetail() {
//        logger.info("getDiseaseGeneAssociationDetail");
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:144250");;
        GeneIdentifier geneId = new GeneIdentifier("Lpl", "MGI:96820");

        DiseaseGeneAssociationDetail result = instance.getDiseaseGeneAssociationDetail(diseaseId, geneId);
//        assertEquals(expResult, result);
        logger.info("{}", result.getDiseaseId());
        assertEquals(diseaseId, result.getDiseaseId());
        assertEquals(5, result.getDiseaseAssociations().size());
        
        DiseaseModelAssociation diseaseModelAssociation = result.getDiseaseAssociations().get(0);
        logger.info("Testing: {}", diseaseModelAssociation);

        assertEquals("MGI:96820", diseaseModelAssociation.getMouseModel().getMgiGeneId());
        assertEquals("14434", diseaseModelAssociation.getMouseModel().getMgiModelId());
        
        assertEquals(74.18, diseaseModelAssociation.getDiseaseToModelScore(), 0.00);
        assertEquals(84.48, diseaseModelAssociation.getModelToDiseaseScore(), 0.00);
        
        //test the phenotypes
        PhenotypeTerm expectedTerm = new PhenotypeTerm();
        expectedTerm.setId("MP:0001552");
        expectedTerm.setTerm("increased circulating triglyceride level");
        
        assertNotNull("Expected a list, but got a null :(", diseaseModelAssociation.getMouseModelPhenotypeTerms());
        assertFalse("Expected a full list, but got an empty one :(", diseaseModelAssociation.getMouseModelPhenotypeTerms().isEmpty());
        assertTrue(String.format("Expected a list containing the term %s, but the term wasn't found :(", expectedTerm), diseaseModelAssociation.getMouseModelPhenotypeTerms().contains(expectedTerm));
          
        
        for (DiseaseModelAssociation diseaseAssociation : result.getDiseaseAssociations()) {
            logger.info("{}", diseaseAssociation);
        }

    }
}
