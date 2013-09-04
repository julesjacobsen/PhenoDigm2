/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseAssociation;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.model.MouseModel;
import uk.ac.sanger.phenodigm2.model.PhenotypeMatch;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;

/**
 * Disease data access manager interface.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public interface DiseaseDao {
    
    public Disease getDiseaseByDiseaseId(String diseaseId);
    
    public Set<Disease> getDiseasesByOmimGeneId(String omimGeneId);
    
    public Set<Disease> getDiseasesByMgiGeneId(String mgiGeneId);
    
    public Map<Disease, Set<DiseaseAssociation>> getKnownDiseaseAssociationsForMgiGeneId(String mgiGeneId);

    public Map<Disease, Set<DiseaseAssociation>> getPredictedDiseaseAssociationsForMgiGeneId(String mgiGeneId);
    
//    public List<MouseDiseaseModel> getMouseModelsByOmimId(String omimId);
//    
//    public List<PhenotypeMatch> getPhenotypeMatchesForMouseModelsByGeneSymbol(String geneSymbol);
//    

    public GeneIdentifier getGeneIdentifierForMgiGeneId(String acc);

    public GeneIdentifier getHumanOrthologIdentifierForMgiGeneId(String acc);
    
    public List<PhenotypeTerm> getDiseasePhenotypeTerms(String diseaseId);
    
    public List<PhenotypeTerm> getMouseModelPhenotypeTerms(String mouseModelId);
    
    public List<PhenotypeMatch> getPhenotypeMatches(String diseaseId, String mouseModelId);
}
