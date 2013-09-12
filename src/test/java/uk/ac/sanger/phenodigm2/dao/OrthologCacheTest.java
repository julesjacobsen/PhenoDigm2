/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;

/**
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class OrthologCacheTest {
    
    private Map<GeneIdentifier, GeneIdentifier> orthologMap;
    //Human gene identifiers
    private GeneIdentifier FGFR1;
    private GeneIdentifier FGFR2;
    private GeneIdentifier PHOSPHO1;
    private GeneIdentifier SCHIP1;
    
    //Mouse gene identifiers
    private GeneIdentifier Fgfr1;
    private GeneIdentifier Fgfr2;
    private GeneIdentifier Phospho1;
    private GeneIdentifier Schip1;
    
    public OrthologCacheTest() {
    }
    
    @Before
    public void setUp() {
        orthologMap = new HashMap<GeneIdentifier, GeneIdentifier>();

        //Human gene identifiers
        FGFR1 = new GeneIdentifier("FGFR1", "OMIM:136350");
        FGFR2 = new GeneIdentifier("FGFR2", "OMIM:176943");
        PHOSPHO1 = new GeneIdentifier("PHOSPHO1", "");
        SCHIP1 = new GeneIdentifier("SCHIP1", "");
        
        //Mouse gene identifiers
        Fgfr1 = new GeneIdentifier("Fgfr1", "MGI:95522");
        Fgfr2 = new GeneIdentifier("Fgfr2", "MGI:95523");
        Phospho1 = new GeneIdentifier("Phospho1", "MGI:2447348");
        Schip1 = new GeneIdentifier("Schip1","MGI:1353557");
        
        orthologMap.put(Fgfr1, FGFR1);
        orthologMap.put(Fgfr2, FGFR2);
        orthologMap.put(Phospho1, PHOSPHO1);
        orthologMap.put(Schip1, SCHIP1);
    }
        

    @Test
    public void testInitialization(){
        OrthologCache instance = new OrthologCache(orthologMap);
        assertNotNull(instance);
    }
    
    /**
     * Test of getMouseGeneIdentifier method, of class OrthologCache.
     */
    @Test
    public void testGetMouseGeneIdentifier() {
        System.out.println("getMouseGeneIdentifier");
        String mgiGeneId = "MGI:95522";
        OrthologCache instance = new OrthologCache(orthologMap);
        GeneIdentifier expResult = Fgfr1;
        GeneIdentifier result = instance.getMouseGeneIdentifier(mgiGeneId);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMouseGeneIdentifier method, of class OrthologCache.
     */
    @Test
    public void testGetMouseGeneIdentifierIncorrectGeneId() {
        System.out.println("testGetMouseGeneIdentifierIncorrectGeneId");
        String mgiGeneId = "MGI:FROOD";
        OrthologCache instance = new OrthologCache(orthologMap);
        GeneIdentifier result = instance.getMouseGeneIdentifier(mgiGeneId);
        assertNull(result);
    }
    
    /**
     * Test of getHumanOrthologOfMouseGene method, of class OrthologCache.
     */
    @Test
    public void testGetHumanOrthologOfMouseGene_String() {
        System.out.println("getHumanOrthologOfMouseGene");
        String mgiGeneId = Fgfr1.getCompoundIdentifier();
        OrthologCache instance = new OrthologCache(orthologMap);
        GeneIdentifier expResult = FGFR1;
        GeneIdentifier result = instance.getHumanOrthologOfMouseGene(mgiGeneId);
        assertEquals(expResult, result);

    }
    
    /**
     * Test of getHumanOrthologOfMouseGene method, of class OrthologCache.
     */
    @Test
    public void testGetHumanOrthologOfMouseGene_IncorrectString() {
        System.out.println("getHumanOrthologOfMouseGene");
        String mgiGeneId = "Fgfr1"; //this is a gene symbol, not an identifier
        OrthologCache instance = new OrthologCache(orthologMap);
        GeneIdentifier result = instance.getHumanOrthologOfMouseGene(mgiGeneId);
        assertNull(result);

    }

    /**
     * Test of getHumanOrthologOfMouseGene method, of class OrthologCache.
     */
    @Test
    public void testGetHumanOrthologOfMouseGene_IncorrectGeneIdentifier() {
        System.out.println("getHumanOrthologOfMouseGeneIncorrectTest");
        GeneIdentifier mouseGeneIdentifier = Fgfr2;
        OrthologCache instance = new OrthologCache(orthologMap);
        //this is NOT what we would expect to have returned.
        GeneIdentifier expResult = FGFR1; 
        GeneIdentifier result = instance.getHumanOrthologOfMouseGene(mouseGeneIdentifier);
        assertNotSame(expResult, result);
    }
    
    /**
     * Test of getHumanOrthologOfMouseGene method, of class OrthologCache.
     */
    @Test
    public void testGetHumanOrthologOfMouseGene_GeneIdentifier() {
        System.out.println("getHumanOrthologOfMouseGene");
        GeneIdentifier mouseGeneIdentifier = Fgfr2;
        OrthologCache instance = new OrthologCache(orthologMap);
        GeneIdentifier expResult = FGFR2;
        GeneIdentifier result = instance.getHumanOrthologOfMouseGene(mouseGeneIdentifier);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetAllMouseGenes() {
        OrthologCache instance = new OrthologCache(orthologMap);

        assertEquals(orthologMap.size(), instance.getAllMouseGenes().size());
    }
    
    /**
     * Test of getHumanOrthologOfMouseGene method, of class OrthologCache.
     */
    @Test
    public void testGetHumanOrthologOfMouseGene_NoOmimGeneIdentifier() {
        System.out.println("getHumanOrthologOfMouseGene_NoOmimGeneIdentifier");
        GeneIdentifier mouseGeneIdentifier = Phospho1;
        OrthologCache instance = new OrthologCache(orthologMap);
        GeneIdentifier expResult = PHOSPHO1;
        GeneIdentifier result = instance.getHumanOrthologOfMouseGene(mouseGeneIdentifier);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getHumanOrthologOfMouseGene method, of class OrthologCache.
     */
    @Test
    public void testGetHumanOrthologOfMouseGene_NoOmimGeneIdentifierTwo() {
        System.out.println("getHumanOrthologOfMouseGene_NoOmimGeneIdentifierTwo");
        GeneIdentifier mouseGeneIdentifier = Schip1;
        OrthologCache instance = new OrthologCache(orthologMap);
        GeneIdentifier expResult = SCHIP1;
        GeneIdentifier result = instance.getHumanOrthologOfMouseGene(mouseGeneIdentifier);
        assertEquals(expResult, result);
    }
    
}