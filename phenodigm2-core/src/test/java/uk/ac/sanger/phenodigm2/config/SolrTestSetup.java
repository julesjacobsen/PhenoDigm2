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
package uk.ac.sanger.phenodigm2.config;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 *
 * @author jj8
 */
public class SolrTestSetup {

    private final SolrServer solrServer;
    
    private static final Logger logger = LoggerFactory.getLogger(SolrTestSetup.class);

    public SolrTestSetup(SolrServer solrServer) {
        this.solrServer = solrServer;
        logger.info("Setting up solr core for testing");
    }

    public void rebuildCore() {
        logger.info("Rebuilding Solr core");

        ModifiableSolrParams cleanAndImportParams = new ModifiableSolrParams();
        cleanAndImportParams.set("qt", "/dataimport");
        cleanAndImportParams.set("command", "full-import");
        cleanAndImportParams.set("clean", true);
        cleanAndImportParams.set("commit", true);

        runSolrQuery(cleanAndImportParams);
        //so the command has been sent - now we need to block anything from trying
        //to access the core untill it's finished indexing the database.
        //this will take about 10-20 seconds anyway, so let it do it's thing before pestering it to see if its finished.
        sleep(10000);
        while (!finishedIndexing()) {
            logger.info("Solr still not finished indexing - going to sleep for a few secs.");
            sleep(5000);
        }

        logger.info("DONE! Solr server should be sorted...");

    }

    private QueryResponse runSolrQuery(SolrParams solrParams) {
        try {
            return solrServer.query(solrParams);
        } catch (SolrServerException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            logger.error(null, ex);
        }
    }

    private boolean finishedIndexing() {

        //keep checking the status untill the indexing has finished
        ModifiableSolrParams statusParams = new ModifiableSolrParams();
        statusParams.set("qt", "/dataimport");
        statusParams.set("command", "status");
        boolean finished = false;

        QueryResponse response = runSolrQuery(statusParams);
        logger.info("Checking server status...");
        logger.info("{}", response);
        String status = (String) response.getResponse().get("status");
        logger.info("Response status: {}", status);

        if (status.equals("idle")) {
            logger.info("Server idle - should have finished indexing.");
            finished = true;
        }

        return finished;
    }

}
