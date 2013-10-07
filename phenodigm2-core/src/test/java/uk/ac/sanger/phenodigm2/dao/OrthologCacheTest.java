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
import uk.ac.sanger.phenodigm2.model.Gene;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;

/**
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class OrthologCacheTest {
    
    private Map<GeneIdentifier, Gene> orthologMap;
    //Human gene identifiers
    private GeneIdentifier FGFR1;
    private GeneIdentifier FGFR2;
    private GeneIdentifier SELT;
    private GeneIdentifier SCHIP1;
    
    //Mouse gene identifiers
    private GeneIdentifier Fgfr1;
    private GeneIdentifier Fgfr2;
    private GeneIdentifier Selt1;
    private GeneIdentifier Schip1;
    
    public OrthologCacheTest() {
    }
    
    @Before
    public void setUp() {
        orthologMap = new HashMap<GeneIdentifier, Gene>();

        //Human gene identifiers
        FGFR1 = new GeneIdentifier("FGFR1", "HGNC:3688");
        FGFR2 = new GeneIdentifier("FGFR2", "HGNC:3689");
        SELT = new GeneIdentifier("SELT", "");
        SCHIP1 = null;
        
        //Mouse gene identifiers
        Fgfr1 = new GeneIdentifier("Fgfr1", "MGI:95522");
        Fgfr2 = new GeneIdentifier("Fgfr2", "MGI:95523");
        Selt1 = new GeneIdentifier("Phospho1", "MGI:2447348");
        Schip1 = new GeneIdentifier("Schip1","MGI:1353557");
        
        //Genes (well, ortholog pairs really)
        Gene fgfr1Gene = new Gene(Fgfr1, FGFR1);
        Gene fgfr2Gene = new Gene(Fgfr2, FGFR2);
        Gene selt1Ortholog = new Gene(Selt1, SELT);
        Gene schip1Ortholog = new Gene(Schip1, SCHIP1);
        
        
        orthologMap.put(Fgfr1, fgfr1Gene);
        orthologMap.put(Fgfr2, fgfr2Gene);
        orthologMap.put(Selt1, selt1Ortholog);
        orthologMap.put(Schip1, schip1Ortholog);
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
        GeneIdentifier mouseGeneIdentifier = Fgfr2;
        OrthologCache instance = new OrthologCache(orthologMap);
        GeneIdentifier expResult = FGFR2;
        GeneIdentifier result = instance.getHumanOrthologOfMouseGene(mouseGeneIdentifier);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetAllMouseGeneIdentifiers() {
        OrthologCache instance = new OrthologCache(orthologMap);

        assertEquals(orthologMap.size(), instance.getAllMouseGeneIdentifiers().size());
    }
    
    @Test
    public void testGetAllGenes() {
        OrthologCache instance = new OrthologCache(orthologMap);
        
        assertEquals(orthologMap.size(), instance.getGenes().size());
    }
    
    /**
     * Test of getHumanOrthologOfMouseGene method, of class OrthologCache.
     */
    @Test
    public void testGetHumanOrthologOfMouseGene_NoHgncIdentifier() {
        GeneIdentifier mouseGeneIdentifier = Selt1;
        OrthologCache instance = new OrthologCache(orthologMap);
        GeneIdentifier expResult = SELT;
        GeneIdentifier result = instance.getHumanOrthologOfMouseGene(mouseGeneIdentifier);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getHumanOrthologOfMouseGene method, of class OrthologCache.
     */
    @Test
    public void testGetHumanOrthologOfMouseGene_NoHgncIdentifierTwo() {
        GeneIdentifier mouseGeneIdentifier = Schip1;
        OrthologCache instance = new OrthologCache(orthologMap);
        GeneIdentifier expResult = SCHIP1;
        GeneIdentifier result = instance.getHumanOrthologOfMouseGene(mouseGeneIdentifier);
        assertEquals(expResult, result);
    }
 
    /**
     * Test of getGene method, of class OrthologCache.
     */
    @Test
    public void testGetGeneHasHumanOrtholog() {
        GeneIdentifier mouseGeneIdentifier = Fgfr1;
        OrthologCache instance = new OrthologCache(orthologMap);
        GeneIdentifier expResult = FGFR1;
        GeneIdentifier result = instance.getHumanOrthologOfMouseGene(mouseGeneIdentifier);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getGene method, of class OrthologCache.
     */
    @Test
    public void testGetGeneNoHumanOrtholog() {
        GeneIdentifier mouseGeneIdentifier = Schip1;
        OrthologCache instance = new OrthologCache(orthologMap);
        GeneIdentifier expResult = null;
        GeneIdentifier result = instance.getHumanOrthologOfMouseGene(mouseGeneIdentifier);
        assertEquals(expResult, result);
    }
}