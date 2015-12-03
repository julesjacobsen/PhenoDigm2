package uk.ac.sanger.phenodigm2.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Spring Boot application to run in a Tomcat container supporting servlet spec 3.1 from a WAR using JSP for the page
 * rendering. This is really a simple UI test for the phenodigm-core library. The pages are dog-ugly.
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
@SpringBootApplication
public class PhenodigmWebApp extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PhenodigmWebApp.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(PhenodigmWebApp.class, args);
    }

}
