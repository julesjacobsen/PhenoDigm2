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

        //add human gene identifiers
        GeneIdentifier FGFR1 = new GeneIdentifier("FGFR1", "OMIM:136350");
        GeneIdentifier FGFR2 = new GeneIdentifier("FGFR2", "OMIM:176943");
        apertSyndrome.setAssociatedHumanGenes(new ArrayList<GeneIdentifier>());
        apertSyndrome.getAssociatedHumanGenes().add(FGFR2);
        //add mouse gene identifiers

        GeneIdentifier Fgfr1 = new GeneIdentifier("Fgfr1", "MGI:95522");
        GeneIdentifier Fgfr2 = new GeneIdentifier("Fgfr2", "MGI:95523");
        apertSyndrome.setAssociatedMouseGenes(new ArrayList<GeneIdentifier>());
        apertSyndrome.getAssociatedMouseGenes().add(Fgfr2);

        diseaseMap.put("OMIM:101200", apertSyndrome);


        pfeifferSyndrome = new Disease("OMIM:101600");
        pfeifferSyndrome.setTerm("PFEIFFER SYNDROME");
        List<String> pfeifferAlternativeTerms = new ArrayList<String>();
        pfeifferAlternativeTerms.add("ACROCEPHALOSYNDACTYLY, TYPE V; ACS");
        pfeifferAlternativeTerms.add("ACS V");
        pfeifferAlternativeTerms.add("NOACK SYNDROME");
        pfeifferAlternativeTerms.add("CRANIOFACIAL-SKELETAL-DERMATOLOGIC DYSPLASIA, INCLUDED");
        pfeifferSyndrome.setAlternativeTerms(pfeifferAlternativeTerms);

        //add human gene identifiers
        pfeifferSyndrome.setAssociatedHumanGenes(new ArrayList<GeneIdentifier>());
        pfeifferSyndrome.getAssociatedHumanGenes().add(FGFR1);
        pfeifferSyndrome.getAssociatedHumanGenes().add(FGFR2);
        //add mouse gene identifiers

        pfeifferSyndrome.setAssociatedMouseGenes(new ArrayList<GeneIdentifier>());
        pfeifferSyndrome.getAssociatedMouseGenes().add(Fgfr1);
        pfeifferSyndrome.getAssociatedMouseGenes().add(Fgfr2);

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
    public void testGetDiseasesByOmimGeneId() {
        System.out.println("getDiseasesByOmimGeneId");
        String omimGeneId = "OMIM:136350";
        DiseaseCache instance = new DiseaseCache(diseaseMap);
        Set<Disease> expResult = new TreeSet<Disease>();
        expResult.add(pfeifferSyndrome);
        Set<Disease> result = instance.getDiseasesByHgncGeneId(omimGeneId);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDiseasesByHgncGeneId method, of class DiseaseCache.
     */
    @Test
    public void testGetDiseasesByOmimGeneIdTwoDiseasesForGene() {
        System.out.println("testGetDiseasesByOmimGeneIdTwoDiseasesForGene");
        String omimGeneId = "OMIM:176943";
        DiseaseCache instance = new DiseaseCache(diseaseMap);
        Set<Disease> expResult = new TreeSet<Disease>();
        expResult.add(apertSyndrome);
        expResult.add(pfeifferSyndrome);
        Set<Disease> result = instance.getDiseasesByHgncGeneId(omimGeneId);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getDiseasesByHgncGeneId method, of class DiseaseCache.
     */
    @Test
    public void testGetDiseasesByUnknownOmimGeneId() {
        System.out.println("testGetDiseasesByUnknownOmimGeneId");
        String omimGeneId = "OMIM:WIBBLE";
        DiseaseCache instance = new DiseaseCache(diseaseMap);
 
        Set<Disease> expResult = new TreeSet<Disease>();
        Set<Disease> result = instance.getDiseasesByHgncGeneId(omimGeneId);
        assertEquals(expResult, result);
    }
    
    @Test
    public void getAllDiseases() {
        DiseaseCache instance = new DiseaseCache(diseaseMap);
        
        assertEquals(2, instance.getAllDiseses().size());
    }
}