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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;

/**
 * An in-memory cache for storing Disease entities, indexed by disease id 
 * and HGNC gene id.
 * 
 * There are a fixed number of about 7000 of these and they are referred to for 
 * every DiseaseAssociation request, hence the cache is used to store these in 
 * to avoid hundreds of trips to the database for every page request.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
class DiseaseCache {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final Map<String, Disease> diseaseIdToDiseaseMap;    
    private Map<String, Set<Disease>> hgncGeneIdToDiseaseMap;

    protected DiseaseCache(Map<String, Disease> diseaseIdToDiseaseMap, Map<String, Set<Disease>> hgncGeneIdToDiseasesMap) {
        this.diseaseIdToDiseaseMap = diseaseIdToDiseaseMap;
        this.hgncGeneIdToDiseaseMap = hgncGeneIdToDiseasesMap;
        init();
    }
    
    /**
     * Sets up the cache using the diseaseIdToDiseaseMap from the constructor.
     */
    private void init(){
//        hgncGeneIdToDiseaseMap = new HashMap<String, Set<Disease>>();
//        for (Disease disease : diseaseIdToDiseaseMap.values()) {
//            mapDiseaseToHgncGeneId(disease);
//        }
//        logger.info(String.format("DiseaseCache initialized. Mapped %d diseases to %d genes.", diseaseIdToDiseaseMap.size(), hgncGeneIdToDiseaseMap.keySet().size()));
    }
    
    /**
     * Maps a disease to an HGNC gene identifier. Actually this is populating the 
     * map of HGNC genes to the diseases which they are associated with. 
     * @param disease 
     */
    private void mapDiseaseToHgncGeneId(Disease disease) {
        
//        for (GeneIdentifier humanGeneIdentifier : disease.getAssociatedHumanGenes()) {
//        
//            String hgncGeneId = humanGeneIdentifier.getCompoundIdentifier();
//            if (!hgncGeneIdToDiseaseMap.containsKey(hgncGeneId)) {
//                Set<Disease> diseases = new TreeSet<Disease>();
//                diseases.add(disease);
//                hgncGeneIdToDiseaseMap.put(hgncGeneId, diseases);
//            }
//            else {
//                hgncGeneIdToDiseaseMap.get(hgncGeneId).add(disease);
//            }    
//        }
    }
    
    /**
     * Returns the Disease for a given disease id.
     * 
     * @param diseaseId
     * @return the Disease associated with the supplied disease id
     */
    protected Disease getDisease(String diseaseId) {
        return diseaseIdToDiseaseMap.get(diseaseId);
    }

    /**
     * Returns a Set of Diseases associated with the supplied HGNC gene id. Will
     * return an empty Set in cases where the cache doesn't contain the hgncGeneId.
     * Human genetic diseases are primarily mapped to their native gene symbol
     * - these are maintained by the HGNC <link>http://www.genenames.org/</link> 
     * 
     * @param hgncGeneId
     * @return Set of Diseases associated with the supplied HGNC gene id
     */
    protected Set<Disease> getDiseasesByHgncGeneId(String hgncGeneId) {
        Set<Disease> diseases = hgncGeneIdToDiseaseMap.get(hgncGeneId);
        if (diseases == null){ 
            logger.info(String.format("'%s' not mapped to any diseases", hgncGeneId));
            return new TreeSet<Disease>();
        }
        return diseases;
    }
    
    /**
     * Returns a Set of all Disease objects stored in the cache.
     * @return Set of all diseases in PhenoDigm.
     */
    protected Set<Disease> getAllDiseses() {
        Set<Disease> allDiseases = new TreeSet<Disease>();
        allDiseases.addAll(diseaseIdToDiseaseMap.values());
        return allDiseases;
    }
}
