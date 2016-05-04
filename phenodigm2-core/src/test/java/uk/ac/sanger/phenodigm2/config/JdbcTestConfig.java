package uk.ac.sanger.phenodigm2.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import uk.ac.sanger.phenodigm2.dao.PhenoDigmWebDaoJdbcImpl;

import javax.sql.DataSource;

/**
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
@Configuration
@PropertySource(value = "jdbc.properties")
public class JdbcTestConfig {

    @Autowired
    Environment env;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource;
        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("pheno.driverClassName"));
        dataSource.setUrl(env.getProperty("pheno.url") + env.getProperty("flyway.h2.args"));
        dataSource.setUsername(env.getProperty("pheno.username"));
        dataSource.setPassword(env.getProperty("pheno.password"));

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Lazy
    @Bean
    public Flyway flyway() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource());
        flyway.setSchemas("PHENODIGM2");
        flyway.migrate();
        return flyway;
    }

    @Bean
    public PhenoDigmWebDaoJdbcImpl phenoDigmWebDaoJdbcImpl() {
        flyway();
        return new PhenoDigmWebDaoJdbcImpl();
    }
}
