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
import uk.ac.sanger.phenodigm2.model.DiseaseIdentifier;
import uk.ac.sanger.phenodigm2.model.DiseaseModelAssociation;
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
@ContextConfiguration(locations = {"classpath:/application-test-context.xml"})
public class PhenoDigmDaoNeo4jImplTest {

    Logger logger = LoggerFactory.getLogger(PhenoDigmDaoNeo4jImplTest.class);
   
//    @Autowired
    PhenoDigmDaoNeo4jImpl instance;
        
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void assertThisIsTrue() {
        //this whole package needs sorting - it's utterly experimental and is currently abandoned.
        assertTrue(true);
    }
//    @Test 
//    public void getAllGeneIdentifiers() {
//        assertTrue(instance.getAllMouseGeneIdentifiers().size() > 9000);
//    }
    
//    @Test 
//    public void getAllDiseases() {
//        Set<Disease> result = instance.getAllDiseses();
//        assertTrue(result.size() > 6700);
//        assertFalse("Expected a full set of Disease objects, but got an empty one :(", result.isEmpty());
//    }
//    
//    /**
//     * Test of setUpDiseaseCache method, of class JdbcDiseaseDAOImpl.
//     */
//    @Test
//    public void testsetUpDiseaseCache() {
//        DiseaseIdentifier omimDiseaseId = new DiseaseIdentifier("OMIM:101600");
//        Disease result = instance.getDisease(omimDiseaseId);
//        assertEquals(omimDiseaseId, result.getDiseaseIdentifier());
//
//    }
//
//    /**
//     * Test of getDisease method, of class JdbcDiseaseDAOImpl.
//     */
//    @Test
//    public void testGetDiseaseByOmimDiseaseId() {
//        DiseaseIdentifier omimDiseaseId = new DiseaseIdentifier("OMIM:101600");
//
//        Disease result = instance.getDisease(omimDiseaseId);
//        assertEquals("PFEIFFER SYNDROME", result.getTerm());
//
//    }
// 
//    @Test
//    public void testGetDiseasePhenotypeTerms() {
//        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:101200");
//        List<PhenotypeTerm> result = instance.getDiseasePhenotypes(diseaseId);
//        PhenotypeTerm expectedTerm = new PhenotypeTerm();
//        expectedTerm.setId("HP:0000175");
//        expectedTerm.setTerm("Cleft palate");
//        assertTrue(result.contains(expectedTerm));
//    }
//    
//    @Test
//    public void testGetMouseModelPhenotypeTerms() {
//        String mouseModelId = "1";
//        List<PhenotypeTerm> result = instance.getMouseModelPhenotypes(mouseModelId);
//        PhenotypeTerm expectedTerm = new PhenotypeTerm();
//        expectedTerm.setId("MP:0000609");
//        expectedTerm.setTerm("abnormal liver physiology");
//        assertTrue(result.contains(expectedTerm));
//    }
//    
//    @Test
//    public void testGetDiseaseAssociationPhenotypeMatches() {
//        String diseaseId = "OMIM:101600";
//        Integer mouseModelId = 27680;
//        List<PhenotypeMatch> result = instance.getPhenotypeMatches(diseaseId, mouseModelId);
//        PhenotypeMatch expectedMatch = new PhenotypeMatch();
//        expectedMatch.setSimJ(0.853968);
//        expectedMatch.setIc(8.901661);
//        expectedMatch.setLcs("HP_0009702 Carpal synostosis ^ MP_0008915 fused carpal bones");
//        
//        PhenotypeTerm humanTerm = new PhenotypeTerm();
//        humanTerm.setId("HP:0005048");
//        humanTerm.setTerm("Synostosis of carpal bones");
//        expectedMatch.setHumanPhenotype(humanTerm);
//        
//        PhenotypeTerm mouseTerm = new PhenotypeTerm();
//        mouseTerm.setId("MP:0008915");
//        mouseTerm.setTerm("fused carpal bones");
//        expectedMatch.setMousePhenotype(mouseTerm);
//        
//        PhenotypeMatch matchResult = null;
//        for (PhenotypeMatch phenotypeMatch : result) {
//            if (phenotypeMatch.equals(expectedMatch)) {
//                matchResult = phenotypeMatch;
//            }
//        }
//        assertNotNull(matchResult);
//        assertEquals(expectedMatch.getLcs(), matchResult.getLcs());
//        //make sure there are other matches in there too!
//        assertTrue(result.size() > 1);
//    }
//    
}
