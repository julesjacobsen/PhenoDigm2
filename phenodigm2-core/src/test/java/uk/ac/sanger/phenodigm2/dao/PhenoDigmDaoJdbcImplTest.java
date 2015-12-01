/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.sanger.phenodigm2.config.JdbcTestConfig;

/**
 *
 * Test for PhenoDigmDaoJdbcImpl implementation of PhenoDigmDao interface.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JdbcTestConfig.class})
public class PhenoDigmDaoJdbcImplTest extends PhenoDigmDaoTest {

    @Autowired
    private PhenoDigmDaoJdbcImpl instance;


    public PhenoDigmDaoJdbcImplTest() {
        logger = LoggerFactory.getLogger(PhenoDigmDaoJdbcImplTest.class);
    }

    @Before
    public void setUp() {
        instance.initCaches();
    }
}
