/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.graph;

import java.util.List;
import java.util.Map;
import org.neo4j.rest.graphdb.RestAPIFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.sanger.phenodigm2.model.Disease;

/**
 *
 * @author jj8
 */
public class Main {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Main.class);
        
        RestAPIFacade restAPIFacade = new RestAPIFacade("http://localhost:7474/db/data");
        
        String query = "MATCH (disease:Disease) RETURN disease.diseaseId, disease.diseaseTerm";
        
        long roundtripTimeStart = System.currentTimeMillis();
        Map<?, ?> result = restAPIFacade.query(query, null);
        long roundtripTime = System.currentTimeMillis() - roundtripTimeStart;
        System.out.println(result.get("columns"));
        
        long getTimeStart = System.currentTimeMillis();
        List<List<Object>> rows = (List<List<Object>>) result.get("data");
        for (List<Object> row : rows) {
            System.out.println(row);
        }
        long getTime = System.currentTimeMillis() - getTimeStart;

        logger.info(rows.size() + " row(s), roundtrip time " + roundtripTime + " ms." + " Iterate/convert time " + getTime + " ms.");
        
        roundtripTimeStart = System.currentTimeMillis();
        result = restAPIFacade.query(query, null);
        roundtripTime = System.currentTimeMillis() - roundtripTimeStart;

        System.out.println(result.get("columns"));
        
        getTimeStart = System.currentTimeMillis();
        rows = (List<List<Object>>) result.get("data");
        for (List<Object> row : rows) {
            Disease disease = new Disease((String) row.get(0));
            disease.setTerm((String) row.get(1));
            logger.info(disease.toString());
        }
        getTime = System.currentTimeMillis() - getTimeStart;
        logger.info(rows.size() + " row(s), roundtrip time " + roundtripTime + " ms." + " Iterate/convert time " + getTime + " ms.");
        
        restAPIFacade.close();

    }

}
