/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

/**
 * Bean for storing the status of a gene with regards to whether the gene or disease 
 * has curated data from the disease resource, MGI or has predicted phenotype 
 * associations. 
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class CurationStatus {
    
    private boolean isAssociatedInHuman = false;
    private boolean hasMgiLiteratureEvidence = false;
    private boolean hasMgiPhenotypeEvidence = false;
    private boolean hasImpcPhenotypeEvidence = false;

    public CurationStatus() {
    }

    public CurationStatus(boolean isAssociatedInHuman, boolean hasMgiLiteratureEvidence, boolean hasMgiPhenotypeEvidence, boolean hasImpcPhenotypeEvidence) {
        this.isAssociatedInHuman = isAssociatedInHuman;
        this.hasMgiLiteratureEvidence = hasMgiLiteratureEvidence;
        this.hasMgiPhenotypeEvidence = hasMgiPhenotypeEvidence;
        this.hasImpcPhenotypeEvidence = hasImpcPhenotypeEvidence;
    }
    
    public boolean getIsAssociatedInHuman() {
        return isAssociatedInHuman;
    }

    public void setIsAssociatedInHuman(boolean isAssociatedInHuman) {
        this.isAssociatedInHuman = isAssociatedInHuman;
    }

    public boolean getHasMgiLiteratureEvidence() {
        return hasMgiLiteratureEvidence;
    }

    public void setHasMgiLiteratureEvidence(boolean hasMgiLiteratureEvidence) {
        this.hasMgiLiteratureEvidence = hasMgiLiteratureEvidence;
    }

    public boolean getHasMgiPhenotypeEvidence() {
        return hasMgiPhenotypeEvidence;
    }

    public void setHasMgiPhenotypeEvidence(boolean hasMgiPhenotypeEvidence) {
        this.hasMgiPhenotypeEvidence = hasMgiPhenotypeEvidence;
    }

    public boolean getHasImpcPhenotypeEvidence() {
        return hasImpcPhenotypeEvidence;
    }

    public void setHasImpcPhenotypeEvidence(boolean hasImpcPhenotypeEvidence) {
        this.hasImpcPhenotypeEvidence = hasImpcPhenotypeEvidence;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.isAssociatedInHuman ? 1 : 0);
        hash = 37 * hash + (this.hasMgiLiteratureEvidence ? 1 : 0);
        hash = 37 * hash + (this.hasMgiPhenotypeEvidence ? 1 : 0);
        hash = 37 * hash + (this.hasImpcPhenotypeEvidence ? 1 : 0);
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
        final CurationStatus other = (CurationStatus) obj;
        if (this.isAssociatedInHuman != other.isAssociatedInHuman) {
            return false;
        }
        if (this.hasMgiLiteratureEvidence != other.hasMgiLiteratureEvidence) {
            return false;
        }
        if (this.hasMgiPhenotypeEvidence != other.hasMgiPhenotypeEvidence) {
            return false;
        }
        if (this.hasImpcPhenotypeEvidence != other.hasImpcPhenotypeEvidence) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CurationStatus{" + "isAssociatedInHuman=" + isAssociatedInHuman + ", hasMgiLiteratureEvidence=" + hasMgiLiteratureEvidence + ", hasMgiPhenotypeEvidence=" + hasMgiPhenotypeEvidence + ", hasImpcPhenotypeEvidence=" + hasImpcPhenotypeEvidence + '}';
    }
 
}
