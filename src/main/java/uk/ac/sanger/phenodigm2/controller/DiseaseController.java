/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.controller;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
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
@RequestMapping(value = "/disease")
public class DiseaseController {
    
    private static final Logger logger = Logger.getLogger(DiseaseController.class);
    
    @Autowired
    PhenoDigmDao diseaseDao;

    @RequestMapping(method = RequestMethod.GET)
    public String allDiseases(Model model) {
        Set<Disease> allDiseases = diseaseDao.getAllDiseses();
        
        model.addAttribute("allDiseases", allDiseases);
        
        return "diseases";
    }

    @RequestMapping(value = "/{diseaseId}")
    public String disease(@PathVariable("diseaseId") String diseaseId, Model model) {
        
        logger.info("Getting gene-disease associations for disease: " + diseaseId);
        
        Disease disease = diseaseDao.getDiseaseByDiseaseId(diseaseId);
        model.addAttribute("disease", disease);
        logger.info(String.format("Found disease: %s %s", disease.getDiseaseId(), disease.getTerm()));
        
        disease.setPhenotypeTerms(diseaseDao.getDiseasePhenotypeTerms(diseaseId));
        
        //add existing curated disease associations
        Map<GeneIdentifier, Set<DiseaseAssociation>> orthologAssociations =  diseaseDao.getKnownDiseaseAssociationsForDiseaseId(diseaseId);
        populateGenePhenotypeTerms(orthologAssociations);
        logInfo("orthologous", disease, orthologAssociations);

        model.addAttribute("orthologAssociations", orthologAssociations);
        
        //also want to display the genes associated by phenotype similarity
        Map<GeneIdentifier, Set<DiseaseAssociation>> predictedAssociations = diseaseDao.getPredictedDiseaseAssociationsForDiseaseId(diseaseId);
        populateGenePhenotypeTerms(predictedAssociations);
        logInfo("predicted", disease, predictedAssociations);

        model.addAttribute("predictedAssociations", predictedAssociations);

        return "disease";
    }

    /**
     * Populates the PhenotypeTerms for all DiseaseAssociation in the given map  
     * @param geneAssociationsMap 
     */
    private void populateGenePhenotypeTerms(Map<GeneIdentifier, Set<DiseaseAssociation>> geneAssociationsMap){
        
        for (GeneIdentifier geneId : geneAssociationsMap.keySet()) {           
            Set<DiseaseAssociation> diseaseAssociations = geneAssociationsMap.get(geneId);
            for (DiseaseAssociation diseaseAssociation : diseaseAssociations) {
                MouseModel mouseModel = diseaseAssociation.getMouseModel();
                if (mouseModel.getPhenotypeTerms().isEmpty()) {
                    mouseModel.setPhenotypeTerms(diseaseDao.getMouseModelPhenotypeTerms(mouseModel.getMgiModelId()));
                }
            }
        }
    }
    
    private void logInfo(String associationsType, Disease disease, Map<GeneIdentifier, Set<DiseaseAssociation>> geneAssociations) {
        Set<GeneIdentifier> genesWithMultipleDiseaseModels = new TreeSet<GeneIdentifier>();
        
        logger.info(String.format("%s has %d %s gene associations:", disease.getDiseaseId(), geneAssociations.keySet().size(), associationsType));
        int num = 1;
        for (GeneIdentifier geneId : geneAssociations.keySet()) {
            Set<DiseaseAssociation> diseaseAssociations = geneAssociations.get(geneId);
            if (diseaseAssociations.size() > 1) {
                genesWithMultipleDiseaseModels.add(geneId);
            }
            for (DiseaseAssociation diseaseAssociation : diseaseAssociations) {
                logger.info(String.format("%d %s: %s", num++, geneId, diseaseAssociation));
            }
        }
        if (!genesWithMultipleDiseaseModels.isEmpty()) {
            logger.info(String.format("Note the following genes have more than one mouse model which matches the %s disease phenotype: %s", disease.getDiseaseId(), genesWithMultipleDiseaseModels));
        }
    }

}
