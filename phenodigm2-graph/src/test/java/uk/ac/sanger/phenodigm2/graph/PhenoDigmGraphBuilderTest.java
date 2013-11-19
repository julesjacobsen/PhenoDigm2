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
import uk.ac.sanger.phenodigm2.dao.PhenoDigmDao;
import uk.ac.sanger.phenodigm2.dao.PhenoDigmDaoJdbcImpl;

/**
 *
 * @author jj8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/jdbc-test-services.xml"})
public class PhenoDigmGraphBuilderTest {
    
    @Autowired
    PhenoDigmDao phenoDigmDao;// = new PhenoDigmDaoStubImpl();
    
//    private static final String TEST_DB_PATH = "target/phenodigm_graph_test.db";
    private static final String TEST_DB_PATH = "target/phenodigm.graphdb";
    
    public PhenoDigmGraphBuilderTest() {
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
        String dbPath = TEST_DB_PATH;
        PhenoDigmGraphBuilder instance = new PhenoDigmGraphBuilder();
        instance.buildGraphDatabase(phenoDigmDao, dbPath);
    }
  
    private void clearDb() {
        System.out.println("Clearing DB at " + TEST_DB_PATH);
        try {
            FileUtils.deleteRecursively(new File(TEST_DB_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
