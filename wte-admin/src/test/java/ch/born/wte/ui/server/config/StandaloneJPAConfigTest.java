package ch.born.wte.ui.server.config;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { StandaloneJPAConfigTest.TestConfiguration.class })
public class StandaloneJPAConfigTest {
    
    @PersistenceContext(unitName="wte-templates")
    EntityManager em;
    
    @Test
    public void test() {
        assertNotNull(em);
    }

    @Import(StandaloneJPAConfig.class)
    public static class TestConfiguration {
        
        @Bean
        @Qualifier("wte")
        public DataSource dataSource(){
            
            EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
            EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.HSQL).build();
            return db;
            
        }
    }
}
