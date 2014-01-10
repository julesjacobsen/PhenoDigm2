/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseIdentifier;
import uk.ac.sanger.phenodigm2.model.DiseaseModelAssociation;
import uk.ac.sanger.phenodigm2.model.Gene;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.model.MouseModel;
import uk.ac.sanger.phenodigm2.model.PhenotypeMatch;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;
import uk.ac.sanger.phenodigm2.web.DiseaseAssociationSummary;
import uk.ac.sanger.phenodigm2.web.DiseaseGeneAssociationDetail;
import uk.ac.sanger.phenodigm2.web.GeneAssociationSummary;

/**
 * Disease data access manager interface.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public interface PhenoDigmDao {
    
    public Set<Disease> getAllDiseses();

    public Disease getDisease(DiseaseIdentifier diseaseId);

    public List<PhenotypeTerm> getDiseasePhenotypeTerms(DiseaseIdentifier diseaseId);

    public Set<Gene> getAllGenes();
    
    public Gene getGene(GeneIdentifier geneIdentifier);
        
    public Set<MouseModel> getAllMouseModels();
    
    public List<PhenotypeTerm> getMouseModelPhenotypeTerms(String mouseModelId);
    
    public List<PhenotypeMatch> getPhenotypeMatches(String diseaseId, String mouseModelId);

    public Map<Disease, List<GeneAssociationSummary>> getDiseaseToGeneAssociationSummaries(DiseaseIdentifier diseaseId, double minScoreCutoff);

    public Map<Gene, List<DiseaseAssociationSummary>> getGeneToDiseaseAssociationSummaries(GeneIdentifier geneId, double minScoreCutoff);

    public DiseaseGeneAssociationDetail getDiseaseGeneAssociationDetail(DiseaseIdentifier diseaseId, GeneIdentifier geneId);

}
