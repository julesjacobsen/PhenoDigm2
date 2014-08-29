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
package uk.ac.sanger.phenodigm2.dao;

import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static uk.ac.sanger.phenodigm2.dao.PhenoDigmWebDaoTest.logger;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseIdentifier;
import uk.ac.sanger.phenodigm2.web.GeneAssociationSummary;

/**
 *
 * @author jj8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:jdbc-test-context.xml"})
public class PhenoDigmWebDaoJdbcImplTest extends PhenoDigmWebDaoTest {
    
    public PhenoDigmWebDaoJdbcImplTest() {
        logger = LoggerFactory.getLogger(PhenoDigmWebDaoJdbcImplTest.class);
    }

    
    /**
     * Test of getDiseaseToGeneAssociationSummariesNoKnownAssociations method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetDiseaseToGeneAssociationSummariesNoKnownAssociations() {
//        logger.info("testGetDiseaseToGeneAssociationSummariesNoKnownAssociations");
        
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("DECIPHER:18");
        double cutoff = 0.0;
        
        Disease expDisease = new Disease(diseaseId);
        expDisease.setTerm("1P36 MICRODELETION SYNDROME");
        
        List<GeneAssociationSummary> result = instance.getDiseaseToGeneAssociationSummaries(diseaseId, cutoff);
        logger.info("{} has {} GeneAssociationSummary using cutoff of {}", diseaseId, result.size(), cutoff);

        assertFalse(result.isEmpty());
        for (GeneAssociationSummary geneAssociationSummary : result) {
            assertFalse(geneAssociationSummary.getAssociationSummary().isHasLiteratureEvidence());
        }
        
    }
}
