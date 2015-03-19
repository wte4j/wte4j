package ch.born.wte.ui.client.templates;

import ch.born.wte.ui.shared.TemplateDto;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;

public interface TemplateListDisplay extends IsWidget {

	HasData<TemplateDto> getDataContainer();
	
	void setDowndLoadCommand(ScheduledCommand command);

	void setUpdateCommand(ScheduledCommand command);

	void setUnlockCommand(ScheduledCommand command);

	void setLockCommand(ScheduledCommand command);

	void setDeleteCommand(ScheduledCommand command);

	void setSelectedTemplate(TemplateDto current);

}
