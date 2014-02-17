/*
 * Copyright © 2011-2013 EMBL - European Bioinformatics Institute
 * and Genome Research Limited
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.sanger.phenodigm2.dao;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.sanger.phenodigm2.model.MouseModel;

/**
 * Tests for class ExternalLinkFactory
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class ExternalLinkFactoryTest {
    
    //MGI models
    MouseModel mgiHomozygote;
    MouseModel mgiHeterozygote;
    //IMPC/MGP models
    MouseModel impcHomozygote;
    MouseModel impcHeterozygote;
    
    public ExternalLinkFactoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        mgiHomozygote = new MouseModel();
        mgiHomozygote.setMgiGeneId("MGI:1234");
        mgiHomozygote.setMgiModelId(4321);
        mgiHomozygote.setSource("MGI");
        mgiHomozygote.setAllelicComposition("Fgf9<tm1Dor>/Fgf9<tm1Dor>");
        mgiHomozygote.setGeneticBackground("involves: 129S6/SvEvTac * C57BL/6");
        mgiHomozygote.setAlleleIds("MGI:2135961");
        mgiHomozygote.setAllelicCompositionLink("<a href=\"http://informatics.jax.org/accession/MGI:2135961\">Fgf9<sup>tm1Dor</sup></a>/<a href=\"http://informatics.jax.org/accession/MGI:2135961\">Fgf9<sup>tm1Dor</sup></a>");
        
        mgiHeterozygote = new MouseModel();
        mgiHeterozygote.setMgiGeneId("MGI:1233");
        mgiHeterozygote.setMgiModelId(888);
        mgiHeterozygote.setSource("MGI");
        mgiHeterozygote.setAllelicComposition("Pitx2<tm1Jfm>/Pitx2<tm2Jfm>");
        mgiHeterozygote.setGeneticBackground("involves: 129S4/SvJaeSor * C57BL/6J");
        mgiHeterozygote.setAlleleIds("MGI:2136268|MGI:2136269");
        mgiHeterozygote.setAllelicCompositionLink("<a href=\"http://informatics.jax.org/accession/2136268\">Pitx2<sup>tm1Jfm</sup></a>/<a href=\"http://informatics.jax.org/accession/MGI:2136269\">Pitx2<sup>tm2Jfm</sup></a>");
    
        impcHomozygote = new MouseModel();
        impcHomozygote.setMgiGeneId("MGI:2148742");
        impcHomozygote.setMgiModelId(27887);
        impcHomozygote.setSource("MGP");
        impcHomozygote.setAllelicComposition("Cldn16<tm1a(KOMP)Wtsi>/Cldn16<tm1a(KOMP)Wtsi>");
        impcHomozygote.setGeneticBackground("MAPG");
        impcHomozygote.setAlleleIds(null);
        impcHomozygote.setAllelicCompositionLink("<a href=\"http://www.mousephenotype.org/data/genes/MGI:2148742\">Cldn16<sup>tm1a(KOMP)Wtsi</sup></a>/<a href=\"http://www.mousephenotype.org/data/genes/MGI:2148742\">Cldn16<sup>tm1a(KOMP)Wtsi</sup></a>");
        
    
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of formatAllelicCompositionHtml method, of class ExternalLinkFactory.
     */
    @Test
    public void testFormatAllelicCompositionHtmlSingleAllele() {
        System.out.println("formatAllelicCompositionHtmlSingleAllele");
        String expResult = "Pitx2<sup>tm1Jfm</sup>";
        String result = ExternalLinkFactory.formatAllelicCompositionHtml("Pitx2<tm1Jfm>");
        assertEquals(expResult, result);
    }
    
    /**
     * Test of formatAllelicCompositionHtml method, of class ExternalLinkFactory.
     */
    @Test
    public void testFormatAllelicCompositionHtmlTwoAllele() {
        System.out.println("formatAllelicCompositionHtmlTwoAllele");
        String expResult = "Pitx2<sup>tm1Jfm</sup>/Pitx2<sup>tm2Jfm</sup>";
        String result = ExternalLinkFactory.formatAllelicCompositionHtml("Pitx2<tm1Jfm>/Pitx2<tm2Jfm>");
        assertEquals(expResult, result);
    }
    
    /**
     * Test of buildLink method, of class ExternalLinkFactory.
     */
    @Test
    public void testBuildLinkMgiHomozygote() {
        System.out.println("buildLinkMgiHomozygote");
        String expResult = "<a href=\"http://informatics.jax.org/accession/MGI:2135961\">Fgf9<sup>tm1Dor</sup></a>/<a href=\"http://informatics.jax.org/accession/MGI:2135961\">Fgf9<sup>tm1Dor</sup></a>";
        String result = ExternalLinkFactory.buildLink(mgiHomozygote);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of buildLink method, of class ExternalLinkFactory.
     */
    @Test
    public void testBuildLinkMgiHeterozygote() {
        System.out.println("buildLinkMgiHeterozygote");
        String expResult = "<a href=\"http://informatics.jax.org/accession/MGI:2136268\">Pitx2<sup>tm1Jfm</sup></a>/<a href=\"http://informatics.jax.org/accession/MGI:2136269\">Pitx2<sup>tm2Jfm</sup></a>";
        String result = ExternalLinkFactory.buildLink(mgiHeterozygote);
        assertEquals(expResult, result);
    }
    
        
    /**
     * Test of buildLink method, of class ExternalLinkFactory.
     */
    @Test
    public void testBuildLinkImpcHomozygote() {
        System.out.println("buildLinkIMpcHomozygote");
        String expResult = "<a href=\"http://www.mousephenotype.org/data/genes/MGI:2148742\">Cldn16<sup>tm1a(KOMP)Wtsi</sup></a>/<a href=\"http://www.mousephenotype.org/data/genes/MGI:2148742\">Cldn16<sup>tm1a(KOMP)Wtsi</sup></a>";
        String result = ExternalLinkFactory.buildLink(impcHomozygote);
        assertEquals(expResult, result);
    }

}
