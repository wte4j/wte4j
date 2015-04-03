package org.wte4j.examples.showcase.server.hsql;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

public class ShowCaseDbInitializer {

	private ResourcePatternResolver resourceLoader;

	public ShowCaseDbInitializer(ResourcePatternResolver resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public HsqlServerBean createDatabase(Path dataBaseLocation) {
		Path hsqlScript = dataBaseLocation.resolve("wte4j-showcase.script");
		if (!Files.exists(hsqlScript)) {
			try {
				if (!Files.exists(dataBaseLocation)) {
					Files.createDirectory(dataBaseLocation);
				}
				Resource[] resources = resourceLoader.getResources("classpath:/db/*");
				for (Resource resource : resources) {
					Path filePath = dataBaseLocation.resolve(resource.getFilename());
					File source = resource.getFile();
					Files.copy(source.toPath(), filePath);
				}
			} catch (IOException e) {
				throw new IllegalArgumentException("can not copy database files", e);
			}
		}
		return new HsqlServerBean(dataBaseLocation, "wte4j-showcase");
	}

	public static void main(String... args) {
		ShowCaseDbInitializer initializer = new ShowCaseDbInitializer(new StaticApplicationContext());
		HsqlServerBean hsqlServerBean = initializer.createDatabase(Paths.get(args[0]));
		hsqlServerBean.startDatabase();
	}

}
