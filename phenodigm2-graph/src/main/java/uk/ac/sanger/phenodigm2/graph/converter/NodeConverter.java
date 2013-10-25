/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.sanger.phenodigm2.graph.converter;

import org.neo4j.graphdb.Node;

/**
 * Defines behaviour for converters to convert Node objects into domain objects 
 * without using annotations.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 * @param <T>
 */
public interface NodeConverter<T> {
    
    public T convert(Node node);
    
}
