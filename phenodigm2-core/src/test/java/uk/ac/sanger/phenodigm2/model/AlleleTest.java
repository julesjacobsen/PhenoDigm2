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
package uk.ac.sanger.phenodigm2.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class AlleleTest {
    
    private Allele instance;
    
    private final String source = "SOURCE";
    private final String geneId = "Gene1";
    private final String alleleSymbol = "Aa/Bb";
    private final String alleleId = "12345";

    
    @Before
    public void setUp() {
        instance = new Allele();
    }
    
    @Test
    public void testGetSetSource() {
        instance.setSource(source);
        assertEquals(source, instance.getSource());
    }

    
    @Test
    public void testGetSetGeneId() {
        instance.setGeneId(geneId);
        assertEquals(geneId, instance.getGeneId());
    }

    @Test
    public void testGetSetAlleleSymbol() {
        instance.setAlleleSymbol(alleleSymbol);
        assertEquals(alleleSymbol, instance.getAlleleSymbol());
    }

    @Test
    public void testGetSetAlleleId() {
        instance.setAlleleId(alleleId);
        assertEquals(alleleId, instance.getAlleleId());
    }

    @Test
    public void testHashCode() {
    }

    @Test
    public void testEquals() {
    }

    @Test
    public void testToString() {

        instance.setSource(source);
        instance.setGeneId(geneId);
        instance.setAlleleSymbol(alleleSymbol);
        instance.setAlleleId(alleleId);

        String expected = "Allele{source=" + source + ", geneId=" + geneId + ", alleleId=" + alleleId + ", alleleSymbol=" + alleleSymbol + '}';
        assertEquals(expected, instance.toString());
    }
    
}
