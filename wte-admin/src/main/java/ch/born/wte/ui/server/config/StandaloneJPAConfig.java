package ch.born.wte.ui.server.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StandaloneJPAConfig {
    
    @Autowired
    @Qualifier("wte")
    private DataSource dataSource;

    @Bean
    @Qualifier("wte")
    public LocalContainerEntityManagerFactoryBean wteEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emfFactoryBean = new LocalContainerEntityManagerFactoryBean();
        emfFactoryBean.setDataSource(dataSource);
        emfFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emfFactoryBean.setPersistenceUnitName("wte-templates");
        return emfFactoryBean;
    }




    @Bean
    @Qualifier("wte")
    public PlatformTransactionManager wteTransATransactionManager(@Qualifier("wte") EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}
