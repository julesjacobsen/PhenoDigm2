/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.ac.sanger.phenodigm2.dao.DiseaseDao;

/**
 *
 * @author jj8
 */
@Controller
public class DiseaseController {
    
    @Autowired
    DiseaseDao diseaseDao;

    @RequestMapping(value = "/disease")
    public String allDiseases(Model model) {
        return "disease";
    }
}
