/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class GeneIdentifierTest {
    
    public GeneIdentifierTest() {
    }

    /**
     * Test of constructor for class GeneIdentifier.
     */
    @Test
    public void testGeneIdentifierAllFieldsOverloadedConstructor() {
        System.out.println("testGeneIdentifierAllFieldsOverloadedConstructor");
        String geneSymbol = "Fgfr2";
        String databaseIdentifier = "MGI";
        String databaseAccession = "95523";
        GeneIdentifier instance = new GeneIdentifier(geneSymbol, databaseIdentifier, databaseAccession);
        String expResult = "Fgfr2{MGI:95523}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of constructor for class GeneIdentifier.
     */
    @Test
    public void testGeneIdentifierParserOverloadedConstructor() {
        System.out.println("testGeneIdentifierParserOverloadedConstructor");
        String geneSymbol = "Fgfr2";
        String databaseCompoundIdentifier = "MGI:95523";
        GeneIdentifier instance = new GeneIdentifier(geneSymbol, databaseCompoundIdentifier);
        String expResult = "Fgfr2{MGI:95523}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    @Test
    public void testCompareTo(){

        GeneIdentifier gene1 = new GeneIdentifier("Fgfr1", "MGI:95524");
        GeneIdentifier gene2 = new GeneIdentifier("Fgfr2", "MGI:95523");
        
        List<GeneIdentifier> genesList = new ArrayList<GeneIdentifier>();
        
        genesList.add(gene2);
        genesList.add(gene1);
        
        Collections.sort(genesList);
        
        assertEquals(gene1, genesList.get(0));
    }
}