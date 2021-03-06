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
import org.junit.Assert;
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
import uk.ac.sanger.phenodigm2.model.MouseModel;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;
import uk.ac.sanger.phenodigm2.web.AssociationSummary;
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

    static Logger logger;

    /**
     * Test of getDiseaseToGeneAssociationSummaries method, of class
     * PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetDiseaseToGeneAssociationSummaries() {
//        logger.info("getDiseaseToGeneAssociationSummaries");

        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:101600");
        double cutoff = 0.0;

        List<GeneAssociationSummary> result = instance.getDiseaseToGeneAssociationSummaries(diseaseId, cutoff);
        logger.info("{} has {} GeneAssociationSummary using cutoff of {}", diseaseId, result.size(), cutoff);

        assertFalse(result.isEmpty());

        GeneIdentifier expectedModelGeneIdentifier = new GeneIdentifier("Fgfr2", "MGI:95523");
        GeneIdentifier expectedHgncGeneIdentifier = new GeneIdentifier("FGFR2", "HGNC:3689");
        AssociationSummary expectedSummary = new AssociationSummary(true, true, true, 82.77, 0.0);

        GeneAssociationSummary expectedAssociationSummary = null;
        for (GeneAssociationSummary geneAssociationSummary : result) {
            if (geneAssociationSummary.getModelGeneIdentifier().equals(expectedModelGeneIdentifier)) {
                expectedAssociationSummary = geneAssociationSummary;
                logger.info("Found expected {}", geneAssociationSummary);
            }
        }
        assertNotNull(expectedAssociationSummary);
        assertEquals(expectedHgncGeneIdentifier, expectedAssociationSummary.getHgncGeneIdentifier());
        assertEquals(expectedSummary, expectedAssociationSummary.getAssociationSummary());
    }

    /**
     * Test of getDiseaseToGeneAssociationSummaries method, of class
     * PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetDiseaseToGeneAssociationSummariesWithCutOff() {
//        logger.info("getDiseaseToGeneAssociationSummaries");
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:101600");
        double cutoff = 2.00;

        List<GeneAssociationSummary> result = instance.getDiseaseToGeneAssociationSummaries(diseaseId, cutoff);
        logger.info("{} has {} GeneAssociationSummary using cutoff of {}", diseaseId, result.size(), cutoff);

        assertFalse(result.isEmpty());
        GeneIdentifier expectedModelGeneIdentifier = new GeneIdentifier("Fgfr2", "MGI:95523");
        GeneIdentifier expectedHgncGeneIdentifier = new GeneIdentifier("FGFR2", "HGNC:3689");
        AssociationSummary expectedSummary = new AssociationSummary(true, true, true, 82.77, 0.0);

        GeneAssociationSummary expectedAssociationSummary = null;
        for (GeneAssociationSummary geneAssociationSummary : result) {
            if (geneAssociationSummary.getModelGeneIdentifier().equals(expectedModelGeneIdentifier)) {
                expectedAssociationSummary = geneAssociationSummary;
                logger.info("Found expected {}", geneAssociationSummary);
            }
        }
        assertNotNull(expectedAssociationSummary);
        assertEquals(expectedHgncGeneIdentifier, expectedAssociationSummary.getHgncGeneIdentifier());
        assertEquals(expectedSummary, expectedAssociationSummary.getAssociationSummary());
    }

    @Test
    public void testGetDiseasePhenotypes() {
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:101200");
        List<PhenotypeTerm> result = instance.getDiseasePhenotypes(diseaseId);
        PhenotypeTerm expectedTerm = new PhenotypeTerm("HP:0000175", "Cleft palate");
        assertTrue(result.contains(expectedTerm));
        assertTrue(result.size() >= 75);
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
        List<String> pfeifferAlternativeTerms = new ArrayList();
        pfeifferAlternativeTerms.add("ACROCEPHALOSYNDACTYLY, TYPE V; ACS");
        pfeifferAlternativeTerms.add("ACS V");
        pfeifferAlternativeTerms.add("NOACK SYNDROME");
        pfeifferAlternativeTerms.add("CRANIOFACIAL-SKELETAL-DERMATOLOGIC DYSPLASIA, INCLUDED");
        expectedResult.setAlternativeTerms(pfeifferAlternativeTerms);
        expectedResult.setLocus("10q26.13");
        List<String> pfeifferClasses = new ArrayList();
        pfeifferClasses.add("unclassified");
        expectedResult.setClasses(pfeifferClasses);

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
     * Test of getGeneToDiseaseAssociationSummaries method, of class
     * PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetGeneToDiseaseAssociationSummaries() {
//        logger.info("getGeneToDiseaseAssociationSummaries");
        GeneIdentifier geneId = new GeneIdentifier("Fgfr2", "MGI:95523");
        double cutoff = 0.00;
        List<DiseaseAssociationSummary> result = instance.getGeneToDiseaseAssociationSummaries(geneId, cutoff);
        logger.info("{} has {} DiseaseAssociationSummary using cutoff of {}", geneId, result.size(), cutoff);
        int expectSize = 7076;
        assertFalse(result.isEmpty());
    }

    /**
     * Test of getGeneToDiseaseAssociationSummaries method, of class
     * PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetGeneToDiseaseAssociationSummariesWithCutoff() {
//        logger.info("getGeneToDiseaseAssociationSummaries");
        GeneIdentifier geneId = new GeneIdentifier("Fgfr2", "MGI:95523");
        double cutoff = 2.00;
        List<DiseaseAssociationSummary> result = instance.getGeneToDiseaseAssociationSummaries(geneId, cutoff);
        logger.info("{} has {} DiseaseAssociationSummary using cutoff of {}", geneId, result.size(), cutoff);

        int expectSize = 310;
        assertFalse(result.isEmpty());
    }

    /**
     * Test of getDiseaseGeneAssociationDetail method, of class
     * PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetDiseaseGeneAssociationDetail() {
//        logger.info("getDiseaseGeneAssociationDetail");
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:101600");
        GeneIdentifier geneId = new GeneIdentifier("Fgfr2", "MGI:95523");

        DiseaseGeneAssociationDetail result = instance.getDiseaseGeneAssociationDetail(diseaseId, geneId);
        List<DiseaseModelAssociation> resultAssociations = result.getDiseaseAssociations();
        assertNotNull(result);
        assertNotNull(resultAssociations);
        assertFalse(resultAssociations.isEmpty());

        logger.info("{} - {} have the following {} DiseaseModelAssociations:", diseaseId, geneId, resultAssociations.size());
        int testModelId = 114;
        DiseaseModelAssociation associationToTest = null;
        for (DiseaseModelAssociation diseaseModelAssociation : resultAssociations) {
            if (diseaseModelAssociation.getMouseModel().getMgiModelId() == testModelId) {
                associationToTest = diseaseModelAssociation;
            }
            logger.info("{}", diseaseModelAssociation);
        }
        assertEquals(diseaseId, result.getDiseaseId());
//        assertEquals(18, result.getDiseaseAssociations().size());
        
        assertNotNull(associationToTest);
        logger.info("Testing: {}", associationToTest);

        assertEquals(72.65, associationToTest.getDiseaseToModelScore(), 0.00);
        assertEquals(74.90, associationToTest.getModelToDiseaseScore(), 0.00);

        //test the mouseModel
        MouseModel expectedModel = associationToTest.getMouseModel();

        assertNotNull(expectedModel);
        assertEquals("MGI:95523", expectedModel.getMgiGeneId());
        assertEquals(114, expectedModel.getMgiModelId(), 0);
        assertEquals("MGI:2153811", expectedModel.getAlleleIds());
        assertEquals("<a href=\"http://informatics.jax.org/accession/MGI:2153811\">Fgfr2<sup>tm1.1Dsn</sup></a>/<a href=\"http://informatics.jax.org/accession/MGI:2153811\">Fgfr2<sup>tm1.1Dsn</sup></a>", expectedModel.getAllelicCompositionLink());

        //test the mouseModel phenotypes
        PhenotypeTerm expectedTerm = new PhenotypeTerm("MP:0000111", "cleft palate");

        List<PhenotypeTerm> resultList = expectedModel.getPhenotypeTerms();

        assertNotNull("Expected a list, but got a null :(", resultList);
        assertFalse("Expected a full list, but got an empty one :(", resultList.isEmpty());
        assertTrue(String.format("Expected a list containing the term %s, but the term wasn't found :(", expectedTerm), resultList.contains(expectedTerm));

    }
}
