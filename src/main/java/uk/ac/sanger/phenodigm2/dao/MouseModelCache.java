/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.sanger.phenodigm2.model.MouseModel;

/**
 * An in-memory cache for storing MouseModel data. Models are indexed by MGI 
 * model id and gene id. 
 * 
 * There are a fixed number of about 30200 of these and 
 * they are referred to for every DiseaseAssociation request, hence the cache is
 * used to store these in to avoid hundreds of trips to the database for every
 * page request.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
class MouseModelCache {
    
    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    private static Map<String, MouseModel> mouseModelMap;
    private static Map<String, Set<MouseModel>> mgiGeneIdToModelsMap;
    
    /**
     * 
     * @param mouseModels 
     */
    protected MouseModelCache(Map<String, MouseModel> mouseModels) {
        mouseModelMap = mouseModels;
        init();
    }

    private void init() {
        mgiGeneIdToModelsMap = new HashMap<String, Set<MouseModel>>();
        for (MouseModel mouseModel : mouseModelMap.values()) {
            String mgiGeneId = mouseModel.getMgiGeneId();
            if (mgiGeneIdToModelsMap.containsKey(mgiGeneId)) {
                mgiGeneIdToModelsMap.get(mgiGeneId).add(mouseModel);
            } else {
                Set<MouseModel> mouseModelSet = new HashSet<MouseModel>();
                mouseModelSet.add(mouseModel);
                mgiGeneIdToModelsMap.put(mgiGeneId, mouseModelSet);
            }
        }
        System.out.println(String.format("MouseModelCache initialized. Mapped %d models to %d genes.", mouseModelMap.size(), mgiGeneIdToModelsMap.keySet().size()));
    }
    
    protected MouseModel getModel(String mouseModelId) {
        return mouseModelMap.get(mouseModelId);
    }
    
    /**
     * Returns a Set of MouseModel for a given MGI gene id. If the gene id is 
     * not in the cache an empty Set will be returned. 
     * @param mgiGeneId
     * @return a Set of MouseModel
     */
    protected Set<MouseModel> getModelsByMgiGeneId(String mgiGeneId) {
        Set<MouseModel> results = null;
        results = mgiGeneIdToModelsMap.get(mgiGeneId);
        if (results == null) {
            logger.info(mgiGeneId + " not mapped to any mouse models" );
            results = new HashSet<MouseModel>();
        }
        return results;
    } 
}
