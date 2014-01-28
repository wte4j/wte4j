package ch.born.wte.service;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;


@ContextConfiguration(locations = {
        "classpath:applicationContext-datasource.xml",
        "classpath:applicationContext-hibernate.xml",
"classpath:applicationContext-service.xml" })
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class AbstractIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests
implements ApplicationContextAware {


    private static final String JUNIT_TEST_DOCUMENT_DOTX = "junitTestDocument.dotx";

    private JdbcTemplate jdbcTemplate;



    public final Object getSpringBean(final String beanId) {
        return applicationContext.getBean(beanId);
    }

    public final JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplate();
            DataSource dataSource = (DataSource) getSpringBean("dataSource");
            jdbcTemplate.setDataSource(dataSource);
        }
        return jdbcTemplate;
    }

    public final Resource getResource(final String location) {
        return applicationContext.getResource(location);
    }

    public final Resource getTestTemplateFileAsResource() {
        Resource testDocument = getResource(JUNIT_TEST_DOCUMENT_DOTX);
        return testDocument;
    }

    public final String getJUnitTestTemplateFileName() {
        return JUNIT_TEST_DOCUMENT_DOTX;
    }
}
