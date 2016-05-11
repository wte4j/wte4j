/**
 * Copyright (C) 2015 adesso Schweiz AG (www.adesso.ch)
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
package org.wte4j.impl.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.wte4j.FormatterFactory;
import org.wte4j.Template;
import org.wte4j.WteModelService;
import org.wte4j.impl.TemplateContext;
import org.wte4j.impl.TemplateContextFactory;

@Component
public class TemplateContextFactoryImpl implements TemplateContextFactory {
	@Autowired
	private FormatterFactory formatterFactory;

	@Autowired(required = false)
	@Qualifier("wteModelService")
	private WteModelService modelService;

	@Override
	public <E> TemplateContext<E> createTemplateContext(Template<E> template) {
		return new TemplateContextImpl<E>(formatterFactory, modelService, template);
	}

}
