
package uk.ac.sanger.phenodigm2.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.sql.DataSource;
import liquibase.Liquibase;
import liquibase.database.jvm.HsqlConnection;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author jj8
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@Repository
@ContextConfiguration(locations = {"classpath:/jdbc-test-services.xml"})
public class TestDatabase {

    private Logger logger = Logger.getLogger(TestDatabase.class);
    private Liquibase liquibase;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    
    private static final String CHANGE_LOG = "src/main/resources/liquibase/master.xml";
    private static final String CONNECTION_STRING = "jdbc:hsqldb:mem:testdb;shutdown=false";
    private static final String USER_NAME = "SA";
    private static final String PASSWORD = "";
    
    private Connection holdingConnection;
    
    public void setUp(String liqibaseContexts){
        try {
            ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();
            Class.forName("org.hsqldb.jdbcDriver");
            holdingConnection = getConnectionImpl();
//            holdingConnection = dataSource.getConnection();
            HsqlConnection hsconn = new HsqlConnection(holdingConnection);

            liquibase = new Liquibase(CHANGE_LOG, new FileSystemResourceAccessor(), hsconn);
            liquibase.dropAll();
            liquibase.update(liqibaseContexts);
            logger.info("Made new test database: " + liquibase.getDatabase().getLiquibaseSchemaName());
            hsconn.close();
        
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(TestDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private Connection getConnectionImpl() throws SQLException {
        return DriverManager.getConnection(CONNECTION_STRING, USER_NAME, PASSWORD);
    }
}
