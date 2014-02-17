/*
 * Copyright © 2011-2013 EMBL - European Bioinformatics Institute
 * and Genome Research Limited
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.sanger.phenodigm2.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.sanger.phenodigm2.model.MouseModel;

/**
 * Creates external links (URIs) for a given object. 
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class ExternalLinkFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(ExternalLinkFactory.class);
    
    private enum URL_PATTERNS {
        MGI_URL("<a href=\"http://informatics.jax.org/accession/%s\">%s</a>"),
        IMPC_URL("<a href=\"http://www.mousephenotype.org/data/genes/%s\">%s</a>");

        private final String urlPattern;
        
        private URL_PATTERNS(String urlPattern){
            this.urlPattern = urlPattern;
        }
        
        @Override
        public String toString() {
            return urlPattern;
        }
    
    }
    
    public static String buildLink(MouseModel mouseModel) {
        String returnUrl = "";
        String urlPattern = "";
        
        //find out what source of the model is for building the URL.
        switch (mouseModel.getSource()) {
            case "MGI":
                urlPattern = URL_PATTERNS.MGI_URL.toString();
                break;
            case "MGP":
                urlPattern = URL_PATTERNS.IMPC_URL.toString();
                break;           
        }
        //now figure out the allelic composition         
        String[] alleles = mouseModel.getAllelicComposition().split("/");
        String[] alleleIds;

        if (mouseModel.getAlleleIds() == null || mouseModel.getAlleleIds().isEmpty()) {
            //this is not good
            String alleleOne = String.format(urlPattern, mouseModel.getMgiGeneId(), formatAllelicCompositionHtml(alleles[0]));
            return String.format("%s/%s", alleleOne, alleleOne);
        } else {
            alleleIds = mouseModel.getAlleleIds().split("\\|");
        }
        //hopefully there should be some kind of allele id
        if (alleleIds.length == 1) {
            //this is a homozygote
            String alleleOne = String.format(urlPattern, alleleIds[0], formatAllelicCompositionHtml(alleles[0]));
            return String.format("%s/%s", alleleOne, alleleOne);
        }
        if (alleleIds.length == 2) {
            //this is a heterozygote
            String alleleOne = String.format(urlPattern, alleleIds[0], formatAllelicCompositionHtml(alleles[0]));
            String alleleTwo = String.format(urlPattern, alleleIds[1], formatAllelicCompositionHtml(alleles[1]));
            returnUrl = String.format("%s/%s", alleleOne, alleleTwo);
        }    
//        logger.info("Made MouseModel {} {} URL {}",mouseModel.getMgiGeneId(), mouseModel.getMgiModelId(), returnUrl);       
        return returnUrl;
    }
    
    /**
     * Returns an HTML formatted allelic composition by replacing all instances 
     * of '<' and '>' with '<sup>' and '</sup>'. For example this method returns
     * the input of 'Fgf9<tm1Dor>/Fgf9<tm1Dor>'
     * as           'Fgf9<sup>tm1Dor</sup>/Fgf9<sup>tm1Dor</sup>'.
     * @param allelicComposition
     * @return an HTML formatted allelic composition
     */
    public static String formatAllelicCompositionHtml(String allelicComposition) {
        //substitute old tags
        allelicComposition = allelicComposition.replace("<", "££").replace(">", "$$");
        //replace tags with html ones
        allelicComposition = allelicComposition.replace("££", "<sup>").replace("$$", "</sup>");
        return allelicComposition;
    }
}
