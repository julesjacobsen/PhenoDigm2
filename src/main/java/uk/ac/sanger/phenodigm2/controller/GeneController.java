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
import uk.ac.sanger.phenodigm2.dao.DiseaseDao;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseAssociation;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;

/**
 *
 * @author jj8
 */

@Controller
public class GeneController {

    @Autowired
    private DiseaseDao diseaseDao;
    
    @RequestMapping("/gene")
    public String allGenes(Model model) {
        //TODO: get all genes from the diseaseDao
        model.addAttribute("geneIdentifier", diseaseDao.getGeneIdentifierForMgiGeneId("MGI:95523"));
        return "genes";
    }
    
    
    @RequestMapping("/{acc}")
    public String gene(@PathVariable String acc, Model model) {
        String mgiId = acc;
        System.out.println("GeneController: Making gene page for " + mgiId);
        model.addAttribute("mgiId", mgiId);
        
        GeneIdentifier geneIdentifier = diseaseDao.getGeneIdentifierForMgiGeneId(mgiId);
        if (geneIdentifier == null) {
            return "geneNotFound";
        }
        
        System.out.println("GeneController: Found GeneIdentifier: " + geneIdentifier);
        model.addAttribute("geneIdentifier", geneIdentifier);
        model.addAttribute("humanOrtholog", diseaseDao.getHumanOrthologIdentifierForMgiGeneId(acc));
        //Diseases 
        //known
        Map<Disease, Set<DiseaseAssociation>> knownDiseaseAssociations = diseaseDao.getKnownDiseaseAssociationsForMgiGeneId(mgiId);
        
        model.addAttribute("knownDiseaseAssociations", knownDiseaseAssociations);
        //predicted
        Map<Disease, Set<DiseaseAssociation>> predictedDiseaseAssociations = diseaseDao.getPredictedDiseaseAssociationsForMgiGeneId(mgiId);
        model.addAttribute("predictedDiseaseAssociations", predictedDiseaseAssociations);
        
        return "test";
    }
    
}
