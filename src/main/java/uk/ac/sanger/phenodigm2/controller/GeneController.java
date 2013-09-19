/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.controller;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
        //Get all genes from the phenoDigmDao where we have a known disease association
        Set<GeneIdentifier> genesWithKnownDiseaseAssociations = new TreeSet<GeneIdentifier>();
        
        for (Disease disease : phenoDigmDao.getAllDiseses()) {
            genesWithKnownDiseaseAssociations.addAll(disease.getAssociatedMouseGenes());
        }
       
        model.addAttribute("geneIdentifiers", genesWithKnownDiseaseAssociations);
        return "genes";
    }
    
    
    @RequestMapping(value="/gene/{acc}", method=RequestMethod.GET)
    public String gene(@PathVariable("acc") String acc, Model model) {
        String mgiId = acc;
        System.out.println("GeneController: Making gene page for " + mgiId);
        model.addAttribute("mgiId", mgiId);
        
        GeneIdentifier geneIdentifier = phenoDigmDao.getGeneIdentifierForMgiGeneId(mgiId);
        if (geneIdentifier == null) {
            return "geneNotFound";
        }
        
        System.out.println("GeneController: Found GeneIdentifier: " + geneIdentifier);
        model.addAttribute("geneIdentifier", geneIdentifier);
        model.addAttribute("humanOrtholog", phenoDigmDao.getHumanOrthologIdentifierForMgiGeneId(mgiId));
        //Diseases 
        //known
        Map<Disease, Set<DiseaseAssociation>> knownDiseaseAssociations = phenoDigmDao.getKnownDiseaseAssociationsForMgiGeneId(mgiId);
        //INTERFACE TESTING ONLY!!! This should be an AJAX call.
        populateDiseasePhenotypeTerms(knownDiseaseAssociations);

        model.addAttribute("knownDiseaseAssociations", knownDiseaseAssociations);
        //predicted
        Map<Disease, Set<DiseaseAssociation>> predictedDiseaseAssociations = phenoDigmDao.getPredictedDiseaseAssociationsForMgiGeneId(mgiId);
        //INTERFACE TESTING ONLY!!! This should be an AJAX call.
        populateDiseasePhenotypeTerms(predictedDiseaseAssociations);
        
        model.addAttribute("predictedDiseaseAssociations", predictedDiseaseAssociations);
        
        return "geneTabulatedDiseaseView";
    }
     
    /**
     * Populates the PhenotypeTerms for all DiseaseAssociation in the given map  
     * @param diseaseAssociationsMap 
     */
    private void populateDiseasePhenotypeTerms(Map<Disease, Set<DiseaseAssociation>> diseaseAssociationsMap){
        
        for (Disease disease : diseaseAssociationsMap.keySet()) {
            if (disease.getPhenotypeTerms() == null) {
                disease.setPhenotypeTerms(phenoDigmDao.getDiseasePhenotypeTerms(disease.getDiseaseId()));                
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
