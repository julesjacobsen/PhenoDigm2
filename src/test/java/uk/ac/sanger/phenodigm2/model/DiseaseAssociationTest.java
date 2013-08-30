/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jj8
 */
public class DiseaseAssociationTest {
    
    public DiseaseAssociationTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @Test
    public void testCompareTo() {
        DiseaseAssociation first = new DiseaseAssociation();
        first.setDiseaseToModelScore(90.12);
        DiseaseAssociation second = new DiseaseAssociation();
        second.setDiseaseToModelScore(89.99);
        List<DiseaseAssociation> associationsList = new ArrayList<DiseaseAssociation>();
        associationsList.add(second);
        associationsList.add(first);
        
        Collections.sort(associationsList);
        
        DiseaseAssociation firstElement = associationsList.get(0);
        assertEquals(firstElement, first);
        
        
    }
}