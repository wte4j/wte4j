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
package org.wte4j.ui.client;

import com.google.gwt.core.client.GWT;

public final class Application {

	public static final Wte4jResources RESOURCES = GWT.create(Wte4jResources.class);
	public static Labels LABELS = GWT.create(Labels.class);

	public static final String BASE_PATH;
	public static final String REST_SERVICE_BASE_URL;
	
	static {
		String baseUrl = GWT.getModuleBaseURL();
		baseUrl = baseUrl.substring(0, baseUrl.length() -1);
		baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf('/'));
		
		BASE_PATH = baseUrl + "/Wte4jAdmin/";
		REST_SERVICE_BASE_URL = BASE_PATH + "rest/";
	}
	
	private Application() {
	}

}
