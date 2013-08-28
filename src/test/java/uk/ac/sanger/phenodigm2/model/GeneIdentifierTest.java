/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

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
    public void testGeneIdentifierEmptyConstructor() {
        System.out.println("testGeneIdentifierEmptyConstructor");
        GeneIdentifier instance = new GeneIdentifier();
        String expResult = null;
        String result = instance.getGeneSymbol();
        assertEquals(expResult, result);
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

}