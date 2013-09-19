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
    public void testGetSetDiseaseIdentifier() {
        DiseaseIdentifier expected = new DiseaseIdentifier("OMIM:1234");
        DiseaseAssociation diseaseAssociation = new DiseaseAssociation();
        diseaseAssociation.setDiseaseIdentifier(expected);
        assertEquals(expected, diseaseAssociation.getDiseaseIdentifier());
    }
    
    @Test
    public void testCompareToDifferentValues() {
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
    
    @Test
    public void testCompareToSameDiseaseToModelScores() {
        DiseaseAssociation first = new DiseaseAssociation();
        first.setDiseaseToModelScore(90.12);       
        first.setDiseaseIdentifier(new DiseaseIdentifier("OMIM:1234"));
        MouseModel firstMouseModel = new MouseModel();
        firstMouseModel.setMgiModelId("MGI:1234");
        first.setMouseModel(firstMouseModel);
        
        DiseaseAssociation second = new DiseaseAssociation();
        second.setDiseaseToModelScore(90.12);
        second.setDiseaseIdentifier(new DiseaseIdentifier("OMIM:1234"));
        MouseModel secondMouseModel = new MouseModel();
        secondMouseModel.setMgiModelId("MGI:1235");
        second.setMouseModel(secondMouseModel);
        
        List<DiseaseAssociation> associationsList = new ArrayList<DiseaseAssociation>();
        associationsList.add(second);
        associationsList.add(first);
        
        Collections.sort(associationsList);
        
        DiseaseAssociation firstElement = associationsList.get(0);
        assertEquals(firstElement, first);
        
        
    }
        
    @Test
    public void testCompareToSameModelToDiseaseScores() {
        DiseaseAssociation first = new DiseaseAssociation();
        first.setModelToDiseaseScore(90.12);
        first.setDiseaseIdentifier(new DiseaseIdentifier("OMIM:1234"));
        MouseModel firstMouseModel = new MouseModel();
        firstMouseModel.setMgiModelId("MGI:1234");
        first.setMouseModel(firstMouseModel);
        
        DiseaseAssociation second = new DiseaseAssociation();
        second.setModelToDiseaseScore(90.12);
        second.setDiseaseIdentifier(new DiseaseIdentifier("OMIM:1234"));
        MouseModel secondMouseModel = new MouseModel();
        secondMouseModel.setMgiModelId("MGI:1235");
        second.setMouseModel(secondMouseModel);
        
        List<DiseaseAssociation> associationsList = new ArrayList<DiseaseAssociation>();
        associationsList.add(second);
        associationsList.add(first);
        
        Collections.sort(associationsList);
        
        DiseaseAssociation firstElement = associationsList.get(0);
        assertEquals(firstElement, first);
        
        
    }
}