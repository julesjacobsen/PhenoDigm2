/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.sanger.phenodigm2.graph.converter;

import org.neo4j.graphdb.Node;
import uk.ac.sanger.phenodigm2.model.Disease;

/**
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class DiseaseNodeConverter implements NodeConverter {

    @Override
    public Disease convert(Node node) {
        Disease disease = new Disease( (String) node.getProperty("diseaseId"));
        disease.setTerm( (String) node.getProperty("diseaseTerm"));
       
        return disease;
    }
    
}
