package ch.born.wte.ui.server.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StandaloneJPAConfig {
    
    @Autowired
    Environment env;
    
    @Autowired(required=false)
    @Qualifier("wte")
    private DataSource externalDataSource;
    
    

    @Bean
    @Qualifier("wte")
    public LocalContainerEntityManagerFactoryBean wteEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emfFactoryBean = new LocalContainerEntityManagerFactoryBean();
        emfFactoryBean.setDataSource(lookUpDataSource());
        emfFactoryBean.setJpaVendorAdapter(new OpenJpaVendorAdapter());
        emfFactoryBean.setPersistenceUnitName("wte-templates");
        return emfFactoryBean;
    }
    
    
    private DataSource lookUpDataSource(){
        if(env.containsProperty("wte.jdbc.jndi") || env.containsProperty("wte.jdbc.url")){
            return wteInternalDataSource();
        }
        else {
            return externalDataSource;
        }
        
    }


    @Bean
    @Lazy
    public DataSource wteInternalDataSource() {
        if(env.containsProperty("wte.jdbc.jndi")){
            JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
            DataSource dataSource = dsLookup.getDataSource(env.getProperty("wte.jdbc.jndi"));
            return dataSource;
            
        }
        else if(env.containsProperty("wte.jdbc.url")){
            BasicDataSource dataSource=new BasicDataSource();
            dataSource.setUrl(env.getProperty("wte.jdbc.url"));
            dataSource.setDriverClassName(env.getProperty("wte.jdbc.driver"));
            dataSource.setUsername(env.getProperty("wte.jdbc.user"));
            dataSource.setPassword(env.getProperty("wte.jdbc.password"));
            return dataSource;
            
        } else{
            throw new BeanCreationException("NO Datasource defined");
        }
        
    }




    @Bean
    @Qualifier("wte")
    public PlatformTransactionManager wteTransactionManager(@Qualifier("wte") EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}
