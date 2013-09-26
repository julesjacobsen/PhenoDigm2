/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jj8
 */
public class DiseaseIdentifierTest {
    
    public DiseaseIdentifierTest() {
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

    @Test
    public void testCompareTo() {
        
        DiseaseIdentifier decypherDisease1 = new DiseaseIdentifier("DECYPHER:1234");
        DiseaseIdentifier omimDisease1 = new DiseaseIdentifier("OMIM:1234");
        DiseaseIdentifier omimDisease2 = new DiseaseIdentifier("OMIM:1235");
        DiseaseIdentifier orphanetDisease1 = new DiseaseIdentifier("ORPHANET:1234");
        
        List<DiseaseIdentifier> diseaseList  = new ArrayList<DiseaseIdentifier>();
        
        diseaseList.add(orphanetDisease1);
        diseaseList.add(omimDisease1);
        diseaseList.add(decypherDisease1);
        diseaseList.add(omimDisease2);
        
        List<DiseaseIdentifier> expectedDiseaseList  = new ArrayList<DiseaseIdentifier>();
        
        expectedDiseaseList.add(decypherDisease1);        
        expectedDiseaseList.add(omimDisease1);
        expectedDiseaseList.add(omimDisease2);
        expectedDiseaseList.add(orphanetDisease1);
        
                
        Collections.sort(diseaseList);
        
        for (DiseaseIdentifier diseaseIdentifier : expectedDiseaseList) {
            System.out.println(diseaseIdentifier);
        }
        
        assertTrue(diseaseList.equals(expectedDiseaseList));
    }
    
    @Test
    public void testEquals() {
        DiseaseIdentifier decypherDisease1 = new DiseaseIdentifier("DECYPHER:1234");
        DiseaseIdentifier decypherDisease2 = new DiseaseIdentifier("DECYPHER:1234");
        
        assertTrue(decypherDisease1.equals(decypherDisease2));
        
        Set<DiseaseIdentifier> diseaseSet = new HashSet<DiseaseIdentifier>();
        diseaseSet.add(decypherDisease1);
        diseaseSet.add(decypherDisease2);
        
        assertEquals(1, diseaseSet.size());
    }
    
}
