package uk.ac.sanger.phenodigm2.config;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.sanger.phenodigm2.dao.PhenoDigmWebDaoSolrImpl;

/**
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
@Configuration
public class SolrTestConfig {

    @Bean
    public SolrServer solrServer() {
        CoreContainer coreContainer = new CoreContainer("build/solr/");
        coreContainer.load();
        return new EmbeddedSolrServer(coreContainer, "phenodigm");
    }

    @Bean
    public PhenoDigmWebDaoSolrImpl phenoDigmWebDaoSolrImpl() {
        SolrTestSetup solrTestSetup = new SolrTestSetup(solrServer());
        solrTestSetup.rebuildCore();
        return new PhenoDigmWebDaoSolrImpl();
    }
}
