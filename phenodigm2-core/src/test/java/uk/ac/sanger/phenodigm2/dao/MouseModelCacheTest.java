/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.sanger.phenodigm2.model.MouseModel;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;

/**
 *
 * @author jj8
 */
public class MouseModelCacheTest {
    
    private Map<Integer, MouseModel> mouseModelMap;
    private MouseModel Fgfr2_8135;
    private MouseModel Fgfr2_8136;
//    private MouseModel Fgfr1_8136;
//    private MouseModel Fgfr1_8136;
    
    public MouseModelCacheTest() {
    }
    
    @Before
    public void setUp() {
        mouseModelMap = new HashMap<Integer, MouseModel>();
        Fgfr2_8135 = new MouseModel();
        Fgfr2_8135.setMgiGeneId("MGI:95523");
        Fgfr2_8135.setMgiModelId(8135);
        Fgfr2_8135.setAllelicComposition("Fgfr2<m1Sgg>/Fgfr2<+>");
        Fgfr2_8135.setAlleleIds(null);
        Fgfr2_8135.setGeneticBackground("involves: C3H/HeJ * C57BL/6J");
        Fgfr2_8135.setPhenotypeTerms(new ArrayList<PhenotypeTerm>());
        mouseModelMap.put(Fgfr2_8135.getMgiModelId(), Fgfr2_8135);

        
        Fgfr2_8136 = new MouseModel();
        Fgfr2_8136.setMgiGeneId("MGI:95523");
        Fgfr2_8136.setMgiModelId(8136);
        Fgfr2_8136.setAllelicComposition("Fgfr2<m1Sgg>/Fgfr2<m1Sgg>");
        Fgfr2_8136.setAlleleIds(null);
        Fgfr2_8136.setGeneticBackground("involves: C3H/HeJ * C57BL/6J");
        Fgfr2_8136.setPhenotypeTerms(new ArrayList<PhenotypeTerm>());
        mouseModelMap.put(Fgfr2_8136.getMgiModelId(), Fgfr2_8136);

    }

    /**
     * Test of getModel method, of class MouseModelCache.
     */
    @Test
    public void testGetModel() {
        System.out.println("getModel");
        Integer mouseModelId = 8135;
        MouseModelCache instance = new MouseModelCache(mouseModelMap);
        MouseModel expResult = Fgfr2_8135;
        MouseModel result = instance.getModel(mouseModelId);
        assertEquals(expResult, result);
    }

    /**
     * Test of getModelsByMgiGeneId method, of class MouseModelCache.
     */
    @Test
    public void testGetModelsByMgiGeneId() {
        System.out.println("getModelsByMgiGeneId");
        String mgiGeneId = "MGI:95523";
        MouseModelCache instance = new MouseModelCache(mouseModelMap);
        Set<MouseModel> expResult = new HashSet<>();
        expResult.add(Fgfr2_8136);
        expResult.add(Fgfr2_8135);
        Set result = instance.getModelsByMgiGeneId(mgiGeneId);
        assertEquals(expResult, result);
    }
}