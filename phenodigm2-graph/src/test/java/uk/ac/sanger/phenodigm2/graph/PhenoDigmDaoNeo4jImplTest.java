/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.sanger.phenodigm2.graph;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
import uk.ac.sanger.phenodigm2.model.DiseaseAssociation;
import uk.ac.sanger.phenodigm2.model.Gene;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.model.MouseModel;
import uk.ac.sanger.phenodigm2.model.PhenotypeMatch;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;

/**
 *
 * @author jj8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-Graph.xml"})
public class PhenoDigmDaoNeo4jImplTest {

    Logger logger = LoggerFactory.getLogger(PhenoDigmDaoNeo4jImplTest.class);
   
    @Autowired
    PhenoDigmDaoNeo4jImpl instance;
    
    public PhenoDigmDaoNeo4jImplTest() {
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
     * Test of getAllDiseses method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetAllDiseses() {
        Set<Disease> result = instance.getAllDiseses();
//        for (Disease disease : result) {
//            logger.info("{}", disease);
//        }
        assertTrue(result.size() > 6700);
        assertFalse("Expected a full set of Disease objects, but got an empty one :(", result.isEmpty());
    }

    /**
     * Test of getAllGenes method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetAllGenes() {
        System.out.println("getAllGenes");
        PhenoDigmDaoNeo4jImpl instance = new PhenoDigmDaoNeo4jImpl();
        Set<Gene> expResult = null;
        Set<Gene> result = instance.getAllGenes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDiseaseByDiseaseId method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetDiseaseByDiseaseId() {
        System.out.println("getDiseaseByDiseaseId");
        String string = "";
        PhenoDigmDaoNeo4jImpl instance = new PhenoDigmDaoNeo4jImpl();
        Disease expResult = null;
        Disease result = instance.getDiseaseByDiseaseId(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDiseasesByHgncGeneId method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetDiseasesByHgncGeneId() {
        System.out.println("getDiseasesByHgncGeneId");
        String string = "";
        PhenoDigmDaoNeo4jImpl instance = new PhenoDigmDaoNeo4jImpl();
        Set<Disease> expResult = null;
        Set<Disease> result = instance.getDiseasesByHgncGeneId(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDiseasesByMgiGeneId method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetDiseasesByMgiGeneId() {
        System.out.println("getDiseasesByMgiGeneId");
        String string = "";
        PhenoDigmDaoNeo4jImpl instance = new PhenoDigmDaoNeo4jImpl();
        Set<Disease> expResult = null;
        Set<Disease> result = instance.getDiseasesByMgiGeneId(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getKnownDiseaseAssociationsForMgiGeneId method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetKnownDiseaseAssociationsForMgiGeneId() {
        System.out.println("getKnownDiseaseAssociationsForMgiGeneId");
        String string = "";
        PhenoDigmDaoNeo4jImpl instance = new PhenoDigmDaoNeo4jImpl();
        Map<Disease, Set<DiseaseAssociation>> expResult = null;
        Map<Disease, Set<DiseaseAssociation>> result = instance.getKnownDiseaseAssociationsForMgiGeneId(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPredictedDiseaseAssociationsForMgiGeneId method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetPredictedDiseaseAssociationsForMgiGeneId() {
        System.out.println("getPredictedDiseaseAssociationsForMgiGeneId");
        String string = "";
        PhenoDigmDaoNeo4jImpl instance = new PhenoDigmDaoNeo4jImpl();
        Map<Disease, Set<DiseaseAssociation>> expResult = null;
        Map<Disease, Set<DiseaseAssociation>> result = instance.getPredictedDiseaseAssociationsForMgiGeneId(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getKnownDiseaseAssociationsForDiseaseId method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetKnownDiseaseAssociationsForDiseaseId() {
        System.out.println("getKnownDiseaseAssociationsForDiseaseId");
        String string = "";
        PhenoDigmDaoNeo4jImpl instance = new PhenoDigmDaoNeo4jImpl();
        Map<GeneIdentifier, Set<DiseaseAssociation>> expResult = null;
        Map<GeneIdentifier, Set<DiseaseAssociation>> result = instance.getKnownDiseaseAssociationsForDiseaseId(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPredictedDiseaseAssociationsForDiseaseId method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetPredictedDiseaseAssociationsForDiseaseId() {
        System.out.println("getPredictedDiseaseAssociationsForDiseaseId");
        String string = "";
        PhenoDigmDaoNeo4jImpl instance = new PhenoDigmDaoNeo4jImpl();
        Map<GeneIdentifier, Set<DiseaseAssociation>> expResult = null;
        Map<GeneIdentifier, Set<DiseaseAssociation>> result = instance.getPredictedDiseaseAssociationsForDiseaseId(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllMouseGeneIdentifiers method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetAllMouseGeneIdentifiers() {
        System.out.println("getAllMouseGeneIdentifiers");
        PhenoDigmDaoNeo4jImpl instance = new PhenoDigmDaoNeo4jImpl();
        Set<GeneIdentifier> expResult = null;
        Set<GeneIdentifier> result = instance.getAllMouseGeneIdentifiers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGeneIdentifierForMgiGeneId method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetGeneIdentifierForMgiGeneId() {
        System.out.println("getGeneIdentifierForMgiGeneId");
        String string = "";
        PhenoDigmDaoNeo4jImpl instance = new PhenoDigmDaoNeo4jImpl();
        GeneIdentifier expResult = null;
        GeneIdentifier result = instance.getGeneIdentifierForMgiGeneId(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHumanOrthologIdentifierForMgiGeneId method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetHumanOrthologIdentifierForMgiGeneId() {
        System.out.println("getHumanOrthologIdentifierForMgiGeneId");
        String string = "";
        PhenoDigmDaoNeo4jImpl instance = new PhenoDigmDaoNeo4jImpl();
        GeneIdentifier expResult = null;
        GeneIdentifier result = instance.getHumanOrthologIdentifierForMgiGeneId(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDiseasePhenotypeTerms method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetDiseasePhenotypeTerms() {
        System.out.println("getDiseasePhenotypeTerms");
        String string = "";
        PhenoDigmDaoNeo4jImpl instance = new PhenoDigmDaoNeo4jImpl();
        List<PhenotypeTerm> expResult = null;
        List<PhenotypeTerm> result = instance.getDiseasePhenotypeTerms(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllMouseModels method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetAllMouseModels() {
        System.out.println("getAllMouseModels");
        PhenoDigmDaoNeo4jImpl instance = new PhenoDigmDaoNeo4jImpl();
        Set<MouseModel> expResult = null;
        Set<MouseModel> result = instance.getAllMouseModels();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMouseModelPhenotypeTerms method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetMouseModelPhenotypeTerms() {
        System.out.println("getMouseModelPhenotypeTerms");
        String string = "";
        PhenoDigmDaoNeo4jImpl instance = new PhenoDigmDaoNeo4jImpl();
        List<PhenotypeTerm> expResult = null;
        List<PhenotypeTerm> result = instance.getMouseModelPhenotypeTerms(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPhenotypeMatches method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetPhenotypeMatches() {
        System.out.println("getPhenotypeMatches");
        String string = "";
        String string1 = "";
        PhenoDigmDaoNeo4jImpl instance = new PhenoDigmDaoNeo4jImpl();
        List<PhenotypeMatch> expResult = null;
        List<PhenotypeMatch> result = instance.getPhenotypeMatches(string, string1);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGene method, of class PhenoGraphDAO.
     */
    @Test
    public void testGetGene() {
        System.out.println("getGene");
        GeneIdentifier gi = null;
        PhenoDigmDaoNeo4jImpl instance = new PhenoDigmDaoNeo4jImpl();
        Gene expResult = null;
        Gene result = instance.getGene(gi);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
