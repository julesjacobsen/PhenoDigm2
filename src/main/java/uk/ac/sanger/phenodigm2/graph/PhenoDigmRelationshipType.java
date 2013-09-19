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
    IS_ASSOCIATED_WITH,
//    HAS_GENE_ASSOCIATION,
    IS_ASSOCIATED_WITH_BY_ORTHOLOGY,
//    HAS_ORTHOLOGOUS_GENE_ASSOCIATION,
    IS_ASSOCIATED_WITH_BY_PREDICTION,
//    HAS_PREDICTED_GENE_ASSOCIATION,
    //ortholog pairs
//    IS_MOUSE_ORTHOLOG_OF,
//    IS_HUMAN_ORTHOLOG_OF,
    IS_ORTHOLOG_OF,
    //gene-model associations
    IS_MOUSE_MODEL_OF,
//    HAS_MOUSE_MODEL,
    //disease Association between mouse and disease
    //PhenoDigm
    IS_PREDICTED_MODEL_OF,
//    HAS_PREDICTED_MODEL,
    //MGI
    IS_CURATED_MODEL_OF,
//    HAS_CURATED_MODEL,
    //HP / MP term associations
    IS_PHENOTYPE_OF,
//    HAS_PHENOTYPE,
    //match between MP and HP term
    PHENOTYPE_MATCH
    ;
}
