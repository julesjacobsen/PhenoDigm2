/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.controller;

import uk.ac.sanger.phenodigm2.web.DiseaseAssociationSummary;
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
import org.springframework.web.bind.annotation.RequestMethod;
import uk.ac.sanger.phenodigm2.dao.PhenoDigmWebDao;
import uk.ac.sanger.phenodigm2.model.Gene;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.web.AssociationSummary;

/**
 *
 * @author jj8
 */

@Controller
@RequestMapping("/gene")
public class GeneController {

    private static final Logger logger = LoggerFactory.getLogger(GeneController.class);
    
    @Autowired
    private PhenoDigmWebDao phenoDigmDao;
    
    @RequestMapping("/")
    public String allGenes(Model model) {
        logger.info("Making all genes page");
        //Get all genes from the phenoDigmDao where we have a known disease association
        Set<Gene> allGenes = new TreeSet<>();//phenoDigmDao.getAllGenes();       
//        for (Disease disease : phenoDigmDao.getAllDiseses()) {
//            for (GeneIdentifier geneIdentifier : disease.getAssociatedMouseGenes()) {
//                genesWithKnownDiseaseAssociations.add(phenoDigmDao.getGene(geneIdentifier));            
//            }
//        }
        model.addAttribute("genes", allGenes);
        return "genes";
    }
    
    @RequestMapping(value="/{acc}", method=RequestMethod.GET)
    public String gene(@PathVariable("acc") String acc, Model model) {
        String mgiId = acc;
        logger.info("Making gene page for " + mgiId);
        model.addAttribute("mgiId", mgiId);
        GeneIdentifier geneIdentifier = new GeneIdentifier(mgiId, mgiId);
        logger.info("Found GeneIdentifier: " + geneIdentifier);
        model.addAttribute("geneIdentifier", geneIdentifier);
        
        logger.info("Getting disease-gene associations for gene: " + mgiId);
        Map<Gene, List<DiseaseAssociationSummary>> geneToDiseaseAssociationsMap = phenoDigmDao.getGeneToDiseaseAssociationSummaries(geneIdentifier);
        
        List<DiseaseAssociationSummary> curatedAssociationSummaries = new ArrayList<DiseaseAssociationSummary>();
        List<DiseaseAssociationSummary> phenotypeAssociationSummaries = new ArrayList<DiseaseAssociationSummary>();
        
        for (Gene gene : geneToDiseaseAssociationsMap.keySet()) {
            logger.info(String.format("Found gene: %s %s", gene.getOrthologGeneId().getCompoundIdentifier(), gene.getOrthologGeneId().getGeneSymbol()));
            List<DiseaseAssociationSummary> diseaseAssociationSummarys = geneToDiseaseAssociationsMap.get(gene);
            
            for (DiseaseAssociationSummary geneAssociationSummary : diseaseAssociationSummarys) {
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
        }
            
        model.addAttribute("curatedAssociations", curatedAssociationSummaries);         
        model.addAttribute("phenotypeAssociations", phenotypeAssociationSummaries);
       
        return "gene";
    }    
}
