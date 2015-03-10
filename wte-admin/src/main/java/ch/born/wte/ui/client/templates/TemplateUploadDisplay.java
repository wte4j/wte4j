package ch.born.wte.ui.client.templates;

import ch.born.wte.ui.shared.TemplateDto;

import com.google.gwt.user.client.ui.IsWidget;

public interface TemplateUploadDisplay extends IsWidget {

	public abstract void setData(TemplateDto currentTemplate,
			String templateUploadRestURL);

	public abstract void setPresenter(
			TemplateUploadPresenter templateUploadPresenter);

}