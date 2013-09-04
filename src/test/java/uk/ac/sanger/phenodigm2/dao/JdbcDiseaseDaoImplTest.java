/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseAssociation;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.model.MouseModel;
import uk.ac.sanger.phenodigm2.model.PhenotypeMatch;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;

/**
 *
 * @author jj8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/jdbc-services.xml"})
public class JdbcDiseaseDaoImplTest {

    @Autowired
    DiseaseDao instance;

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
    public void getGeneIdentifierForMgiGeneId() {
        String mgiGeneId = "MGI:95523";
        GeneIdentifier expResult = new GeneIdentifier("Fgfr2", "MGI:95523");
        GeneIdentifier result = instance.getGeneIdentifierForMgiGeneId(mgiGeneId);
        assertEquals(expResult, result);
    }
    
    @Test
    public void getHumanOrthologIdentifierForMgiGeneId() {
        String mgiGeneId = "MGI:95523";
        GeneIdentifier expResult = new GeneIdentifier("FGFR2", "OMIM:176943");
        GeneIdentifier result = instance.getHumanOrthologIdentifierForMgiGeneId(mgiGeneId);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getDiseaseByDiseaseId method, of class JdbcDiseaseDAOImpl.
     */
    @Test
    public void testGetDiseaseByOmimDiseaseId() {
        System.out.println("getDiseaseByOmimDiseaseId");
        String omimDiseaseId = "OMIM:101600";

        Disease result = instance.getDiseaseByDiseaseId(omimDiseaseId);
        assertEquals("PFEIFFER SYNDROME", result.getTerm());

    }

    /**
     * Test of getDiseasesByOmimGeneId method, of class JdbcDiseaseDAOImpl.
     */
    @Test
    public void testGetDiseasesByOmimGeneId() {
        System.out.println("getDiseasesByOmimGeneId");
        String omimGeneId = "OMIM:176943";

        Set<Disease> result = instance.getDiseasesByOmimGeneId(omimGeneId);
        System.out.println("Human diseases for gene " + omimGeneId);
        for (Disease disease : result) {
            System.out.println(disease.getOmimId() + " - " + disease.getTerm());
        }
        assertEquals(11, result.size());
    }

    /**
     * Test of getDiseasesByMgiGeneId method, of class JdbcDiseaseDAOImpl.
     */
    @Test
    public void testGetDiseasesByMgiGeneId() {
        System.out.println("getDiseasesByMgiGeneId");
        String mgiGeneId = "MGI:95523";

        Set<Disease> result = instance.getDiseasesByMgiGeneId(mgiGeneId);
        System.out.println("Human ortholog diseases for mouse gene " + mgiGeneId);
        for (Disease disease : result) {
            System.out.println(disease.getOmimId() + " - " + disease.getTerm());
        }
        assertEquals(11, result.size());

    }

    /**
     * Test of getKnownDiseaseAssociationsForMgiGeneId method, of class
     * JdbcDiseaseDAOImpl.
     */
    @Test
    public void testGetKnownDiseaseAssociationsForMgiGeneId() {
        System.out.println("getKnownDiseaseAssociationsForMgiGeneId");
        String mgiGeneId = "MGI:95523";

        Map<Disease, Set<DiseaseAssociation>> result = instance.getKnownDiseaseAssociationsForMgiGeneId(mgiGeneId);

        for (Disease disease : result.keySet()) {
            System.out.println(disease);
            for (DiseaseAssociation diseaseAssociation : result.get(disease)) {
                System.out.println(String.format("    %s", diseaseAssociation));
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
        System.out.println("getPredictedDiseaseAssociationsForMgiGeneId");
        String mgiGeneId = "MGI:95523";

        Map<Disease, Set<DiseaseAssociation>> result = instance.getPredictedDiseaseAssociationsForMgiGeneId(mgiGeneId);
      
//        for (Disease disease : result.keySet()) {
//            System.out.println(disease);
//            for (DiseaseAssociation diseaseAssociation : result.get(disease)) {
//                System.out.println(String.format("    %s", diseaseAssociation));
//            }
//        }
        assertTrue(result.keySet().size() > 290);
    }

    @Test
    public void testGetDiseasePhenotypeTerms() {
        String diseaseId = "OMIM:101200";
        List<PhenotypeTerm> result = instance.getDiseasePhenotypeTerms(diseaseId);
        PhenotypeTerm expectedTerm = new PhenotypeTerm();
        expectedTerm.setTermId("HP:0000175");
        expectedTerm.setName("Cleft palate");
        expectedTerm.setDefinition("Cleft palate is a developmental defect of the `palate` (FMA:54549) resulting from a failure of fusion of the palatine processes and manifesting as a spearation of the roof of the mouth (soft and hard palate).");
        expectedTerm.setComment("Cleft palate is a developmental defect that occurs between the 7th and 12th week of pregnancy. Normally, the palatine processes fuse during this time to form the soft and hard palate. A failure of fusion results in a cleft palate. The clinical spectrum ranges from bifid uvula, to (incomplete or complete) cleft of the soft palate, up to (complete or incomplete) cleft of both the soft and hard palate.");
        assertTrue(result.contains(expectedTerm));
    }
    
    @Test
    public void testGetMouseModelPhenotypeTerms() {
        String mouseModelId = "1";
        List<PhenotypeTerm> result = instance.getMouseModelPhenotypeTerms(mouseModelId);
        PhenotypeTerm expectedTerm = new PhenotypeTerm();
        expectedTerm.setTermId("MP:0000609");
        expectedTerm.setName("abnormal liver physiology");
        expectedTerm.setDefinition("any functional anomaly of the bile-secreting organ that is important for detoxification, for fat, carbohydrate, and protein metabolism, and for glycogen storage");
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
        
        PhenotypeTerm humanTerm = new PhenotypeTerm();
        humanTerm.setTermId("HP:0005048");
        humanTerm.setName("Synostosis of carpal bones");
        expectedMatch.setHumanPhenotype(humanTerm);
        
        PhenotypeTerm mouseTerm = new PhenotypeTerm();
        mouseTerm.setTermId("MP:0008915");
        mouseTerm.setName("fused carpal bones");
        expectedMatch.setMousePhenotype(mouseTerm);
        
        assertTrue(result.contains(expectedMatch));
    }
    
    @Test
    public void genePageIntegrationTest() {
        //this is a test of how the DAO will be used within the controller and
        //displayed in the view
        DiseaseDao diseaseDao = instance;

        String mgiGeneId = "MGI:95523";
        System.out.println(String.format("\n\nGene: %s", mgiGeneId));
        System.out.println("Known Disease Associations:");
        Map<Disease, Set<DiseaseAssociation>> knownDiseases = diseaseDao.getKnownDiseaseAssociationsForMgiGeneId(mgiGeneId);
        Map<Disease, Set<DiseaseAssociation>> predictedDiseases = diseaseDao.getPredictedDiseaseAssociationsForMgiGeneId(mgiGeneId);

        if (knownDiseases.keySet().isEmpty()) {
            System.out.println("  No known disease associations for " + mgiGeneId);
        }
        for (Disease disease : knownDiseases.keySet()) {
            System.out.println(disease.getTerm());
            System.out.println(disease);
            System.out.println(String.format("%n  Mouse Disease Models with Phenotypic Similarity to %s - via Literature:", disease.getTerm()));
            Set<DiseaseAssociation> literatureDiseaseAssociations = knownDiseases.get(disease);
            System.out.print(formatDiseaseAssociations(literatureDiseaseAssociations));

            System.out.println(String.format("%n  Phenotypic Matches to Mouse Models of %s:", disease.getTerm()));
            Set<DiseaseAssociation> phenotypicDiseaseAssociations = predictedDiseases.get(disease);
            if (phenotypicDiseaseAssociations == null) {
                phenotypicDiseaseAssociations = new TreeSet<DiseaseAssociation>();
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
                System.out.println(String.format("  %s %s", disease.getTerm(), disease.getOmimId()));
                System.out.print(formatDiseaseAssociations(predictedDiseases.get(disease)));    
            }    
        }
        
        System.out.println("--------------------------------------------------------------------------------------------------\n");
    }

    private String formatDiseaseAssociations(Set<DiseaseAssociation> diseaseAssociations) {
        StringBuffer stringBuffer = new StringBuffer("");
        if (diseaseAssociations.isEmpty()) {
            return String.format("    No significant disease phenotype associations found.%n");
        }
        for (DiseaseAssociation diseaseAssociation : diseaseAssociations) {
            MouseModel associatedModel = diseaseAssociation.getMouseModel();
            stringBuffer.append(String.format("    d2mScore:%s %s%n", diseaseAssociation.getModelToDiseaseScore(), associatedModel));
            stringBuffer.append(String.format("      PhenotypeMatches: %s%n", diseaseAssociation.getPhenotypeMatches()));
        }
        return stringBuffer.toString();
    }
}