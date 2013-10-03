/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.sanger.phenodigm2.model.Gene;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;

/**
 * In memory-cache which stores all human - mouse ortholog mappings.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
class OrthologCache {
    
    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    
    //access is usually to get human orthologs of a mouse gene identifier via MGI id
    private static Map<GeneIdentifier, Gene> mouseToHumanOrthologsMap;
    
    private static Map<String, GeneIdentifier> mgiIdToGeneIdentifierMap;
    
    /**
     * Requires a Map of MGI geneId to OMIM geneId. i.e. Mouse to Human gene identifiers.
     * @param mouseToHumanOrthologs 
     */
    protected OrthologCache(Map<GeneIdentifier, Gene> mouseToHumanOrthologs){
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
     * e.g. "MGI:95522" will return FGFR1{HGNC:3688}
     * @param mgiGeneId
     * @return 
     */
    protected GeneIdentifier getHumanOrthologOfMouseGene(String mgiGeneId) {
        GeneIdentifier mouseGeneIdentifier = mgiIdToGeneIdentifierMap.get(mgiGeneId);
        
        return getHumanOrthologOfMouseGene(mouseGeneIdentifier);
    }
    
    
    /**
     * 
     * @param mouseGeneIdentifier
     * @return human GeneIdentifier or null 
     */
    protected GeneIdentifier getHumanOrthologOfMouseGene(GeneIdentifier mouseGeneIdentifier) {
        
        Gene gene = mouseToHumanOrthologsMap.get(mouseGeneIdentifier); 

        if (gene == null) {
            return null;
        }
             
        return gene.getHumanGeneId();
    }

    protected Set<GeneIdentifier> getAllMouseGeneIdentifiers() {
        return mouseToHumanOrthologsMap.keySet();
    }
    
    protected Gene getGene(GeneIdentifier geneIdentifier) {
        return mouseToHumanOrthologsMap.get(geneIdentifier);
    }

    protected Collection<Gene> getGenes() {
        return mouseToHumanOrthologsMap.values();
    }
}