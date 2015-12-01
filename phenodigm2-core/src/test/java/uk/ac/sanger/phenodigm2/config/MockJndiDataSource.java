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

import javax.naming.NamingException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

/**
 *
 * @author jj8
 */
public class MockJndiDataSource {

    private DataSource dataSource;
    
    private SimpleNamingContextBuilder mockJndiDataSource;

    private static final Logger logger = LoggerFactory.getLogger(MockJndiDataSource.class);

    public MockJndiDataSource(DataSource dataSource) {
        this.dataSource = dataSource;

        try {
            logger.info("Setting-up new JNDI resource for testing");
            mockJndiDataSource = new SimpleNamingContextBuilder();
//THIS DOESN'T WORK!!!! providing the mockJndiDataSource with either a fully manually specified path, or an injected one seems to provide a null URL for the dataImportHandler....
//                DataSource phenodigmJdbcDataSource = new DriverManagerDataSource("jdbc:h2:file:users/jj8/target/phenodigm2;MODE=MySQL;IFEXISTS=TRUE;DATABASE_TO_UPPER=FALSE;SCHEMA=PHENODIGM2;","sa", "");
            mockJndiDataSource.bind("java:comp/env/jdbc/phenodigmDataSource", dataSource);
            mockJndiDataSource.activate();
            logger.info("Activated new JNDI resource: {}", mockJndiDataSource);
        } catch (IllegalStateException | NamingException ex) {
            logger.error(null, ex);
        }
    }

}
