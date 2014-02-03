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
package uk.ac.sanger.phenodigm2.graph;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.neo4j.graphdb.Node;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.neo4j.rest.graphdb.util.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import uk.ac.sanger.phenodigm2.dao.PhenoDigmDao;
import uk.ac.sanger.phenodigm2.graph.converter.DiseaseNodeConverter;
import uk.ac.sanger.phenodigm2.graph.converter.NodeConverter;
import uk.ac.sanger.phenodigm2.graph.converter.PhenotypeNodeConverter;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseIdentifier;
import uk.ac.sanger.phenodigm2.model.DiseaseModelAssociation;
import uk.ac.sanger.phenodigm2.model.Gene;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.model.MouseModel;
import uk.ac.sanger.phenodigm2.model.PhenotypeMatch;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;
import uk.ac.sanger.phenodigm2.web.DiseaseAssociationSummary;
import uk.ac.sanger.phenodigm2.web.DiseaseGeneAssociationDetail;
import uk.ac.sanger.phenodigm2.web.GeneAssociationSummary;

/**
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
@Repository
public class PhenoDigmDaoNeo4jImpl implements PhenoDigmDao {

    private static final Logger logger = LoggerFactory.getLogger(PhenoDigmDaoNeo4jImpl.class);

    private RestGraphDatabase graphDatabaseService;

    private RestCypherQueryEngine queryEngine;

    public PhenoDigmDaoNeo4jImpl(RestGraphDatabase graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
        RestAPI restAPI = ((RestGraphDatabase) graphDatabaseService).getRestAPI();
        queryEngine = new RestCypherQueryEngine(restAPI);
    }
    
    @Override
    public Set<Disease> getAllDiseses() {
        logger.info("Getting all diseases...");
//            String sql = "select distinct h2mo.*, d.*  from disease d left join disease_genes og on og.disease_id = d.disease_id left join human2mouse_orthologs h2mo on og.omim_gene_id = h2mo.omim_gene_id where d.type = 'disease';";
        String cypher = "MATCH (disease:Disease) RETURN disease;";
        
        RestCypherQueryEngine queryEngine = new RestCypherQueryEngine(((RestGraphDatabase) graphDatabaseService).getRestAPI());
        Iterator<Node> result = queryEngine.query(cypher, null).to(Node.class).iterator();;

        Set<Disease> returnSet = new TreeSet<Disease>();

        NodeConverter<Disease> diseaseConverter = new DiseaseNodeConverter();
        
        while (result.hasNext()) {
            Node node = result.next();
            Disease disease = diseaseConverter.convert(node);
//            logger.info("Made {}", disease.getTerm());
            returnSet.add(disease);
        }
        
        return returnSet;
    }

    @Override
    public Set<Gene> getAllGenes() {
        
        String cypher = "MATCH (mouseGene:Gene) - [:IS_ORTHOLOG_OF] - humanGene  RETURN mouseGene.geneSymbol, mouseGene.geneId, humanGene.geneSymbol, humanGene.geneId;";
        RestCypherQueryEngine queryEngine = new RestCypherQueryEngine(((RestGraphDatabase) graphDatabaseService).getRestAPI());
        Iterator<Map<String, Object>> results = queryEngine.query(cypher, null).iterator();

        Set<Gene> returnSet = new TreeSet<Gene>();
        
        while (results.hasNext()) {
            Map<String, Object> result = results.next();
            GeneIdentifier mouseIdentifier = new GeneIdentifier((String) result.get("mouseGene.geneSymbol"), (String) result.get("mouseGene.geneId"));
            GeneIdentifier humanIdentifier = new GeneIdentifier((String) result.get("humanGene.geneSymbol"), (String) result.get("humanGene.geneId"));
            Gene gene = new Gene(mouseIdentifier, humanIdentifier);
//            logger.info("Made {}", gene);
            returnSet.add(gene);
        }
        
        return returnSet;
    }

    @Override
    public Disease getDisease(uk.ac.sanger.phenodigm2.model.DiseaseIdentifier diseaseId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PhenotypeTerm> getDiseasePhenotypes(DiseaseIdentifier diseaseId) {
        String cypher = "MATCH (disease:Disease) <- [:IS_PHENOTYPE_OF] - phenotype "
                + "WHERE disease.diseaseId = {diseaseId} "
                + "RETURN phenotype";

        RestCypherQueryEngine queryEngine = new RestCypherQueryEngine(((RestGraphDatabase) graphDatabaseService).getRestAPI());
        Iterator<Node> result = queryEngine.query(cypher, MapUtil.map("diseaseId", diseaseId)).to(Node.class).iterator();
        
        
        List<PhenotypeTerm> phenotypes = new ArrayList<PhenotypeTerm>();
        
        PhenotypeNodeConverter phenotypeNodeConverter =  new PhenotypeNodeConverter();
        while (result.hasNext()) {
            Node node = result.next();
            PhenotypeTerm term = phenotypeNodeConverter.convert(node);
            phenotypes.add(term);
//            logger.info("{}", term);
        }
        
        return phenotypes;
    }

    @Override
    public Set<MouseModel> getAllMouseModels() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PhenotypeTerm> getMouseModelPhenotypes(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PhenotypeMatch> getPhenotypeMatches(String string, Integer modelId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Gene getGene(GeneIdentifier geneIdentifier) {
        
        String cypher = "START gene=node:node_auto_index(geneId = {geneId}) MATCH gene -[:IS_ORTHOLOG_OF]- orthologGene RETURN orthologGene.geneSymbol, orthologGene.geneId;";
        
        RestCypherQueryEngine queryEngine = new RestCypherQueryEngine(((RestGraphDatabase) graphDatabaseService).getRestAPI());
        Iterator<Map<String, Object>> results = queryEngine.query(cypher, MapUtil.map("geneId", geneIdentifier.getCompoundIdentifier())).iterator();
        
        Gene gene = null;
        
        while (results.hasNext()) {
            Map<String, Object> result = results.next();
            
            GeneIdentifier orthologIdentifier = new GeneIdentifier((String) result.get("orthologGene.geneSymbol"), (String) result.get("orthologGene.geneId"));
            //The constructor for Gene assumes mouse the human genes to be 
            //supplied, but we can't guarantee someone didn't search for a human gene 
            if (geneIdentifier.getDatabaseCode().equals("MGI")) {
             gene = new Gene(geneIdentifier, orthologIdentifier);   
            }
            else {
             gene = new Gene(orthologIdentifier, geneIdentifier);                   
            }
//            logger.info("Made {}", gene);
            return gene;
        }
        logger.info("No gene matching {} found.", geneIdentifier);
        return null;
    }

    @Override
    public Map<Disease, List<GeneAssociationSummary>> getDiseaseToGeneAssociationSummaries(DiseaseIdentifier diseaseId, double minScoreCutoff) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<Gene, List<DiseaseAssociationSummary>> getGeneToDiseaseAssociationSummaries(GeneIdentifier geneId, double minScoreCutoff) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DiseaseGeneAssociationDetail getDiseaseGeneAssociationDetail(DiseaseIdentifier diseaseId, GeneIdentifier geneId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    private static class DiseaseResultSetExtractor implements ResultSetExtractor<Set<Disease>> {

        Set<Disease> diseases;

        public DiseaseResultSetExtractor() {
            diseases = new TreeSet<Disease>();
        }

        @Override
        public Set<Disease> extractData(ResultSet rs) throws SQLException, DataAccessException {

            while (rs.next()) {
                String diseaseId = rs.getString(1);
                Disease disease = new Disease(diseaseId);
                String term = rs.getString(2);
                disease.setTerm(term);
                logger.info(disease.toString());

//                String altTerms = rs.getString("disease_alts");
                diseases.add(disease);
            }
            return diseases;
        }
    }

}
