/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.graph;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestAPIFacade;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.neo4j.rest.graphdb.util.ConvertedResult;
import org.neo4j.rest.graphdb.util.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.neo4j.annotation.QueryType;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.conversion.ResultConverter;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.query.QueryEngine;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import uk.ac.sanger.phenodigm2.dao.PhenoDigmDao;
import uk.ac.sanger.phenodigm2.graph.converter.DiseaseNodeConverter;
import uk.ac.sanger.phenodigm2.graph.converter.NodeConverter;
import uk.ac.sanger.phenodigm2.model.Disease;
import uk.ac.sanger.phenodigm2.model.DiseaseAssociation;
import uk.ac.sanger.phenodigm2.model.Gene;
import uk.ac.sanger.phenodigm2.model.GeneIdentifier;
import uk.ac.sanger.phenodigm2.model.MouseModel;
import uk.ac.sanger.phenodigm2.model.PhenotypeMatch;
import uk.ac.sanger.phenodigm2.model.PhenotypeTerm;

/**
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
@Repository
public class PhenoDigmDaoNeo4jImpl implements PhenoDigmDao {

    private static final Logger logger = LoggerFactory.getLogger(PhenoDigmDaoNeo4jImpl.class);

    @Autowired
    private RestGraphDatabase graphDatabaseService;

    private RestCypherQueryEngine queryEngine;

    public PhenoDigmDaoNeo4jImpl() {
//        init();
    }

//    private void init() {
//        RestAPI restAPI = ((RestGraphDatabase) graphDatabaseService).getRestAPI();
//        queryEngine = new RestCypherQueryEngine(restAPI);
//    }

    
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Disease getDiseaseByDiseaseId(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Disease> getDiseasesByHgncGeneId(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Disease> getDiseasesByMgiGeneId(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<Disease, Set<DiseaseAssociation>> getKnownDiseaseAssociationsForMgiGeneId(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<Disease, Set<DiseaseAssociation>> getPredictedDiseaseAssociationsForMgiGeneId(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<GeneIdentifier, Set<DiseaseAssociation>> getKnownDiseaseAssociationsForDiseaseId(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<GeneIdentifier, Set<DiseaseAssociation>> getPredictedDiseaseAssociationsForDiseaseId(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<GeneIdentifier> getAllMouseGeneIdentifiers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GeneIdentifier getGeneIdentifierForMgiGeneId(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GeneIdentifier getHumanOrthologIdentifierForMgiGeneId(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PhenotypeTerm> getDiseasePhenotypeTerms(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<MouseModel> getAllMouseModels() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PhenotypeTerm> getMouseModelPhenotypeTerms(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PhenotypeMatch> getPhenotypeMatches(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Gene getGene(GeneIdentifier gi) {
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
