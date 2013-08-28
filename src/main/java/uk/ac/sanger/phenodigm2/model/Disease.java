/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

import java.util.List;
import java.util.Objects;

/**
 * Simple Disease bean representing an OMIM (http://omim.org) genetic disease.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class Disease implements Comparable<Disease>{
    
    private String omimId;
    private String fullOmimId;
    private String type;
    private String term;
    private List<String> alternativeTerms;
    private List<GeneIdentifier> associatedHumanGenes;
    private List<GeneIdentifier> associatedMouseGenes;
    private List<String> locations;

    public Disease() {
    }
    
    public String getOmimId() {
        return omimId;
    }

    public void setOmimId(String omimId) {
        this.omimId = omimId;
    }

    public String getFullOmimId() {
        return fullOmimId;
    }

    public void setFullOmimId(String fullOmimId) {
        this.fullOmimId = fullOmimId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.omimId);
        hash = 17 * hash + Objects.hashCode(this.fullOmimId);
        hash = 17 * hash + Objects.hashCode(this.type);
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
        if (!Objects.equals(this.omimId, other.omimId)) {
            return false;
        }
        if (!Objects.equals(this.fullOmimId, other.fullOmimId)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.term, other.term)) {
            return false;
        }
        return true;
    }
    
    @Override
    public int compareTo(Disease t) {
        return this.omimId.compareTo(t.omimId);
    }
    
    @Override
    public String toString() {
        return "Disease{" + omimId + " - " + term + ", fullOmimId=" + fullOmimId + ", type=" + type + ", alternativeTerms=" + alternativeTerms + ", associatedHumanGenes=" + associatedHumanGenes + ", associatedMouseGenes=" + associatedMouseGenes + ", locations=" + locations + '}';
    }    
   
}
