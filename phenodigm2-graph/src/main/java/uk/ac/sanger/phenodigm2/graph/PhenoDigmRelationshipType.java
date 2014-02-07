/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.graph;

import org.neo4j.graphdb.RelationshipType;

/**
 * Describes the possible relationships between nodes in the PhenoDigm graph database.
 * The relationships are declared in directional sets. For example:
 * Gene 'IS_ASSOCIATED_WITH' Disease
 * is the opposite of the 
 * Disease 'HAS_GENE_ASSOCIATION' Gene.
 * Similarly
 * Gene 'IS_ASSOCIATED_WITH_BY_ORTHOLOGY' Disease
 * is the opposite relationship of
 * Disease 'HAS_ORTHOLOGOUS_GENE_ASSOCIATION' Gene
 * 
 * @author Jules Jacobsen <jules.jacobsen@snager.ac.uk>
 */
public enum PhenoDigmRelationshipType implements RelationshipType {
    //gene-disease associations
    ASSOCIATED_WITH,
    ASSOCIATED_WITH_BY_ORTHOLOGY,
    ASSOCIATED_WITH_BY_PREDICTION,
    
//ortholog pairs
    ORTHOLOG_OF,
    //gene-model associations
    MOUSE_MODEL_OF,

    //disease Association between mouse model and disease
    //PhenoDigm
    PREDICTED_MODEL_OF,
    //MGI literature 
    CURATED_LITERATURE_MODEL_OF,

    //HP / MP term associations
    PHENOTYPE_OF,

    //match between MP and HP term
    PHENOTYPE_MATCH
    ;
}
