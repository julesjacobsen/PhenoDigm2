/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.model;

import java.util.Objects;

/**
 * Represents a phenotype term from a phenotype ontology - either the HPO or the MPO 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class PhenotypeTerm {
    
    private String termId;
    private String name;
    //bit of overkill so for sanity issues 
    //private String description;

    //do we really need an HPO/MPO Enum too? I don't think so.
    public PhenotypeTerm() {
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getDescription() {
//        return description;
//    }

//    public void setDescription(String description) {
//        this.description = description;
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.termId);
        hash = 73 * hash + Objects.hashCode(this.name);
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
        final PhenotypeTerm other = (PhenotypeTerm) obj;
        if (!Objects.equals(this.termId, other.termId)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PhenotypeTerm{" + termId + ", " + name + '}';
    }
      
}
