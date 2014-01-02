/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;

/**
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class DiseaseCacheTest {

    private Map<String, Disease> diseaseMap;
    Disease apertSyndrome;
    Disease pfeifferSyndrome;

    public DiseaseCacheTest() {
    }

    @Before
    public void setUp() {
        diseaseMap = new HashMap<String, Disease>();

        apertSyndrome = new Disease("OMIM:101200");
        apertSyndrome.setTerm("APERT SYNDROME");
        List<String> apertAlternativeTerms = new ArrayList<String>();
        apertAlternativeTerms.add("ACROCEPHALOSYNDACTYLY, TYPE I; ACS");
        apertAlternativeTerms.add("ACS I");
        apertAlternativeTerms.add("APERT-CROUZON DISEASE, INCLUDED");
        apertAlternativeTerms.add("ACROCEPHALOSYNDACTYLY, TYPE II, INCLUDED");
        apertAlternativeTerms.add("ACS II, INCLUDED");
        apertAlternativeTerms.add("VOGT CEPHALODACTYLY, INCLUDED");
        apertSyndrome.setAlternativeTerms(apertAlternativeTerms);
        
        diseaseMap.put("OMIM:101200", apertSyndrome);

        pfeifferSyndrome = new Disease("OMIM:101600");
        pfeifferSyndrome.setTerm("PFEIFFER SYNDROME");
        List<String> pfeifferAlternativeTerms = new ArrayList<String>();
        pfeifferAlternativeTerms.add("ACROCEPHALOSYNDACTYLY, TYPE V; ACS");
        pfeifferAlternativeTerms.add("ACS V");
        pfeifferAlternativeTerms.add("NOACK SYNDROME");
        pfeifferAlternativeTerms.add("CRANIOFACIAL-SKELETAL-DERMATOLOGIC DYSPLASIA, INCLUDED");
        pfeifferSyndrome.setAlternativeTerms(pfeifferAlternativeTerms);

        diseaseMap.put("OMIM:101600", pfeifferSyndrome);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getDiseaseForDiseaseId method, of class DiseaseCache.
     */
    @Test
    public void testGetDiseaseForOmimDiseaseId() {
        System.out.println("getDiseaseForOmimDiseaseId");
        String omimDiseaseId = "OMIM:101200";
        DiseaseCache instance = new DiseaseCache(diseaseMap);
        Disease result = instance.getDiseaseForDiseaseId(omimDiseaseId);
        assertEquals("OMIM:101200", result.getDiseaseId());
    }

    @Test
    public void testGetDiseaseForUnknownOmimDiseaseId() {
        System.out.println("testGetDiseaseForUnknownOmimDiseaseId");
        String omimDiseaseId = "OMIM:WIBBLE";
        DiseaseCache instance = new DiseaseCache(diseaseMap);
        Disease result = instance.getDiseaseForDiseaseId(omimDiseaseId);;
        assertNull(result);
    }
        
    /**
     * Test of getDiseasesByHgncGeneId method, of class DiseaseCache.
     */
    @Test
    public void testGetDiseasesByHgncGeneId() {
        System.out.println("getDiseasesByHgncGeneId");
        String hgncGeneId = "OMIM:136350";
        DiseaseCache instance = new DiseaseCache(diseaseMap);
        Set<Disease> expResult = new TreeSet<Disease>();
        expResult.add(pfeifferSyndrome);
        Set<Disease> result = instance.getDiseasesByHgncGeneId(hgncGeneId);
        assertEquals(String.format("Expected list containing disease %s but got this instead: %s", pfeifferSyndrome.getDiseaseId(), result), expResult, result);
    }

    /**
     * Test of getDiseasesByHgncGeneId method, of class DiseaseCache.
     */
    @Test
    public void testGetDiseasesByHgncGeneIdTwoDiseasesForGene() {
        System.out.println("testGetDiseasesByHgncGeneIdTwoDiseasesForGene");
        String hgncGeneId = "OMIM:176943";
        DiseaseCache instance = new DiseaseCache(diseaseMap);
        Set<Disease> expResult = new TreeSet<Disease>();
        expResult.add(apertSyndrome);
        expResult.add(pfeifferSyndrome);
        Set<Disease> result = instance.getDiseasesByHgncGeneId(hgncGeneId);
        assertEquals(String.format("Expected list containing diseases %s and %s but got this instead: %s", apertSyndrome.getDiseaseId(), pfeifferSyndrome.getDiseaseId(), result), expResult, result);
    }
    
    /**
     * Test of getDiseasesByHgncGeneId method, of class DiseaseCache.
     */
    @Test
    public void testGetDiseasesByUnknownHgncGeneId() {
        System.out.println("testGetDiseasesByUnknownHgncGeneId");
        String hgncGeneId = "OMIM:WIBBLE";
        DiseaseCache instance = new DiseaseCache(diseaseMap);
 
        Set<Disease> expResult = new TreeSet<Disease>();
        Set<Disease> result = instance.getDiseasesByHgncGeneId(hgncGeneId);
        assertEquals(expResult, result);
    }
    
    @Test
    public void getAllDiseases() {
        DiseaseCache instance = new DiseaseCache(diseaseMap);
        
        assertEquals(2, instance.getAllDiseses().size());
    }
}