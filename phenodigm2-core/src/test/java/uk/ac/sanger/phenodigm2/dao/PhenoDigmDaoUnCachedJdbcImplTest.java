///*
//Copyright © 2011-2013 EMBL - European Bioinformatics Institute
//and Genome Research Limited
// 
//Licensed under the Apache License, Version 2.0 (the "License"); 
//you may not use this file except in compliance with the License.  
//You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//*/

package uk.ac.sanger.phenodigm2.dao;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.sanger.phenodigm2.config.JdbcTestConfig;

/**
 *
 * @author jj8
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JdbcTestConfig.class})
public class PhenoDigmDaoUnCachedJdbcImplTest {
    
    Logger logger = LoggerFactory.getLogger(PhenoDigmDaoUnCachedJdbcImplTest.class);

    @Test
    public void test() {
        assertTrue(true);
    }
}
