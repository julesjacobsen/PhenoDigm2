/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseAssociation;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.model.MouseModel;

/**
 *
 * @author jj8
 */
public class PhenoDigmDaoStubImplTest {
    
    @Autowired
    private static PhenoDigmDaoStubImpl testDiseaseDao;

    
    public PhenoDigmDaoStubImplTest() {
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
     * Test of getDiseaseByOmimId method, of class DiseaseDAOImpl.
     */
    @Test
    public void testGetDiseaseByOmimDiseaseId() {
        System.out.println("getDiseaseByOmimDiseaseId");
        testDiseaseDao = PhenoDigmDaoStubImpl.getInstance();
        
        String omimId = "OMIM:101200";
        
        Disease result = testDiseaseDao.getDiseaseByDiseaseId(omimId);
        System.out.println(result);
        assertEquals("APERT SYNDROME", result.getTerm());
    }

    /**
     * Test of getDiseasesByMgiGeneId method, of class DiseaseDAOImpl.
     */
    @Test
    public void testGetDiseaseByOmimGeneId() {
        System.out.println("getDiseaseByOmimGeneId");
        String omimGeneId = "OMIM:176943";
        PhenoDigmDaoStubImpl instance = PhenoDigmDaoStubImpl.getInstance();

        Set<Disease> result = instance.getDiseasesByHgncGeneId(omimGeneId);
        System.out.println("Human diseases for gene " + omimGeneId);
        for (Disease disease : result) {
            System.out.println(disease.getDiseaseId() + " - " + disease.getTerm());
        }
        assertEquals(11, result.size());

    }

    /**
     * Test of getDiseasesByMgiGeneId method, of class DiseaseDAOImpl.
     */
    @Test
    public void testGetDiseaseByMgiGeneId() {
        System.out.println("getDiseaseByMgiGeneId");
        String mgiGeneId = "MGI:95523";
        PhenoDigmDaoStubImpl instance = PhenoDigmDaoStubImpl.getInstance();

        Set<Disease> result = instance.getDiseasesByMgiGeneId(mgiGeneId);
        System.out.println("Human ortholog diseases for mouse gene " + mgiGeneId);
        for (Disease disease : result) {
            System.out.println(disease.getDiseaseId() + " - " + disease.getTerm());
        }
        assertEquals(11, result.size());

    }

    /**
     * Test of getKnownDiseaseAssociationsForMgiGeneId method, of class DiseaseDAOImpl.
     */
    @Test
    public void testGetKnownDiseaseAssociationsForMgiGeneId() {
        System.out.println("getKnownDiseaseAssociationsForMgiGeneId");
        String mgiGeneId = "MGI:95523";
        PhenoDigmDaoStubImpl instance = PhenoDigmDaoStubImpl.getInstance();
        Map<Disease, Set<DiseaseAssociation>> result = instance.getKnownDiseaseAssociationsForMgiGeneId(mgiGeneId);

        for (Disease disease : result.keySet()) {
            System.out.println(disease);
            for (DiseaseAssociation diseaseAssociation : result.get(disease)) {
                System.out.println(String.format("    %s",diseaseAssociation));
            }
        }

        assertEquals(11, result.keySet().size());

    }

    
    @Test
    public void testGetGeneIdentifierForMgiGeneId() {
        String mgiGeneId = "MGI:95523";
        GeneIdentifier expResult = new GeneIdentifier("Fgfr2", "MGI:95523");
        GeneIdentifier result = PhenoDigmDaoStubImpl.getInstance().getGeneIdentifierForMgiGeneId(mgiGeneId);
//        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetHumanOrthologIdentifierForMgiGeneId() {
        String mgiGeneId = "MGI:95523";
        GeneIdentifier expResult = new GeneIdentifier("FGFR2", "OMIM:176943");
        GeneIdentifier result = PhenoDigmDaoStubImpl.getInstance().getHumanOrthologIdentifierForMgiGeneId(mgiGeneId);
//        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPredictedDiseaseAssociationsForMgiGeneId method, of class DiseaseDAOImpl.
     */
    @Test
    public void testGetPredictedDiseaseAssociationsForMgiGeneId() {
        System.out.println("getPredictedDiseaseAssociationsForMgiGeneId");
        String mgiGeneId = "MGI:95523";
        PhenoDigmDaoStubImpl instance = PhenoDigmDaoStubImpl.getInstance();
        Map<Disease, Set<DiseaseAssociation>> result = instance.getPredictedDiseaseAssociationsForMgiGeneId(mgiGeneId);
        
        for (Disease disease : result.keySet()) {
            System.out.println(disease);
            for (DiseaseAssociation diseaseAssociation : result.get(disease)) {
                System.out.println(String.format("    %s",diseaseAssociation));
            }
        }
        
        assertEquals(295, result.keySet().size());
    }
    
    @Test
    public void genePageIntegrationTest(){
        //this is a test of how the DAO will be used within the controller and
        //displayed in the view
        PhenoDigmDao diseaseDao = PhenoDigmDaoStubImpl.getInstance();
        
        String mgiGeneId = "MGI:95523";
        
        System.out.println("\n\nKnown Disease Associations:");
        Map<Disease, Set<DiseaseAssociation>> knownDiseases = diseaseDao.getKnownDiseaseAssociationsForMgiGeneId(mgiGeneId);
        Map<Disease, Set<DiseaseAssociation>> predictedDiseases = diseaseDao.getPredictedDiseaseAssociationsForMgiGeneId(mgiGeneId);

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
            System.out.println("--------------------------------------------------------------------------------------------------\n");
        }
        
        System.out.println("Predicted Disease Associations:");
        
        for (Disease disease : predictedDiseases.keySet()) {
            System.out.println(String.format("  %s %s", disease.getTerm(), disease.getDiseaseId()));
            System.out.print(formatDiseaseAssociations(predictedDiseases.get(disease)));
        }   
        
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