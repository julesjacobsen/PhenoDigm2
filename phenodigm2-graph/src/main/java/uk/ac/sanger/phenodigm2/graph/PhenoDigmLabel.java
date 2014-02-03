/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.sanger.phenodigm2.graph;

import org.neo4j.graphdb.Label;

/**
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public enum PhenoDigmLabel implements Label {
    Disease, Gene, MouseModel, HP, MP;
}
