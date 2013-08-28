/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.sanger.phenodigm2.dao;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 *
 * @author jj8
 */
@Configuration
public class JdbcConfiguration {

    @Value("${jdbc.driverClassName}")
    private String driverName;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String user;
    @Value("${jdbc.password}")
    private String password;
    
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setPassword(this.password);
        dataSource.setUrl(this.url);
        dataSource.setUsername(this.user);
        dataSource.setDriverClassName(this.driverName);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        DataSource ds = dataSource();
        return new JdbcTemplate(ds);
    }
}
