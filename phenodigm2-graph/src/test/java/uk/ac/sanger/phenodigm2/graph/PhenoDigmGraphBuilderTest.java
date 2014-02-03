/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.graph;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 *
 * @author jj8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/application-test-context.xml"})
public class PhenoDigmGraphBuilderTest {
    
    @Autowired
    PhenoDigmGraphBuilder instance;

    public PhenoDigmGraphBuilderTest() {
        instance = new PhenoDigmGraphBuilder();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
        
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
//        clearDb();
    }

    /**
     * Test of buildGraphDatabase method, of class PhenoDigmGraphBuilder.
     */
    @Test
    public void testBuildGraphDatabase() {
        System.out.println("buildGraphDatabase");
        instance.buildGraphDatabase();
    }
}
