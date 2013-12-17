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

import com.googlecode.flyway.core.Flyway;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseModelAssociation;
import uk.ac.sanger.phenodigm2.model.DiseaseIdentifier;
import uk.ac.sanger.phenodigm2.model.Gene;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.web.DiseaseAssociationSummary;
import uk.ac.sanger.phenodigm2.web.DiseaseGeneAssociationDetail;
import uk.ac.sanger.phenodigm2.web.GeneAssociationSummary;

/**
 *
 * @author jj8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:jdbc-test-services.xml"})
public class PhenoDigmWebDaoJdbcImplTest {
    
    Logger logger = LoggerFactory.getLogger(PhenoDigmWebDaoJdbcImplTest.class);

    @Autowired
    PhenoDigmWebDao instance;
    
    public PhenoDigmWebDaoJdbcImplTest() {
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
    }

    /**
     * Test of getDiseaseToGeneAssociationSummaries method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetDiseaseToGeneAssociationSummaries() {
        System.out.println("getDiseaseToGeneAssociationSummaries");
        
//        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:101400");
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:112600");
        Map<Disease, List<GeneAssociationSummary>> expResult = null;
        Map<Disease, List<GeneAssociationSummary>> result = instance.getDiseaseToGeneAssociationSummaries(diseaseId);
//        assertEquals(expResult, result);

    }
    
    /**
     * Test of getDiseaseToGeneAssociationSummaries method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetDiseaseToGeneAssociationSummariesNoAssociations() {
        System.out.println("getDiseaseToGeneAssociationSummaries");
        
//        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:101400");
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:243050");
        Map<Disease, List<GeneAssociationSummary>> expResult = new HashMap<>();
        Disease disease = new Disease(diseaseId);
        disease.setTerm("INDOLYLACROYL GLYCINURIA WITH MENTAL RETARDATION");
        expResult.put(disease, new ArrayList<GeneAssociationSummary>());
        Map<Disease, List<GeneAssociationSummary>> result = instance.getDiseaseToGeneAssociationSummaries(diseaseId);
        assertEquals(expResult, result);

    }

    /**
     * Test of getGeneToDiseaseAssociationSummaries method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetGeneToDiseaseAssociationSummaries() {
        System.out.println("getGeneToDiseaseAssociationSummaries");
        GeneIdentifier geneId = new GeneIdentifier("Apoe", "MGI:88057");
        Map<Gene, List<DiseaseAssociationSummary>> expResult = null;
        Map<Gene, List<DiseaseAssociationSummary>> result = instance.getGeneToDiseaseAssociationSummaries(geneId);
//        assertEquals(expResult, result);

    }

    /**
     * Test of getDiseaseGeneAssociationDetail method, of class PhenoDigmWebDaoJdbcImpl.
     */
    @Test
    public void testGetDiseaseGeneAssociationDetail() {
        System.out.println("getDiseaseGeneAssociationDetail");
        DiseaseIdentifier diseaseId = new DiseaseIdentifier("OMIM:144250");;
        GeneIdentifier geneId = new GeneIdentifier("MGI:96820", "MGI:96820");
        DiseaseGeneAssociationDetail expResult = null;
        DiseaseGeneAssociationDetail result = instance.getDiseaseGeneAssociationDetail(diseaseId, geneId);
//        assertEquals(expResult, result);
        System.out.println(result.getDiseaseId());
        for (DiseaseModelAssociation diseaseAssociation : result.getDiseaseAssociations()) {
            System.out.println(diseaseAssociation);
        }

    }
    
}
