/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * Test for PhenoDigmDaoJdbcImpl implementation of PhenoDigmDao interface.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:jdbc-test-context.xml"})
public class PhenoDigmDaoJdbcImplTest extends PhenoDigmDaoTest {

    public PhenoDigmDaoJdbcImplTest() {
        logger = LoggerFactory.getLogger(PhenoDigmDaoJdbcImplTest.class);
    }
}
