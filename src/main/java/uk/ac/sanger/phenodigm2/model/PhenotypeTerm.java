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
    private String definition;
    //comments on the definition.
    private String comment;
    
    //do we need an HPO/MP Enum too? I don't think so.
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

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
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
        return termId + " " + name;
    }
      
}
