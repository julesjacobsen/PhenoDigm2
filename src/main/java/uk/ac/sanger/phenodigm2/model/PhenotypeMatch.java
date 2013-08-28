/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

import java.util.Objects;

/**
 * Contains information about how well a pair of <code>PhenotypeTerm</code> 
 * match each other.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class PhenotypeMatch {
    //information content
    private float ic;
    //Jaccard similarity score
    private double simJ;
    private PhenotypeTerm mousePhenotype;
    private PhenotypeTerm humanPhenotype;

    public PhenotypeMatch() {
    }

    public float getIc() {
        return ic;
    }

    public void setIc(float ic) {
        this.ic = ic;
    }

    public double getSimJ() {
        return simJ;
    }

    public void setSimJ(double simJ) {
        this.simJ = simJ;
    }

    public PhenotypeTerm getMousePhenotype() {
        return mousePhenotype;
    }

    public void setMousePhenotype(PhenotypeTerm mousePhenotype) {
        this.mousePhenotype = mousePhenotype;
    }

    public PhenotypeTerm getHumanPhenotype() {
        return humanPhenotype;
    }

    public void setHumanPhenotype(PhenotypeTerm humanPhenotype) {
        this.humanPhenotype = humanPhenotype;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Float.floatToIntBits(this.ic);
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.simJ) ^ (Double.doubleToLongBits(this.simJ) >>> 32));
        hash = 53 * hash + Objects.hashCode(this.mousePhenotype);
        hash = 53 * hash + Objects.hashCode(this.humanPhenotype);
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
        final PhenotypeMatch other = (PhenotypeMatch) obj;
        if (Float.floatToIntBits(this.ic) != Float.floatToIntBits(other.ic)) {
            return false;
        }
        if (Double.doubleToLongBits(this.simJ) != Double.doubleToLongBits(other.simJ)) {
            return false;
        }
        if (!Objects.equals(this.mousePhenotype, other.mousePhenotype)) {
            return false;
        }
        if (!Objects.equals(this.humanPhenotype, other.humanPhenotype)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PhenotypeMatch{" + "ic=" + ic + ", simJ=" + simJ + ", mousePhenotype=" + mousePhenotype.getTermId() + ", humanPhenotype=" + humanPhenotype.getTermId() + '}';
    }
    
    

}
