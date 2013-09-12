/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;

/**
 * In memory-cache which stores all human - mouse ortholog mappings.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
class OrthologCache {
    
    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    
    //access is usually to get human orthologs of a mouse gene identifier via MGI id
    private static Map<GeneIdentifier, GeneIdentifier> mouseToHumanOrthologsMap;
    
    private static Map<String, GeneIdentifier> mgiIdToGeneIdentifierMap;
    
    /**
     * Requires a Map of MGI geneId to OMIM geneId. i.e. Mouse to Human gene identifiers.
     * @param mouseToHumanOrthologs 
     */
    protected OrthologCache(Map<GeneIdentifier, GeneIdentifier> mouseToHumanOrthologs){
        mouseToHumanOrthologsMap = mouseToHumanOrthologs;
        init();
    }
    
    private void init() {
        mgiIdToGeneIdentifierMap = new HashMap<String, GeneIdentifier>();
        
        for (GeneIdentifier mouseGeneId : mouseToHumanOrthologsMap.keySet()) {
            mgiIdToGeneIdentifierMap.put(mouseGeneId.getCompoundIdentifier(), mouseGeneId);
        }
        System.out.println(String.format("OrthologCache initialized. Mapped %d orthologs.", mouseToHumanOrthologsMap.keySet().size()));
    }      
    
    /**
     * Returns the GeneIdentifier for a given mgiGeneId e.g. "MGI:95522". This 
     * is the same as GeneIdentifier.getCompoundIdentifier(); 
     * @param mgiGeneId
     * @return 
     */
    protected GeneIdentifier getMouseGeneIdentifier(String mgiGeneId) {
        return mgiIdToGeneIdentifierMap.get(mgiGeneId);
    }
    
    /**
     * Returns the Human ortholog of a Mouse gene as identified by is MGI identifier.
     * e.g. "MGI:95522" will return FGFR2{OMIM:176943}
     * @param mgiGeneId
     * @return 
     */
    protected GeneIdentifier getHumanOrthologOfMouseGene(String mgiGeneId) {
        GeneIdentifier mouseGeneIdentifier = mgiIdToGeneIdentifierMap.get(mgiGeneId);
        
        return getHumanOrthologOfMouseGene(mouseGeneIdentifier);
    }
    
    protected GeneIdentifier getHumanOrthologOfMouseGene(GeneIdentifier mouseGeneIdentifier) {
        return mouseToHumanOrthologsMap.get(mouseGeneIdentifier);
    }

    protected Set<GeneIdentifier> getAllMouseGenes() {
        return mouseToHumanOrthologsMap.keySet();
    }
    
}
