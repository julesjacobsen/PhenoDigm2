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

import java.util.*;

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
import uk.ac.sanger.phenodigm2.model.MouseModel;
import uk.ac.sanger.phenodigm2.model.PhenotypeMatch;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;

/**
 * Abstract test class for testing any implementations of the PhenoDigmDao 
 * interface.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public abstract class PhenoDigmDaoTest {
    
    @Autowired
    PhenoDigmDao instance;
    
    static Logger logger;

    
    @Test
    public void testDbSetup() {
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:614298");
        Disease disease = instance.getDisease(diseaseId);
        assertEquals(disease.getTerm(), "NEURODEGENERATION WITH BRAIN IRON ACCUMULATION 4; NBIA4");
    }
        
    @Test public void getAllDiseases() {
        Set<Disease> result = instance.getAllDiseses();
        int expectedDiseases = 7346;
        assertEquals(String.format("Expected %s Disease objects, but got %s :(", expectedDiseases, result.size()), expectedDiseases, result.size());
        assertFalse("Expected a full set of Disease objects, but got an empty one :(", result.isEmpty());
    }
        
    /**
     * Test of setUpDiseaseCache method, of class JdbcDiseaseDAOImpl.
     */
    @Test
    public void testsetUpDiseaseCache() {
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
        expectedResult.setClasses(Arrays.asList("unclassified"));
        
        Disease result = instance.getDisease(omimDiseaseId);
        assertEquals(expectedResult, result);

    }

    /**
     * Test of getDisease method, of class JdbcDiseaseDAOImpl.
     */
    @Test
    public void testGetDiseaseByOmimDiseaseId() {
        String omimDiseaseId = "OMIM:101600";

        Disease result = instance.getDisease(new DiseaseIdentifier(omimDiseaseId));
        assertEquals("PFEIFFER SYNDROME", result.getTerm());

    }

//    /**
//     * Test of getKnownDiseaseAssociationsForMgiGeneId method, of class
//     * JdbcDiseaseDAOImpl.
//     */
//    @Test
//    public void testGetKnownDiseaseAssociationsForMgiGeneId() {
//        String mgiGeneId = "MGI:95523";
//
//        Map<Disease, Set<DiseaseModelAssociation>> result = instance.getKnownDiseaseAssociationsForMgiGeneId(mgiGeneId);
//
//        for (Disease disease : result.keySet()) {
////            System.out.println(disease);
//            Set<DiseaseModelAssociation> diseaseAssociations = result.get(disease);
//            if (disease.getDiseaseId().equals("OMIM:101200")) {
//                assertEquals(2, diseaseAssociations.size());
//            }
//            for (DiseaseModelAssociation diseaseAssociation : diseaseAssociations) {
////                System.out.println(String.format("    %s", diseaseAssociation));
//            }
//        }
//
//        assertTrue(result.keySet().size() >= 11);
//        
//        
//    }
//
//    /**
//     * Test of getPredictedDiseaseAssociationsForMgiGeneId method, of class
//     * JdbcDiseaseDAOImpl.
//     */
//    @Test
//    public void testGetPredictedDiseaseAssociationsForMgiGeneId() {
//        String mgiGeneId = "MGI:95523";
//
//        Map<Disease, Set<DiseaseModelAssociation>> result = instance.getPredictedDiseaseAssociationsForMgiGeneId(mgiGeneId);
//      
////        for (Disease disease : result.keySet()) {
////            System.out.println(disease);
////            for (DiseaseAssociation diseaseAssociation : result.get(disease)) {
////                System.out.println(String.format("    %s", diseaseAssociation));
////            }
////        }
//        assertTrue(result.keySet().size() > 290);
//    }
//
//    @Test 
//    public void testGetKnownDiseaseAssociationsForDiseaseId() {
//        String diseaseId = "OMIM:101600";
//        
//        Map<GeneIdentifier, Set<DiseaseModelAssociation>> result = instance.getKnownDiseaseAssociationsForDiseaseId(diseaseId);
//        
//        int resultSize = result.keySet().size();
//        int expectSize = 2;
//        String sizeErrorMessage = String.format("Expected %d genes associated with %s. Found %d", expectSize, diseaseId, resultSize);
//        assertEquals(sizeErrorMessage, resultSize, expectSize);
//        
//        GeneIdentifier fgfr1 = new GeneIdentifier("Fgfr1", "MGI:95522");
//        assertTrue("Expected gene " + fgfr1 + "to be in result set", result.keySet().contains(fgfr1));
//        
//        GeneIdentifier fgfr2 = new GeneIdentifier("Fgfr2", "MGI:95523");
//        assertTrue("Expected gene " + fgfr2 + "to be in result set", result.keySet().contains(fgfr2));
//        
////        System.out.println(result);
//    }
//    
//    @Test 
//    public void testGetPredictedDiseaseAssociationsForDiseaseId() {
//        String diseaseId = "OMIM:101600";
//        
//        Map<GeneIdentifier, Set<DiseaseModelAssociation>> result = instance.getPredictedDiseaseAssociationsForDiseaseId(diseaseId);
//
////        System.out.println(result);
//
//        int resultSize = result.keySet().size();
//        int expectSize = 46;
//        String sizeErrorMessage = String.format("Expected more than %d genes associated with %s. Found %d", expectSize, diseaseId, resultSize);
//        assertTrue(sizeErrorMessage, resultSize > expectSize);
//        
//        GeneIdentifier gja1 = new GeneIdentifier("Gja1", "MGI:95713");
//        assertTrue("Expected gene " + gja1 + "to be in result set", result.keySet().contains(gja1));
//        
//        GeneIdentifier fgfr2 = new GeneIdentifier("Fgfr2", "MGI:95523");
//        assertTrue("Expected gene " + fgfr2 + "to be in result set", result.keySet().contains(fgfr2));
//        
//        GeneIdentifier fgfr3 = new GeneIdentifier("Fgfr3", "MGI:95524");
//        assertTrue("Expected gene " + fgfr3 + "to be in result set", result.keySet().contains(fgfr3));
//        
//             
//    }
    
    @Test
    public void testGetDiseasePhenotypeTerms() {
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:101200");
        List<PhenotypeTerm> result = instance.getDiseasePhenotypes(diseaseId);
        PhenotypeTerm expectedTerm = new PhenotypeTerm("HP:0000175", "Cleft palate");
        assertTrue(result.contains(expectedTerm));
    }
    
    @Test
    public void testGetMouseModelPhenotypeTerms() {
        String mouseModelId = "114";
        List<PhenotypeTerm> result = instance.getMouseModelPhenotypes(mouseModelId);
        PhenotypeTerm expectedTerm = new PhenotypeTerm("MP:0000111", "cleft palate");
        assertTrue(result.contains(expectedTerm));
    }
    
    @Test
    public void testGetDiseaseAssociationPhenotypeMatches() {
        String diseaseId = "OMIM:101600";
        Integer mouseModelId = 42;
        List<PhenotypeMatch> result = instance.getPhenotypeMatches(diseaseId, mouseModelId);
        PhenotypeMatch expectedMatch = new PhenotypeMatch();
        expectedMatch.setSimJ(0.853968);
        expectedMatch.setIc(8.901661);
        //fused carpal bones (MP:0008915)
        expectedMatch.setLcs("");

        expectedMatch.setHumanPhenotype(new PhenotypeTerm("HP:0005048", "Synostosis of carpal bones"));
        expectedMatch.setMousePhenotype(new PhenotypeTerm("MP:0008915", "fused carpal bones"));
        
        PhenotypeMatch matchResult = null;
        for (PhenotypeMatch phenotypeMatch : result) {
            if (phenotypeMatch.equals(expectedMatch)) {
                matchResult = phenotypeMatch;
            }
        }
        logger.info(matchResult.toString());

        assertNotNull(matchResult);
        assertEquals(expectedMatch.getLcs(), matchResult.getLcs());
        //make sure there are other matches in there too!
        assertTrue(result.size() > 1);
    }
    
    @Test
    public void genePageIntegrationTest() {
        //this is a test of how the DAO will be used within the controller and
        //displayed in the view
        PhenoDigmDao diseaseDao = instance;

        String mgiGeneId = "MGI:95523";
        System.out.println(String.format("\n\nGene: %s", mgiGeneId));
        System.out.println("Known Disease Associations:");
        Map<Disease, Set<DiseaseModelAssociation>> knownDiseases = new HashMap<>();// diseaseDao.getKnownDiseaseAssociationsForMgiGeneId(mgiGeneId);
        Map<Disease, Set<DiseaseModelAssociation>> predictedDiseases = new HashMap<>();// diseaseDao.getPredictedDiseaseAssociationsForMgiGeneId(mgiGeneId);

        if (knownDiseases.keySet().isEmpty()) {
            System.out.println("  No known disease associations for " + mgiGeneId);
        }
        for (Disease disease : knownDiseases.keySet()) {
            System.out.println(disease.getTerm());
            System.out.println(disease);
            System.out.println(String.format("%n  Mouse Disease Models with Phenotypic Similarity to %s - via Literature:", disease.getTerm()));
            Set<DiseaseModelAssociation> literatureDiseaseAssociations = knownDiseases.get(disease);
            System.out.print(formatDiseaseAssociations(literatureDiseaseAssociations));

            System.out.println(String.format("%n  Phenotypic Matches to Mouse Models of %s:", disease.getTerm()));
            Set<DiseaseModelAssociation> phenotypicDiseaseAssociations = predictedDiseases.get(disease);
            if (phenotypicDiseaseAssociations == null) {
                phenotypicDiseaseAssociations = new TreeSet<DiseaseModelAssociation>();
            }
            System.out.print(formatDiseaseAssociations(phenotypicDiseaseAssociations));
        }

        System.out.println("\nPredicted Disease Associations: (first 10 only)");
        if (knownDiseases.keySet().isEmpty()) {
            System.out.println("  No predicted disease associations for " + mgiGeneId);
        }
        
        Iterator<Disease> predictedDiseaseIterator = predictedDiseases.keySet().iterator();
        for (int i = 0; i < 10; i++) {
            if (predictedDiseaseIterator.hasNext()) {
                Disease disease = predictedDiseaseIterator.next();
                System.out.println(String.format("  %s %s", disease.getTerm(), disease.getDiseaseId()));
                System.out.print(formatDiseaseAssociations(predictedDiseases.get(disease)));    
            }    
        }
        
        System.out.println("--------------------------------------------------------------------------------------------------\n");
    }

    private String formatDiseaseAssociations(Set<DiseaseModelAssociation> diseaseAssociations) {
        StringBuffer stringBuffer = new StringBuffer("");
        if (diseaseAssociations.isEmpty()) {
            return String.format("    No significant disease phenotype associations found.%n");
        }
        for (DiseaseModelAssociation diseaseAssociation : diseaseAssociations) {
            MouseModel associatedModel = diseaseAssociation.getMouseModel();
            stringBuffer.append(String.format("    d2mScore:%s %s%n", diseaseAssociation.getModelToDiseaseScore(), associatedModel));
            stringBuffer.append(String.format("      PhenotypeMatches: %s%n", diseaseAssociation.getPhenotypeMatches()));
        }
        return stringBuffer.toString();
    }
}
