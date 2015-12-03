package uk.ac.sanger.phenodigm2.web.config;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import uk.ac.sanger.phenodigm2.web.controller.DiseaseController;
import uk.ac.sanger.phenodigm2.web.controller.GeneController;
import uk.ac.sanger.phenodigm2.dao.PhenoDigmWebDao;
import uk.ac.sanger.phenodigm2.dao.PhenoDigmWebDaoSolrImpl;

import javax.sql.DataSource;

/**
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
@Configuration
public class AppConfig {

    @Autowired
    Environment environment;

    //only required if using the PhenoDigmWebDaoJdbcImpl
//    @Bean
//    public DataSource dataSource() {
//        JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
//        dsLookup.setResourceRef(true);
//        DataSource dataSource = dsLookup.getDataSource("jdbc/phenodigmDataSource");
//        return dataSource;
// In old XML money:
// <jee:jndi-lookup id="phenodigmDataSource" jndi-name="jdbc/phenodigmDataSource" expected-type="javax.sql.DataSource" />
//    }

    @Bean
    public SolrServer solrServer(){
        return new HttpSolrServer(environment.getProperty("solrServerUrl"));
    }

    @Bean
    public PhenoDigmWebDao phenoDigmWebDao() {
        return new PhenoDigmWebDaoSolrImpl();
    }

}
