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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseModelAssociation;
import uk.ac.sanger.phenodigm2.model.DiseaseIdentifier;
import uk.ac.sanger.phenodigm2.model.Gene;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;
import uk.ac.sanger.phenodigm2.web.DiseaseAssociationSummary;
import uk.ac.sanger.phenodigm2.web.DiseaseGeneAssociationDetail;
import uk.ac.sanger.phenodigm2.web.GeneAssociationSummary;

/**
 *
 * @author jj8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:jdbc-test-services.xml"})
public class PhenoDigmWebDaoJdbcImplTest {
    
    Logger logger = LoggerFactory.getLogger(PhenoDigmWebDaoJdbcImplTest.class);

    @Autowired
    PhenoDigmWebDao instance;
    
    public PhenoDigmWebDaoJdbcImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getDiseaseToGeneAssociationSummaries method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetDiseaseToGeneAssociationSummaries() {
        logger.info("getDiseaseToGeneAssociationSummaries");
        
//        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:101400");
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:112600");
        List<GeneAssociationSummary> result = instance.getDiseaseToGeneAssociationSummaries(diseaseId, 0.0);

        assertTrue(result.size() >= 17);

    }
    
    /**
     * Test of getDiseaseToGeneAssociationSummaries method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetDiseaseToGeneAssociationSummariesWithCutOff() {
        logger.info("getDiseaseToGeneAssociationSummaries");
        
//        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:101400");
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:112600");
        List<GeneAssociationSummary> result = instance.getDiseaseToGeneAssociationSummaries(diseaseId, 1.98);

        assertTrue(result.size() >= 17);
        for (GeneAssociationSummary geneAssociationSummary : result) {
            logger.info(geneAssociationSummary.toString());
        }
    }
        
    /**
     * Test of getDiseaseToGeneAssociationSummariesNoKnownAssociations method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetDiseaseToGeneAssociationSummariesNoKnownAssociations() {
        logger.info("testGetDiseaseToGeneAssociationSummariesNoKnownAssociations");
        
//        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:101400");
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
//        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:243050");
//        Disease expected = new Disease(diseaseId);
//        expected.setTerm("INDOLYLACROYL GLYCINURIA WITH MENTAL RETARDATION");
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("DECIPHER:18");
        Disease expected = new Disease(diseaseId);
        expected.setTerm("1P36 MICRODELETION SYNDROME");
        Disease result = instance.getDisease(diseaseId);
        
        assertEquals(expected, result);
    }
    
    /**
     * Test of getGene method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetGene() {
        GeneIdentifier modelGeneId = new GeneIdentifier("Apoe", "MGI:88057");
        GeneIdentifier humanGeneId = new GeneIdentifier("APOE", "HGNC:613");
        Gene expected = new Gene(modelGeneId, humanGeneId);

        Gene result = instance.getGene(modelGeneId);
        
        assertEquals(expected, result);
    }
    
    /**
     * Test of getGeneToDiseaseAssociationSummaries method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetGeneToDiseaseAssociationSummaries() {
        logger.info("getGeneToDiseaseAssociationSummaries");
        GeneIdentifier geneId = new GeneIdentifier("Apoe", "MGI:88057");
        List<DiseaseAssociationSummary> expResult = null;
        List<DiseaseAssociationSummary> result = instance.getGeneToDiseaseAssociationSummaries(geneId, 0.0);
//        assertEquals(expResult, result);

    }
    
    /**
     * Test of getGeneToDiseaseAssociationSummaries method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetGeneToDiseaseAssociationSummariesWithCutoff() {
        logger.info("getGeneToDiseaseAssociationSummaries");
        GeneIdentifier geneId = new GeneIdentifier("Apoe", "MGI:88057");
        List<DiseaseAssociationSummary> expResult = null;
        List<DiseaseAssociationSummary> result = instance.getGeneToDiseaseAssociationSummaries(geneId, 1.98);
//        assertEquals(expResult, result);
        //TODO: Test MGI:1096550 as this has 1:n mouse:human orthologs 
    }

    /**
     * Test of getDiseaseGeneAssociationDetail method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetDiseaseGeneAssociationDetail() {
        logger.info("getDiseaseGeneAssociationDetail");
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
