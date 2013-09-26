/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

import java.util.List;


/**
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class DiseaseAssociation implements Comparable<DiseaseAssociation> {
    
    private DiseaseIdentifier diseaseIdentifier;
    //need to add gene identifier here? 
    private MouseModel mouseModel;
    private double modelToDiseaseScore;
    private double diseaseToModelScore;
    private List<PhenotypeMatch> phenotypeMatches;
    private String pubMedId;
    
    public DiseaseAssociation() {
    }

    public DiseaseIdentifier getDiseaseIdentifier() {
        return diseaseIdentifier;
    }

    public void setDiseaseIdentifier(DiseaseIdentifier diseaseIdentifier) {
        this.diseaseIdentifier = diseaseIdentifier;
    }

    public MouseModel getMouseModel() {
        return mouseModel;
    }

    public void setMouseModel(MouseModel mouseModel) {
        this.mouseModel = mouseModel;
    }

    public double getModelToDiseaseScore() {
        return modelToDiseaseScore;
    }

    public void setModelToDiseaseScore(double modelToDiseaseScore) {
        this.modelToDiseaseScore = modelToDiseaseScore;
    }

    public double getDiseaseToModelScore() {
        return diseaseToModelScore;
    }

    public void setDiseaseToModelScore(double diseaseToModelScore) {
        this.diseaseToModelScore = diseaseToModelScore;
    }

    public List<PhenotypeMatch> getPhenotypeMatches() {
        return phenotypeMatches;
    }

    public void setPhenotypeMatches(List<PhenotypeMatch> phenotypeMatches) {
        this.phenotypeMatches = phenotypeMatches;
    }

    public String getPubMedId() {
        return pubMedId;
    }

    public void setPubMedId(String pubMedId) {
        this.pubMedId = pubMedId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.diseaseIdentifier != null ? this.diseaseIdentifier.hashCode() : 0);
        hash = 97 * hash + (this.mouseModel != null ? this.mouseModel.hashCode() : 0);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.modelToDiseaseScore) ^ (Double.doubleToLongBits(this.modelToDiseaseScore) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.diseaseToModelScore) ^ (Double.doubleToLongBits(this.diseaseToModelScore) >>> 32));
        hash = 97 * hash + (this.pubMedId != null ? this.pubMedId.hashCode() : 0);
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
        final DiseaseAssociation other = (DiseaseAssociation) obj;
        if ((this.diseaseIdentifier == null) ? (other.diseaseIdentifier != null) : !this.diseaseIdentifier.equals(other.diseaseIdentifier)) {
            return false;
        }
        if (this.mouseModel != other.mouseModel && (this.mouseModel == null || !this.mouseModel.equals(other.mouseModel))) {
            return false;
        }
        if (Double.doubleToLongBits(this.modelToDiseaseScore) != Double.doubleToLongBits(other.modelToDiseaseScore)) {
            return false;
        }
        if (Double.doubleToLongBits(this.diseaseToModelScore) != Double.doubleToLongBits(other.diseaseToModelScore)) {
            return false;
        }
        if ((this.pubMedId == null) ? (other.pubMedId != null) : !this.pubMedId.equals(other.pubMedId)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "DiseaseAssociation{" + diseaseIdentifier + " " + mouseModel.getMgiGeneId() + "_" + mouseModel.getMgiModelId() + " PubMed: " + pubMedId + " Scores: [m2d=" + modelToDiseaseScore + ", d2M=" + diseaseToModelScore + "] " + phenotypeMatches + '}';
    }

    
    //need to specify comparator for different sorting criteria 
    @Override
    public int compareTo(DiseaseAssociation that) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
    
        if (this == that) {
            return EQUAL;
        }
        if (this.diseaseToModelScore == that.diseaseToModelScore) {
            if (! this.diseaseIdentifier.equals(that.diseaseIdentifier)) {
                return this.diseaseIdentifier.compareTo(that.diseaseIdentifier);                
            } else {
                return this.mouseModel.getMgiModelId().compareTo(that.mouseModel.getMgiModelId());
            }
        }
        if (this.diseaseToModelScore > that.diseaseToModelScore) {
            return BEFORE;
        }
        if (this.diseaseToModelScore < that.diseaseToModelScore) {
            return AFTER;
        }
        
        return AFTER;
    }
    
    
}
