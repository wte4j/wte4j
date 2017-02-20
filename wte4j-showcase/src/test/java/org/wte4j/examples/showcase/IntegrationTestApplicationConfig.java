/**
 * Copyright (C) 2015 Born Informatik AG (www.born.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wte4j.examples.showcase;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.wte4j.examples.showcase.server.config.RootApplicationConfig;
import org.wte4j.persistence.config.Wte4jPersistenceConfig;

@Configuration
@Import(Wte4jPersistenceConfig.class)
@PropertySource("classpath:/org/wte4j/examples/showcase/integration-test-application.properties")
public class IntegrationTestApplicationConfig extends RootApplicationConfig {

}
