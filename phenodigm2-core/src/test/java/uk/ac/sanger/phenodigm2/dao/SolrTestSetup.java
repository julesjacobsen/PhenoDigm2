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

import java.util.logging.Level;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import static uk.ac.sanger.phenodigm2.dao.PhenoDigmWebDaoTest.logger;

/**
 *
 * @author jj8
 */
public class SolrTestSetup {

    private SolrServer solrServer;
    
    private static final Logger logger = LoggerFactory.getLogger(SolrTestSetup.class);

    public SolrTestSetup(SolrServer solrServer, boolean rebuildCore) {
        logger.info("Setting up solr core for testing");
        this.solrServer = solrServer;
        if (rebuildCore) {
            rebuildCore();
        }
    }

    private void rebuildCore() {
        logger.info("Rebuilding Solr core");

        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("qt", "/dataimport");
        params.set("command", "full-import");
        params.set("clean", true);
        params.set("commit", true);
        try {
            solrServer.query(params);

        } catch (SolrServerException ex) {
            logger.error(null, ex);
        }
        //so the command has been sent - now we need to block anything from trying
        //to access the core untill it's finished indexing the database.
        try {
            //this will take about 30 seconds anyway, so let it do it's thing before pestering it to see if its finished.
            Thread.sleep(30000);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(SolrTestSetup.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (!finishedIndexing()) {
            try {
                Thread.sleep(5000);
                logger.info("Solr still not finished indexing - going to sleep for a few secs.");
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(SolrTestSetup.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        logger.info("DONE! Solr server should be sorted...");

    }

    private boolean finishedIndexing() {

        //keep checking the status untill the indexing has finished
        ModifiableSolrParams statusParams = new ModifiableSolrParams();
        statusParams.set("qt", "/dataimport");
        statusParams.set("command", "status");
        boolean finished = false;
        try {
            QueryResponse response = solrServer.query(statusParams);
            logger.info("Checking server status...");
            logger.info("{}", response);
            String status = (String) response.getResponse().get("status");
            logger.info("Response status: {}", status);

            if (status.equals("idle")) {
                finished = true;
            }
            if (finished) {
                logger.info("Server idle - should have finished indexing.");
            }

        } catch (SolrServerException ex) {
            java.util.logging.Logger.getLogger(SolrTestSetup.class.getName()).log(Level.SEVERE, null, ex);
        }

        return finished;
    }

}
