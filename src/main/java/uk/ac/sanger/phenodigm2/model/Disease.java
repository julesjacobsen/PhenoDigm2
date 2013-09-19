/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

import java.util.List;
import java.util.Objects;

/**
 * Disease bean representing a genetic disease.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class Disease implements Comparable<Disease>{
    
    private DiseaseIdentifier diseaseIdentifier;
    private String term;
    private List<String> alternativeTerms;
    //Human GeneIdentifiers come from the HGNC
    private List<GeneIdentifier> associatedHumanGenes;
    //Mouse GeneIdentifiers come from the MGI
    private List<GeneIdentifier> associatedMouseGenes;
    private List<String> locations;
    private List<PhenotypeTerm> phenotypeTerms;

    public Disease() {
    }
    
    /**
     * Convenience constructor - will create a new Disease with a new DiseaseIdentifier
     * being made from the provided diseaseId.
     * 
     * @param diseaseId 
     */
    public Disease(String diseaseId) {
        this.diseaseIdentifier = new DiseaseIdentifier(diseaseId);
    }
    
    public Disease(DiseaseIdentifier diseaseIdentifier) {
        this.diseaseIdentifier = diseaseIdentifier;
    }
    
    public String getDiseaseId() {
        return diseaseIdentifier.getCompoundIdentifier();
    }

    public DiseaseIdentifier getDiseaseIdentifier() {
        return diseaseIdentifier;
    }
    
    public void setDiseaseIdentifier(DiseaseIdentifier diseaseIdentifier) {
        this.diseaseIdentifier = diseaseIdentifier;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<String> getAlternativeTerms() {
        return alternativeTerms;
    }

    public void setAlternativeTerms(List<String> alternativeTerms) {
        this.alternativeTerms = alternativeTerms;
    }

    public List<GeneIdentifier> getAssociatedHumanGenes() {
        return associatedHumanGenes;
    }

    public void setAssociatedHumanGenes(List<GeneIdentifier> associatedHumanGenes) {
        this.associatedHumanGenes = associatedHumanGenes;
    }

    public List<GeneIdentifier> getAssociatedMouseGenes() {
        return associatedMouseGenes;
    }

    public void setAssociatedMouseGenes(List<GeneIdentifier> associatedMouseGenes) {
        this.associatedMouseGenes = associatedMouseGenes;
    }
    
    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public List<PhenotypeTerm> getPhenotypeTerms() {
        return phenotypeTerms;
    }

    public void setPhenotypeTerms(List<PhenotypeTerm> phenotypeTerms) {
        this.phenotypeTerms = phenotypeTerms;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.diseaseIdentifier);
        hash = 17 * hash + Objects.hashCode(this.term);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Disease other = (Disease) obj;
        if (!Objects.equals(this.diseaseIdentifier, other.diseaseIdentifier)) {
            return false;
        }
        if (!Objects.equals(this.term, other.term)) {
            return false;
        }
        return true;
    }
    
    @Override
    public int compareTo(Disease t) {
        return this.diseaseIdentifier.compareTo(t.diseaseIdentifier);
    }
    
    @Override
    public String toString() {
        return "Disease{" + diseaseIdentifier + " - " + term + ", alternativeTerms=" + alternativeTerms + ", associatedHumanGenes=" + associatedHumanGenes + ", associatedMouseGenes=" + associatedMouseGenes + ", locations=" + locations + '}';
    }    
   
}
