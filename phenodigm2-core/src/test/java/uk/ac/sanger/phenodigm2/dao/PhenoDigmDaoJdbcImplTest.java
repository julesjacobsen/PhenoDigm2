/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
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
import uk.ac.sanger.phenodigm2.model.CurationStatus;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseIdentifier;
import uk.ac.sanger.phenodigm2.model.DiseaseModelAssociation;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.model.MouseModel;
import uk.ac.sanger.phenodigm2.model.PhenotypeMatch;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;

/**
 *
 * @author jj8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:jdbc-test-services.xml"})
public class PhenoDigmDaoJdbcImplTest {
    
    Logger logger = LoggerFactory.getLogger(PhenoDigmDaoJdbcImplTest.class);

    @Autowired
    PhenoDigmDao instance;

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

    @Test
    public void testDbSetup() {
        String diseaseId = "OMIM:614298";
        Disease disease = instance.getDiseaseByDiseaseId(diseaseId);
        assertEquals(disease.getTerm(), "NEURODEGENERATION WITH BRAIN IRON ACCUMULATION 4; NBIA4");
    }
    
    @Test public void getAllGeneIdentifiers() {
        int result = instance.getAllMouseGeneIdentifiers().size();
        int expectedGeneIdentifiers = 4569;
        assertEquals(String.format("Expected %s gene identifiers but got %s",expectedGeneIdentifiers, result), result, expectedGeneIdentifiers);
    }
    
    @Test public void getAllDiseases() {
        Set<Disease> result = instance.getAllDiseses();
        int expectedDiseases = 5463;
        assertEquals("Expected " + expectedDiseases + " Disease objects, but got " + result.size() + " :(", result.size(), expectedDiseases);
        assertFalse("Expected a full set of Disease objects, but got an empty one :(", result.isEmpty());
    }
    
    @Test
    public void getGeneIdentifierForMgiGeneId() {
        String mgiGeneId = "MGI:95523";
        GeneIdentifier expResult = new GeneIdentifier("Fgfr2", "MGI:95523");
        GeneIdentifier result = instance.getGeneIdentifierForMgiGeneId(mgiGeneId);
        assertEquals(expResult, result);
    }
   
    @Test
    public void getHumanOrthologIdentifierForMgiGeneId() {
        String mgiGeneId = "MGI:95523";
        GeneIdentifier expResult = new GeneIdentifier("FGFR2", "HGNC:3689");
        GeneIdentifier result = instance.getHumanOrthologIdentifierForMgiGeneId(mgiGeneId);
        assertEquals(expResult, result);
    }
    
    @Test
    public void getGeneIdentifierForMgiGeneIdUnmappedInHGNC() {
        String mgiGeneId = "MGI:FOOP!";
        GeneIdentifier expResult = null;
        GeneIdentifier result = instance.getGeneIdentifierForMgiGeneId(mgiGeneId);
        assertEquals(expResult, result);
    }
   
    @Test
    public void getHumanOrthologIdentifierForMgiGeneIdUnmappedInHGNC() {
        String mgiGeneId = "MGI:FOOP!";
        GeneIdentifier expResult = null;
        GeneIdentifier result = instance.getHumanOrthologIdentifierForMgiGeneId(mgiGeneId);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of setUpDiseaseCache method, of class JdbcDiseaseDAOImpl.
     */
    @Test
    public void testsetUpDiseaseCache() {
        String omimDiseaseId = "OMIM:101600";
        Disease expectedResult = new Disease();
        expectedResult.setDiseaseIdentifier(new DiseaseIdentifier(omimDiseaseId));
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
        Disease result = instance.getDiseaseByDiseaseId(omimDiseaseId);
        assertEquals(expectedResult, result);

    }

    /**
     * Test of getDiseaseByDiseaseId method, of class JdbcDiseaseDAOImpl.
     */
    @Test
    public void testGetDiseaseByOmimDiseaseId() {
        String omimDiseaseId = "OMIM:101600";

        Disease result = instance.getDiseaseByDiseaseId(omimDiseaseId);
        assertEquals("PFEIFFER SYNDROME", result.getTerm());

    }

    /**
     * Test of getDiseasesByHgncGeneId method, of class JdbcDiseaseDAOImpl.
     */
    @Test
    public void testGetDiseasesByHgncGeneId() {
        String hgncGeneId = "HGNC:3689";

        Set<Disease> result = instance.getDiseasesByHgncGeneId(hgncGeneId);
//        System.out.println("Human diseases for gene " + hgncGeneId);
//        for (Disease disease : result) {
//            System.out.println(disease.getDiseaseId() + " - " + disease.getTerm());
//        }
        assertTrue(result.size() >= 14);
    }

    /**
     * Test of getDiseasesByMgiGeneId method, of class JdbcDiseaseDAOImpl.
     */
    @Test
    public void testGetDiseasesByMgiGeneId() {
        String mgiGeneId = "MGI:95523";

        Set<Disease> result = instance.getDiseasesByMgiGeneId(mgiGeneId);
        System.out.println("Human ortholog diseases for mouse gene " + mgiGeneId);
        for (Disease disease : result) {
            System.out.println(disease.getDiseaseId() + " - " + disease.getTerm());
        }
        assertTrue(result.size() >= 14);

    }
    
    /**
     * Test of getDiseasesByMgiGeneId method, of class
     * JdbcDiseaseDAOImpl.
     */
    @Test
    public void testGetDiseasesByMgiGeneIdNoMappedOrtholog() {
        String mgiGeneId = "MGI:flipWooP!";

        Set<Disease> result = instance.getDiseasesByMgiGeneId(mgiGeneId);   

        assertTrue("Expected empty result, but got: " + result, result.isEmpty());
        
        
    }
    
    /**
     * Test of getKnownDiseaseAssociationsForMgiGeneId method, of class
     * JdbcDiseaseDAOImpl.
     */
    @Test
    public void testGetKnownDiseaseAssociationsForMgiGeneId() {
        String mgiGeneId = "MGI:95523";

        Map<Disease, Set<DiseaseModelAssociation>> result = instance.getKnownDiseaseAssociationsForMgiGeneId(mgiGeneId);

        for (Disease disease : result.keySet()) {
//            System.out.println(disease);
            Set<DiseaseModelAssociation> diseaseAssociations = result.get(disease);
            if (disease.getDiseaseId().equals("OMIM:101200")) {
                assertEquals(2, diseaseAssociations.size());
            }
            for (DiseaseModelAssociation diseaseAssociation : diseaseAssociations) {
//                System.out.println(String.format("    %s", diseaseAssociation));
            }
        }

        assertTrue(result.keySet().size() >= 11);
        
        
    }

    /**
     * Test of getPredictedDiseaseAssociationsForMgiGeneId method, of class
     * JdbcDiseaseDAOImpl.
     */
    @Test
    public void testGetPredictedDiseaseAssociationsForMgiGeneId() {
        String mgiGeneId = "MGI:95523";

        Map<Disease, Set<DiseaseModelAssociation>> result = instance.getPredictedDiseaseAssociationsForMgiGeneId(mgiGeneId);
      
//        for (Disease disease : result.keySet()) {
//            System.out.println(disease);
//            for (DiseaseAssociation diseaseAssociation : result.get(disease)) {
//                System.out.println(String.format("    %s", diseaseAssociation));
//            }
//        }
        assertTrue(result.keySet().size() > 290);
    }

    @Test 
    public void testGetKnownDiseaseAssociationsForDiseaseId() {
        String diseaseId = "OMIM:101600";
        
        Map<GeneIdentifier, Set<DiseaseModelAssociation>> result = instance.getKnownDiseaseAssociationsForDiseaseId(diseaseId);
        
        int resultSize = result.keySet().size();
        int expectSize = 2;
        String sizeErrorMessage = String.format("Expected %d genes associated with %s. Found %d", expectSize, diseaseId, resultSize);
        assertEquals(sizeErrorMessage, resultSize, expectSize);
        
        GeneIdentifier fgfr1 = new GeneIdentifier("Fgfr1", "MGI:95522");
        assertTrue("Expected gene " + fgfr1 + "to be in result set", result.keySet().contains(fgfr1));
        
        GeneIdentifier fgfr2 = new GeneIdentifier("Fgfr2", "MGI:95523");
        assertTrue("Expected gene " + fgfr2 + "to be in result set", result.keySet().contains(fgfr2));
        
//        System.out.println(result);
    }
    
    @Test 
    public void testGetPredictedDiseaseAssociationsForDiseaseId() {
        String diseaseId = "OMIM:101600";
        
        Map<GeneIdentifier, Set<DiseaseModelAssociation>> result = instance.getPredictedDiseaseAssociationsForDiseaseId(diseaseId);

//        System.out.println(result);

        int resultSize = result.keySet().size();
        int expectSize = 46;
        String sizeErrorMessage = String.format("Expected more than %d genes associated with %s. Found %d", expectSize, diseaseId, resultSize);
        assertTrue(sizeErrorMessage, resultSize > expectSize);
        
        GeneIdentifier gja1 = new GeneIdentifier("Gja1", "MGI:95713");
        assertTrue("Expected gene " + gja1 + "to be in result set", result.keySet().contains(gja1));
        
        GeneIdentifier fgfr2 = new GeneIdentifier("Fgfr2", "MGI:95523");
        assertTrue("Expected gene " + fgfr2 + "to be in result set", result.keySet().contains(fgfr2));
        
        GeneIdentifier fgfr3 = new GeneIdentifier("Fgfr3", "MGI:95524");
        assertTrue("Expected gene " + fgfr3 + "to be in result set", result.keySet().contains(fgfr3));
        
             
    }
    
    @Test
    public void testGetDiseasePhenotypeTerms() {
        String diseaseId = "OMIM:101200";
        List<PhenotypeTerm> result = instance.getDiseasePhenotypeTerms(diseaseId);
        PhenotypeTerm expectedTerm = new PhenotypeTerm();
        expectedTerm.setId("HP:0000175");
        expectedTerm.setTerm("Cleft palate");
        assertTrue(result.contains(expectedTerm));
    }
    
    @Test
    public void testGetMouseModelPhenotypeTerms() {
        String mouseModelId = "1";
        List<PhenotypeTerm> result = instance.getMouseModelPhenotypeTerms(mouseModelId);
        PhenotypeTerm expectedTerm = new PhenotypeTerm();
        expectedTerm.setId("MP:0000609");
        expectedTerm.setTerm("abnormal liver physiology");
        assertTrue(result.contains(expectedTerm));
    }
    
    @Test
    public void testGetDiseaseAssociationPhenotypeMatches() {
        String diseaseId = "OMIM:101600";
        String mouseModelId = "27680";
        List<PhenotypeMatch> result = instance.getPhenotypeMatches(diseaseId, mouseModelId);
        PhenotypeMatch expectedMatch = new PhenotypeMatch();
        expectedMatch.setSimJ(0.853968);
        expectedMatch.setIc(8.901661);
        expectedMatch.setLcs("HP_0009702 Carpal synostosis ^ MP_0008915 fused carpal bones");
        
        PhenotypeTerm humanTerm = new PhenotypeTerm();
        humanTerm.setId("HP:0005048");
        humanTerm.setTerm("Synostosis of carpal bones");
        expectedMatch.setHumanPhenotype(humanTerm);
        
        PhenotypeTerm mouseTerm = new PhenotypeTerm();
        mouseTerm.setId("MP:0008915");
        mouseTerm.setTerm("fused carpal bones");
        expectedMatch.setMousePhenotype(mouseTerm);
        
        PhenotypeMatch matchResult = null;
        for (PhenotypeMatch phenotypeMatch : result) {
            if (phenotypeMatch.equals(expectedMatch)) {
                matchResult = phenotypeMatch;
            }
        }
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
        Map<Disease, Set<DiseaseModelAssociation>> knownDiseases = diseaseDao.getKnownDiseaseAssociationsForMgiGeneId(mgiGeneId);
        Map<Disease, Set<DiseaseModelAssociation>> predictedDiseases = diseaseDao.getPredictedDiseaseAssociationsForMgiGeneId(mgiGeneId);

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