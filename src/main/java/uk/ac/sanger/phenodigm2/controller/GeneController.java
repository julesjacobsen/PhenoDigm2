/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.controller;

import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.ac.sanger.phenodigm2.dao.PhenoDigmDao;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseAssociation;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.model.MouseModel;

/**
 *
 * @author jj8
 */

@Controller
public class GeneController {

    @Autowired
    private PhenoDigmDao phenoDigmDao;
    
    @RequestMapping("/gene")
    public String allGenes(Model model) {
        //TODO: get all genes from the phenoDigmDao
        model.addAttribute("geneIdentifier", phenoDigmDao.getGeneIdentifierForMgiGeneId("MGI:95523"));
        return "genes";
    }
    
    
    @RequestMapping("/{acc}")
    public String gene(@PathVariable String acc, Model model) {
        String mgiId = acc;
        System.out.println("GeneController: Making gene page for " + mgiId);
        model.addAttribute("mgiId", mgiId);
        
        GeneIdentifier geneIdentifier = phenoDigmDao.getGeneIdentifierForMgiGeneId(mgiId);
        if (geneIdentifier == null) {
            return "geneNotFound";
        }
        
        System.out.println("GeneController: Found GeneIdentifier: " + geneIdentifier);
        model.addAttribute("geneIdentifier", geneIdentifier);
        model.addAttribute("humanOrtholog", phenoDigmDao.getHumanOrthologIdentifierForMgiGeneId(acc));
        //Diseases 
        //known
        Map<Disease, Set<DiseaseAssociation>> knownDiseaseAssociations = phenoDigmDao.getKnownDiseaseAssociationsForMgiGeneId(mgiId);
        //INTERFACE TESTING ONLY!!! This should be an AJAX call.
        populatePhenotypeTerms(knownDiseaseAssociations);

        model.addAttribute("knownDiseaseAssociations", knownDiseaseAssociations);
        //predicted
        Map<Disease, Set<DiseaseAssociation>> predictedDiseaseAssociations = phenoDigmDao.getPredictedDiseaseAssociationsForMgiGeneId(mgiId);
        //INTERFACE TESTING ONLY!!! This should be an AJAX call.
        populatePhenotypeTerms(predictedDiseaseAssociations);
        
        model.addAttribute("predictedDiseaseAssociations", predictedDiseaseAssociations);
        
        return "geneTabulatedDiseaseView";
    }
    
    /**
     * Populates the PhenotypeTerms for all  
     * @param diseaseAssociationsMap 
     */
    private void populatePhenotypeTerms(Map<Disease, Set<DiseaseAssociation>> diseaseAssociationsMap){
        
        for (Disease disease : diseaseAssociationsMap.keySet()) {
            if (disease.getPhenotypeTerms() == null) {
                disease.setPhenotypeTerms(phenoDigmDao.getDiseasePhenotypeTerms(disease.getOmimId()));                
            }
            Set<DiseaseAssociation> diseaseAssociations = diseaseAssociationsMap.get(disease);
            for (DiseaseAssociation diseaseAssociation : diseaseAssociations) {
                MouseModel mouseModel = diseaseAssociation.getMouseModel();
                if (mouseModel.getPhenotypeTerms().isEmpty()) {
                    mouseModel.setPhenotypeTerms(phenoDigmDao.getMouseModelPhenotypeTerms(mouseModel.getMgiModelId()));
                }
            }
        }
    }
    
}
