/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;

/**
 * An in-memory cache for storing Disease entities, indexed by OMIM disease id 
 * and OMIM gene id.
 * 
 * There are a fixed number of about 7000 of these and they are referred to for 
 * every DiseaseAssociation request, hence the cache is used to store these in 
 * to avoid hundreds of trips to the database for every page request.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
class DiseaseCache {
    
    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

        
    private Map<String, Disease> omimDiseaseIdToDiseaseMap;    
    private Map<String, Set<Disease>> omimGeneIdToDiseasesMap;

    protected DiseaseCache(Map<String, Disease> omimDiseaseIdToDiseaseMap) {
        this.omimDiseaseIdToDiseaseMap = omimDiseaseIdToDiseaseMap;
        init();
    }
    
    /**
     * Sets up the cache using the omimDiseaseIdToDiseaseMap from the constructor.
     */
    private void init(){
        omimGeneIdToDiseasesMap = new HashMap<String, Set<Disease>>();
        for (Disease disease : omimDiseaseIdToDiseaseMap.values()) {
            mapDiseaseToOmimGeneId(disease);
        }
        System.out.println(String.format("DiseaseCache initialized. Mapped %d diseases to %d genes.", omimDiseaseIdToDiseaseMap.size(), omimGeneIdToDiseasesMap.keySet().size()));
    }
    
    /**
     * Maps a disease to an OMIM gene identifier. Actually this is populating the 
     * map of OMIM genes to the diseases which they are associated with. 
     * @param disease 
     */
    private void mapDiseaseToOmimGeneId(Disease disease) {
        
        for (GeneIdentifier humanGeneIdentifier : disease.getAssociatedHumanGenes()) {
        
            String omimGeneId = humanGeneIdentifier.getCompoundIdentifier();
            if (!omimGeneIdToDiseasesMap.containsKey(omimGeneId)) {
                Set<Disease> diseases = new TreeSet<Disease>();
                diseases.add(disease);
                omimGeneIdToDiseasesMap.put(omimGeneId, diseases);
            }
            else {
                omimGeneIdToDiseasesMap.get(omimGeneId).add(disease);
            }    
        }
    }
    
    /**
     * Returns the Disease for a given OMIM disease id.
     * 
     * @param omimDiseaseId
     * @return the Disease associated with the supplied OMIM disease id
     */
    protected Disease getDiseaseForOmimDiseaseId(String omimDiseaseId) {
        return omimDiseaseIdToDiseaseMap.get(omimDiseaseId);
    }

    /**
     * Returns a Set of Diseases associated with the supplied OMIM gene id. Will
     * return an empty Set in cases where the cache doesn't contain the omimGeneId.
     * 
     * @param omimGeneId
     * @return Set of Diseases associated with the supplied OMIM gene id
     */
    protected Set<Disease> getDiseasesByOmimGeneId(String omimGeneId) {
        Set<Disease> diseases = omimGeneIdToDiseasesMap.get(omimGeneId);
        if (diseases == null){ 
            logger.info(omimGeneId + " not mapped to any diseases" );
            return new TreeSet<Disease>();
        }
        return diseases;
    }
    
    /**
     * Returns a Set of all Disease objects stored in the cache.
     * @return Set of all diseases in PhenoDigm.
     */
    protected Set<Disease> getAllDiseses() {
        Set<Disease> allDiseases = new TreeSet();
        allDiseases.addAll(omimDiseaseIdToDiseaseMap.values());
        return allDiseases;
    }
}
