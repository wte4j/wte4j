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
package org.wte4j.ui.shared;

import java.util.ArrayList;
import java.util.List;

public class InvalidTemplateServiceException extends TemplateServiceException {

	private List<String> details = new ArrayList<String>();

	public InvalidTemplateServiceException() {
	}

	public InvalidTemplateServiceException(String message) {
		super(message);
	}

	public InvalidTemplateServiceException(String summary, List<String> details) {
		super(summary);
		this.details = details;
	}

	public List<String> getDetails() {
		return details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}

}
