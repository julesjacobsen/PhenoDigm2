/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

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
public class CurationStatusTest {
    
    public CurationStatusTest() {
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
     * Test of constructor method, of class CurationStatus.
     */
    @Test
    public void testConstructor() {
        CurationStatus instance = new CurationStatus(true, false, true, false);
        CurationStatus expResult = new CurationStatus();
        expResult.setIsAssociatedInHuman(true);
        expResult.setHasMgiLiteratureEvidence(false);
        expResult.setHasMgiPhenotypeEvidence(true);
        expResult.setHasImpcPhenotypeEvidence(false);
        assertEquals(expResult, instance);
    }

    /**
     * Test of getIsAssociatedInHuman method, of class CurationStatus.
     */
    @Test
    public void testIsAssociatedInHuman() {
        CurationStatus instance = new CurationStatus();
        boolean expResult = false;
        boolean result = instance.getIsAssociatedInHuman();
        assertEquals(expResult, result);
    }

    /**
     * Test of setIsAssociatedInHuman method, of class CurationStatus.
     */
    @Test
    public void testSetIsAssociatedInHuman() {
        boolean isAssociatedInHuman = true;
        CurationStatus instance = new CurationStatus();
        instance.setIsAssociatedInHuman(isAssociatedInHuman);
        assertTrue(instance.getIsAssociatedInHuman());
    }

    /**
     * Test of hasMgiLiteratureEvidence method, of class CurationStatus.
     */
    @Test
    public void testHasMgiLiteratureEvidence() {
        CurationStatus instance = new CurationStatus();
        boolean expResult = false;
        boolean result = instance.getHasMgiLiteratureEvidence();
        assertEquals(expResult, result);
    }

    /**
     * Test of setHasMgiLiteratureEvidence method, of class CurationStatus.
     */
    @Test
    public void testSetHasMgiLiteratureEvidence() {
        boolean hasMgiLiteratureEvidence = true;
        CurationStatus instance = new CurationStatus();
        instance.setHasMgiLiteratureEvidence(hasMgiLiteratureEvidence);
        assertTrue(instance.getHasMgiLiteratureEvidence());
    }

    /**
     * Test of getHasMgiPhenotypeEvidence method, of class CurationStatus.
     */
    @Test
    public void testHasMgiPhenotypeEvidence() {
        CurationStatus instance = new CurationStatus();
        boolean expResult = false;
        boolean result = instance.getHasMgiPhenotypeEvidence();
        assertEquals(expResult, result);
    }

    /**
     * Test of setHasMgiPhenotypeEvidence method, of class CurationStatus.
     */
    @Test
    public void testSetHasMgiPhenotypeEvidence() {
        boolean hasMgiPhenotypeEvidence = true;
        CurationStatus instance = new CurationStatus();
        instance.setHasMgiPhenotypeEvidence(hasMgiPhenotypeEvidence);
        assertTrue(instance.getHasMgiPhenotypeEvidence());
    }

    /**
     * Test of getHasImpcPhenotypeEvidence method, of class CurationStatus.
     */
    @Test
    public void testHasImpcPhenotypeEvidence() {
        CurationStatus instance = new CurationStatus();
        boolean expResult = false;
        boolean result = instance.getHasImpcPhenotypeEvidence();
        assertEquals(expResult, result);
    }

    /**
     * Test of setHasImpcPhenotypeEvidence method, of class CurationStatus.
     */
    @Test
    public void testSetHasImpcPhenotypeEvidence() {
        boolean hasImpcPhenotypeEvidence = true;
        CurationStatus instance = new CurationStatus();
        instance.setHasImpcPhenotypeEvidence(hasImpcPhenotypeEvidence);
        assertTrue(instance.getHasImpcPhenotypeEvidence());
    }

    /**
     * Test of equals method, of class CurationStatus.
     */
    @Test
    public void testNotEquals() {
        System.out.println("equals");
        CurationStatus other = new CurationStatus();
        other.setHasImpcPhenotypeEvidence(true);
        CurationStatus instance = new CurationStatus();
        boolean expResult = false;
        boolean result = instance.equals(other);
        assertEquals(expResult, result);
    }

        /**
     * Test of equals method, of class CurationStatus.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        CurationStatus other = new CurationStatus();
        CurationStatus instance = new CurationStatus();
        boolean expResult = true;
        boolean result = instance.equals(other);
        assertEquals(expResult, result);
    }

    
    /**
     * Test of toString method, of class CurationStatus.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        CurationStatus instance = new CurationStatus();
        instance.setIsAssociatedInHuman(true);
        String expResult = "CurationStatus{isAssociatedInHuman=true, hasMgiLiteratureEvidence=false, hasMgiPhenotypeEvidence=false, hasImpcPhenotypeEvidence=false}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
}
