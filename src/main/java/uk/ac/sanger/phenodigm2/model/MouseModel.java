/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

import java.util.List;

/**
 * Defines a mouse model for a genetic disease.
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class MouseModel {
    
    private String mgiGeneId;
    
    private String mgiModelId;
    //The commented out variables are all contained in the Allele, 
    //but I've go them here for a lighter-weight PhenoDigm-only representation. 
    
    private String allelicComposition;    
    private String geneticBackground;
    //link out to MGI - this is fiddly, so it's stored here for ease of reference
    private String allelicCompositionLink;
    
    private List<PhenotypeTerm> phenotypeTerms;

    public MouseModel() {
    }

    public String getMgiGeneId() {
        return mgiGeneId;
    }

    public void setMgiGeneId(String mgiGeneId) {
        this.mgiGeneId = mgiGeneId;
    }

    public String getMgiModelId() {
        return mgiModelId;
    }

    public void setMgiModelId(String mgiModelId) {
        this.mgiModelId = mgiModelId;
    }

    public String getAllelicComposition() {
        return allelicComposition;
    }

    public void setAllelicComposition(String allelicComposition) {
        this.allelicComposition = allelicComposition;
    }

    public String getGeneticBackground() {
        return geneticBackground;
    }

    public void setGeneticBackground(String geneticBackground) {
        this.geneticBackground = geneticBackground;
    }

    public String getAllelicCompositionLink() {
        return allelicCompositionLink;
    }

    public void setAllelicCompositionLink(String allelicCompositionLink) {
        this.allelicCompositionLink = allelicCompositionLink;
    }
    
    
    public List<PhenotypeTerm> getPhenotypeTerms() {
        return phenotypeTerms;
    }

    public void setPhenotypeTerms(List<PhenotypeTerm> phenotypeTerms) {
        this.phenotypeTerms = phenotypeTerms;
    }

    @Override
    public String toString() {
        return "MouseModel{" + "mgiGeneId=" + mgiGeneId + ", mgiModelId=" + mgiModelId + ", allelicComposition=" + allelicComposition + ", geneticBackground=" + geneticBackground + ", phenotypeTerms=" + phenotypeTerms + '}';
    }

}
