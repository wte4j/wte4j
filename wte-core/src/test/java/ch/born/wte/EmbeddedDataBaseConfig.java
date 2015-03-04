package ch.born.wte;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@ImportResource({ "classpath:test-persistence-context.xml" })
public class EmbeddedDataBaseConfig {

	EmbeddedDatabase database;

	@PostConstruct
	public void init() {
		database = new EmbeddedDatabaseBuilder()
				.setName(this.toString())
				.setType(EmbeddedDatabaseType.HSQL).ignoreFailedDrops(true)				
				.addScript("classpath:sql/drops.sql")
				.addScript("classpath:sql/create-schema.sql")
				.addScript("classpath:sql/wte_template.sql")
				.addScript("classpath:sql/wte_template_properties.sql").build();
	}

	@Bean
	public DataSource dataSource() {
		return database;
	}

	@PreDestroy
	public void shutdown() {
		database.shutdown();
		database=null;
	}

}
