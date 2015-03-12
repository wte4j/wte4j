package ch.born.wte.ui.client;

import com.google.gwt.core.client.GWT;

public final class Application {

	public static final WteResources RESOURCES = GWT.create(WteResources.class);
	public static Labels LABELS = GWT.create(Labels.class);

	public static final String REST_SERVICE_BASE_URL = GWT.getModuleBaseURL() + "rest/";

	private Application() {
	}

}
