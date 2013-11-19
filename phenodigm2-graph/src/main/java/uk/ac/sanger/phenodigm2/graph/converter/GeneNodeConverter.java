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

package uk.ac.sanger.phenodigm2.graph.converter;

import org.neo4j.graphdb.Node;
import uk.ac.sanger.phenodigm2.model.Gene;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;


/**
 *
 * @author jj8
 */
public class GeneNodeConverter implements NodeConverter<Gene>{

    @Override
    public Gene convert(Node node) {
        
        GeneIdentifier mouseId = new GeneIdentifier((String) node.getProperty("geneSymbol"), (String) node.getProperty("geneId"));
                
        Gene gene = new Gene(mouseId, null);
        
        return gene;
    }
    
}
