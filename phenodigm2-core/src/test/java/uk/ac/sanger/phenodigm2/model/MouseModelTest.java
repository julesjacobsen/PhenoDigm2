/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
public class MouseModelTest {
    
    private MouseModel model1;
    private MouseModel model2;
    
    public MouseModelTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        model1 = new MouseModel();
        model1.setMgiGeneId("MGI:1234");
        model1.setMgiModelId(4321);
        model1.setSource("MGI");
        model1.setAllelicComposition("hoopy");
        model1.setGeneticBackground("frood");
        model1.setAlleleIds("MGI:7654321");
        
        model2 = new MouseModel();
        model2.setMgiGeneId("MGI:1233");
        model2.setMgiModelId(888);
        model2.setSource("MGP");
        model2.setAllelicComposition("sass");
        model2.setGeneticBackground("that");
        model2.setAlleleIds("MGI:7654321|MGI:1234567");
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of hashCode method, of class MouseModel.
     */
    @Test
    public void testHashCode() {
        Set<MouseModel> modelSet = new HashSet<MouseModel>();
        modelSet.add(model1);
        modelSet.add(model2);
        
        assertEquals(2, modelSet.size());
        
    }

    /**
     * Test of equals method, of class MouseModel.
     */
    @Test
    public void testEquals() {
        MouseModel other = model2;
        MouseModel instance = model1;
        boolean expResult = false;
        boolean result = instance.equals(other);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class MouseModel.
     */
    @Test
    public void testCompareTo() {
        MouseModel other = model2;
        MouseModel instance = model1;
        Set<MouseModel> modelSet = new TreeSet<MouseModel>();
        modelSet.add(other);
        modelSet.add(other);
        modelSet.add(instance);
        
        assertEquals(2, modelSet.size());
        
        List<MouseModel> sortedList = new ArrayList<MouseModel>();
        sortedList.addAll(modelSet);
        //models are sorted according to their modelId only
        assertEquals(model2, sortedList.get(0));
    }

    /**
     * Test of toString method, of class MouseModel.
     */
    @Test
    public void testGetSource() {
        MouseModel instance = model1;
        String expResult = "MGI";
        String result = instance.getSource();
        assertEquals(expResult, result);
    }
    /**
     * Test of toString method, of class MouseModel.
     */
    @Test
    public void testToString() {
        MouseModel instance = model1;
        String expResult = "MouseModel{MGI:1234_4321 (MGI) hoopy frood MGI:7654321}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
}
