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
package uk.ac.sanger.phenodigm2.model;

import java.util.List;

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
    
    private CurationStatus curationStatus;

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

    public CurationStatus getCurationStatus() {
        return curationStatus;
    }

    public void setCurationStatus(CurationStatus curationStatus) {
        this.curationStatus = curationStatus;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.diseaseIdentifier != null ? this.diseaseIdentifier.hashCode() : 0);
        hash = 53 * hash + (this.term != null ? this.term.hashCode() : 0);
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
        if (this.diseaseIdentifier != other.diseaseIdentifier && (this.diseaseIdentifier == null || !this.diseaseIdentifier.equals(other.diseaseIdentifier))) {
            return false;
        }
        if ((this.term == null) ? (other.term != null) : !this.term.equals(other.term)) {
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
