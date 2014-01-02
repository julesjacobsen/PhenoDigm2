/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.controller;

import uk.ac.sanger.phenodigm2.web.GeneAssociationSummary;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.ac.sanger.phenodigm2.dao.PhenoDigmWebDao;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseIdentifier;
import uk.ac.sanger.phenodigm2.web.AssociationSummary;

/**
 *
 * @author jj8
 */
@Controller
public class DiseaseController {

    private static final Logger logger = LoggerFactory.getLogger(DiseaseController.class);

    @Autowired
    private PhenoDigmWebDao phenoDigmDao;

    @RequestMapping(value = "/disease")
    public String allDiseases(Model model) {
        Set<Disease> allDiseases = new TreeSet<Disease>();//phenoDigmDao.getAllDiseses();

        model.addAttribute("allDiseases", allDiseases);

        return "diseases";
    }

    @RequestMapping(value = "/disease/{diseaseId}")
    public String disease(@PathVariable("diseaseId") String diseaseId, Model model) {

        logger.info("Getting gene-disease associations for disease: " + diseaseId);
        Map<Disease, List<GeneAssociationSummary>> diseaseToGeneAssociationsMap = phenoDigmDao.getDiseaseToGeneAssociationSummaries(new DiseaseIdentifier(diseaseId));

        List<GeneAssociationSummary> curatedAssociationSummaries = new ArrayList<GeneAssociationSummary>();
        List<GeneAssociationSummary> phenotypeAssociationSummaries = new ArrayList<GeneAssociationSummary>();

        for (Disease disease : diseaseToGeneAssociationsMap.keySet()) {
            model.addAttribute("disease", disease);
            logger.info(String.format("Found disease: %s %s", disease.getDiseaseId(), disease.getTerm()));
            List<GeneAssociationSummary> geneAssociationSummarys = diseaseToGeneAssociationsMap.get(disease);
//            if (!geneAssociationSummarys.isEmpty()) {
                for (GeneAssociationSummary geneAssociationSummary : geneAssociationSummarys) {
                    AssociationSummary associationSummary = geneAssociationSummary.getAssociationSummary();
                    //always want the associations in the phenotypes list
                    if (associationSummary.getBestHtpcScore() > 0.0 || associationSummary.getBestModScore() > 0.0) {
                        phenotypeAssociationSummaries.add(geneAssociationSummary);
                    }
                    //but only the curated ones in the curated list...
                    if (associationSummary.isAssociatedInHuman() || associationSummary.isHasLiteratureEvidence()) {
                        curatedAssociationSummaries.add(geneAssociationSummary);
                    }
                }
//            }

        }

        model.addAttribute("curatedAssociations", curatedAssociationSummaries);
        model.addAttribute("phenotypeAssociations", phenotypeAssociationSummaries);

        return "disease";
    }
}
