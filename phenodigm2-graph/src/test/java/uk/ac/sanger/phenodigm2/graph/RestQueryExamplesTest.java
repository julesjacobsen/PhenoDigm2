/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.rest.graphdb.RestAPI;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.QueryEngine;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.neo4j.rest.graphdb.util.ConvertedResult;
import org.neo4j.rest.graphdb.util.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.support.query.CypherQueryEngine;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.sanger.phenodigm2.graph.converter.DiseaseNodeConverter;
import uk.ac.sanger.phenodigm2.graph.converter.NodeConverter;
import uk.ac.sanger.phenodigm2.model.Disease;

/**
 *
 * @author jj8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-Graph.xml"})
public class RestQueryExamplesTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GraphDatabase graphDatabaseService;

    private RestCypherQueryEngine queryEngine;

    @Before
    public void init() throws Exception {
        RestAPI restAPI = ((RestGraphDatabase) graphDatabaseService).getRestAPI();
        queryEngine = new RestCypherQueryEngine(restAPI);
    }

    @Test
    public void getSingleValue() {
        String exptResult = "ANGELMAN SYNDROME; AS";

        String cypher = "MATCH (disease:Disease) WHERE disease.diseaseId = {diseaseId} RETURN disease.diseaseTerm;";

        String result = (String) queryEngine.query(cypher, MapUtil.map("diseaseId", "OMIM:105830")).to(String.class).single();

        assertEquals(exptResult, result);
    }

    @Test
    public void getSingleNode() {
        String exptResult = "ANGELMAN SYNDROME; AS";

        String cypher = "MATCH (disease:Disease) WHERE disease.diseaseId = {diseaseId} RETURN disease;";

//        RestCypherQueryEngine queryEngine = new RestCypherQueryEngine(((RestGraphDatabase) graphDatabaseService).getRestAPI());
        Node result = queryEngine.query(cypher, MapUtil.map("diseaseId", "OMIM:105830")).to(Node.class).single();
        DiseaseNodeConverter diseaseConverter = new DiseaseNodeConverter();
        Disease disease = diseaseConverter.convert(result);
        System.out.println(disease);
        assertEquals(exptResult, result.getProperty("diseaseTerm"));
    }

    @Test
    public void getManyValues() {
        String exptResult = "25";

        String cypher = String.format("MATCH (disease:Disease) RETURN disease.diseaseId, disease.diseaseTerm LIMIT %s;", exptResult);

        QueryEngine queryEngine = new RestCypherQueryEngine(((RestGraphDatabase) graphDatabaseService).getRestAPI());
        QueryResult<Map<String, Object>> result = queryEngine.query(cypher, null);

        List<Disease> resultsList = new ArrayList<Disease>();

        for (Map<String, Object> map : result) {
//            logger.info("{}" , map);
            Disease disease = new Disease((String) map.get("disease.diseaseId"));
            disease.setTerm((String) map.get("disease.diseaseTerm"));
            logger.info("Made {}", disease);
            resultsList.add(disease);
        }

        assertEquals(exptResult, String.valueOf(resultsList.size()));

        assertFalse(resultsList.get(0).getDiseaseId().isEmpty());
        assertFalse(resultsList.get(0).getTerm().isEmpty());
    }

    @Test
    public void getGeneNodesAssociatedWithDisease() {
        String cypher = "MATCH (disease:Disease) <- [r:IS_ASSOCIATED_WITH_BY_PREDICTION] - gene "
                + "WHERE disease.diseaseId = {diseaseId} "
                + "RETURN gene "
                + "LIMIT 25;";

        QueryEngine queryEngine = new RestCypherQueryEngine(((RestGraphDatabase) graphDatabaseService).getRestAPI());
        Iterator<Node> result = queryEngine.query(cypher, MapUtil.map("diseaseId", "OMIM:105830")).to(Node.class).iterator();
        while (result.hasNext()) {
            Node node = result.next();
            logger.info("{}", node.getProperty("geneId"));
        }

    }

    @Test
    public void getGeneAndMouseModelNodesAssociatedWithDisease() {
        String cypher = "MATCH (disease:Disease) <- [r:IS_PREDICTED_MODEL_OF] - (model:MouseModel) - [m:IS_MOUSE_MODEL_OF] -> (gene:Gene) "
                + "WHERE disease.diseaseId = {diseaseId} "
                + "RETURN disease, r, gene, m, model";

        QueryEngine queryEngine = new RestCypherQueryEngine(((RestGraphDatabase) graphDatabaseService).getRestAPI());
        QueryResult<Map<String, Object>> result = queryEngine.query(cypher, MapUtil.map("diseaseId", "OMIM:105830"));

        for (Map<String, Object> map : result) {
//            logger.info("{}" , map);
            Node diseaseNode = (Node) map.get("disease");
            logger.info("{}", diseaseNode);
        }

    }

    @Test
    public void getGeneAndMouseModelNodesAssociatedWithGene() {
        String cypher = "MATCH (gene:Gene) <- [m:IS_MOUSE_MODEL_OF] - (model:MouseModel) - [r:IS_PREDICTED_MODEL_OF] - > (disease:Disease)"
                + "WHERE gene.geneSymbol = {geneId} "
                + "RETURN disease, r, gene, m, model "
                + "ORDER BY r.modelToDiseaseScore DESC";

        QueryEngine queryEngine = new RestCypherQueryEngine(((RestGraphDatabase) graphDatabaseService).getRestAPI());
        QueryResult<Map<String, Object>> result = queryEngine.query(cypher, MapUtil.map("geneId", "Fgfr2"));

        NodeConverter<Disease> diseaseConverter = new DiseaseNodeConverter();

        for (Map<String, Object> map : result) {
//            logger.info("{}" , map);
            Node diseaseNode = (Node) map.get("disease");
            Node geneNode = (Node) map.get("gene");
            Node modelNode = (Node) map.get("model");
            Relationship rel = (Relationship) map.get("r");
            Disease disease = diseaseConverter.convert(diseaseNode);
            logger.info("{} {}-{} {}", rel.getProperty("modelToDiseaseScore"), geneNode.getProperty("geneSymbol"), modelNode.getProperty("modelId"), disease.getTerm());

        }
    }

    /**
     * Disease page.
     */
    @Test
    public void getGeneMouseModelAndPhenotypeNodesAssociatedWithDisease() {
//        String cypher = "MATCH (disease:Disease) <- [r:IS_PREDICTED_MODEL_OF] - (model:MouseModel) - [m:IS_MOUSE_MODEL_OF] -> (gene:Gene) "
        String cypher = "MATCH (gene:Gene) <- [m:IS_MOUSE_MODEL_OF] - (model:MouseModel) - [r:IS_PREDICTED_MODEL_OF] - > (disease:Disease) "
                + "WHERE disease.diseaseId = {diseaseId} "
                + "RETURN disease, r, gene, m, model "
                + "ORDER BY gene.geneSymbol, r.modelToDiseaseScore DESC";

        QueryEngine queryEngine = new RestCypherQueryEngine(((RestGraphDatabase) graphDatabaseService).getRestAPI());
        QueryResult<Map<String, Object>> result = queryEngine.query(cypher, MapUtil.map("diseaseId", "OMIM:105830"));

        NodeConverter<Disease> diseaseConverter = new DiseaseNodeConverter();

        for (Map<String, Object> map : result) {
//            logger.info("{}" , map);
            Node diseaseNode = (Node) map.get("disease");
            Disease disease = diseaseConverter.convert(diseaseNode);
            Node geneNode = (Node) map.get("gene");
            Node modelNode = (Node) map.get("model");
            Relationship rel = (Relationship) map.get("r");
            logger.info("{} {}-{} {}", disease.getTerm(), rel.getProperty("modelToDiseaseScore"), geneNode.getProperty("geneSymbol"), modelNode.getProperty("modelId"));
        }

    }

}
