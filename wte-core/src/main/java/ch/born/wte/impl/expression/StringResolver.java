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
package ch.born.wte.impl.expression;

import ch.born.wte.Formatter;
import ch.born.wte.WteDataModel;

public class StringResolver implements ValueResolver<String> {
	private final String contentKey;
	private final Formatter formatter;

	public StringResolver(String contentKey, Formatter formatter) {
		this.contentKey = contentKey;
		this.formatter = formatter;
	}

	@Override
	public String resolve(WteDataModel model) throws ClassCastException {
		Object value = model.getValue(contentKey);
		if (value != null) {
			return formatter.format(value);
		} else {
			return null;
		}
	}
}
