package ch.born.wte.ui.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableTransactionManagement
@EnableWebMvc
@ComponentScan(basePackages={"ch.born.wte.ui.server.services", "ch.born.wte.impl"})
@PropertySource("/WEB-INF/wte.properties")
public class SpringApplicationConfig {
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }
}
