/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class DiseaseTest {
    
    /**
     * Test constructor, of class Disease.
     */
    @Test
    public void testConstructorDiseaseId() {
        String expResult = "OMIM:1234";       
        Disease instance = new Disease(expResult);
        instance.setDiseaseIdentifier(new DiseaseIdentifier(expResult));
        String result = instance.getDiseaseId();
        assertEquals(expResult, result);

    }
    
    /**
     * Test constructor, of class Disease.
     */
    @Test
    public void testConstructorDiseaseIdentifier() {
        String expResult = "OMIM:1234";     
        DiseaseIdentifier diseaseIdentifier = new DiseaseIdentifier(expResult);
        Disease instance = new Disease(diseaseIdentifier);
        
        String result = instance.getDiseaseId();
        assertEquals(expResult, result);

    }
    
    /**
     * Test of getDiseaseId method, of class Disease.
     */
    @Test
    public void testGetDiseaseId() {
        Disease instance = new Disease();
        String expResult = "OMIM:1234";       
        instance.setDiseaseIdentifier(new DiseaseIdentifier(expResult));
        String result = instance.getDiseaseId();
        assertEquals(expResult, result);

    }

    /**
     * Test of getDiseaseIdentifier method, of class Disease.
     */
    @Test
    public void testGetSetDiseaseIdentifier() {
        DiseaseIdentifier expResult = new DiseaseIdentifier("OMIM:1234");
        Disease instance = new Disease();
        instance.setDiseaseIdentifier(expResult);
        DiseaseIdentifier result = instance.getDiseaseIdentifier();
        assertEquals(expResult, result);

    }

    /**
     * Test of getTerm method, of class Disease.
     */
    @Test
    public void testGetSetTerm() {
        Disease instance = new Disease();
        String expResult = "Wibble";
        instance.setTerm(expResult);
        String result = instance.getTerm();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAlternativeTerms method, of class Disease.
     */
    @Test
    public void testGetSetAlternativeTerms() {
        Disease instance = new Disease();
        List<String> expResult = new ArrayList<String>();
        expResult.add("Frood");
        instance.setAlternativeTerms(expResult);
        List<String> result = instance.getAlternativeTerms();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLocus method, of class Disease.
     */
    @Test
    public void testGetSetLocus() {
        Disease instance = new Disease();
        String expResult = "location";
        instance.setLocus(expResult);
        String result = instance.getLocus();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPhenotypeTerms method, of class Disease.
     */
    @Test
    public void testGetSetPhenotypeTerms() {
        Disease instance = new Disease();
        List<PhenotypeTerm> expResult = Arrays.asList(new PhenotypeTerm("ID", "term"));
        instance.setPhenotypeTerms(expResult);
        List<PhenotypeTerm> result = instance.getPhenotypeTerms();
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class Disease.
     */
    @Test
    public void testHashCode() {
        Disease instance = new Disease(new DiseaseIdentifier("OMIM:4321"));
        Disease other = new Disease(new DiseaseIdentifier("OMIM:4321"));
        int expResult = other.hashCode();
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Disease.
     */
    @Test
    public void testEquals() {
        Disease other = new Disease(new DiseaseIdentifier("OMIM:4321"));
        Disease instance = new Disease(new DiseaseIdentifier("OMIM:1234"));
        boolean expResult = false;
        boolean result = instance.equals(other);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class Disease.
     */
    @Test
    public void testCompareTo() {
        Disease t = new Disease(new DiseaseIdentifier("OMIM:1234"));
        Disease instance = new Disease(new DiseaseIdentifier("OMIM:1234"));
        int expResult = 0;
        int result = instance.compareTo(t);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Disease.
     */
    @Test
    public void testToString() {
        Disease instance = new Disease(new DiseaseIdentifier("OMIM:1234"));
        instance.setTerm("WIBBLE");
        String expResult = "Disease{OMIM:1234 - WIBBLE, alternativeTerms=null, locus=null, classes=null}";
        String result = instance.toString();
        assertEquals(expResult, result);

    }
    
}
