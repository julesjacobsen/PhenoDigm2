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

package uk.ac.sanger.phenodigm2.web;

import uk.ac.sanger.phenodigm2.model.GeneIdentifier;

/**
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class GeneAssociationSummary {
    
    private GeneIdentifier humanGeneIdentifier;
    private GeneIdentifier mouseGeneIdentifier;
    private AssociationSummary associationSummary;

    public GeneAssociationSummary(GeneIdentifier humanGeneIdentifier, GeneIdentifier mouseGeneIdentifier, AssociationSummary associationSummary) {
        this.humanGeneIdentifier = humanGeneIdentifier;
        this.mouseGeneIdentifier = mouseGeneIdentifier;
        this.associationSummary = associationSummary;
    }

    public GeneIdentifier getHumanGeneIdentifier() {
        return humanGeneIdentifier;
    }

    public void setHumanGeneIdentifier(GeneIdentifier humanGeneIdentifier) {
        this.humanGeneIdentifier = humanGeneIdentifier;
    }

    public GeneIdentifier getMouseGeneIdentifier() {
        return mouseGeneIdentifier;
    }

    public void setMouseGeneIdentifier(GeneIdentifier mouseGeneIdentifier) {
        this.mouseGeneIdentifier = mouseGeneIdentifier;
    }

    public AssociationSummary getAssociationSummary() {
        return associationSummary;
    }

    public void setAssociationSummary(AssociationSummary associationSummary) {
        this.associationSummary = associationSummary;
    }

    
    @Override
    public String toString() {
        return String.format("GeneAssociationSummary{ %s %s %s}", humanGeneIdentifier, mouseGeneIdentifier, associationSummary);
    }
    
}
