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
import java.util.Map;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseIdentifier;
import uk.ac.sanger.phenodigm2.model.Gene;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.web.DiseaseAssociationSummary;
import uk.ac.sanger.phenodigm2.web.DiseaseGeneAssociationDetail;
import uk.ac.sanger.phenodigm2.web.GeneAssociationSummary;

/**
 * Data access for the web views 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public interface PhenoDigmWebDao {
    
    /**
     * The disease page view.
     * @param diseaseId
     * @return 
     */
    public Map<Disease, List<GeneAssociationSummary>> getDiseaseToGeneAssociationSummaries(DiseaseIdentifier diseaseId);

    /**
     * The gene page view.
     * @param geneId
     * @return 
     */
    public Map<Gene, List<DiseaseAssociationSummary>> getGeneToDiseaseAssociationSummaries(GeneIdentifier geneId);
    
    /**
     * Returns details of the Disease-Gene association.
     * @param diseaseId
     * @param geneId
     * @return 
     */
    public DiseaseGeneAssociationDetail getDiseaseGeneAssociationDetail(DiseaseIdentifier diseaseId, GeneIdentifier geneId);

    /**
     * Returns a Disease object for a given DiseaseIdentifier.
     * @param diseaseId
     * @return
     */
    public Disease getDisease(DiseaseIdentifier diseaseId);
    
}
