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
package org.wte4j.ui.client.templates.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.gwtbootstrap3.client.ui.constants.AlertType;
import org.wte4j.ui.client.dialog.DialogType;
import org.wte4j.ui.client.dialog.MessageDialog;
import org.wte4j.ui.shared.InvalidTemplateServiceException;
import org.wte4j.ui.shared.MappingDto;
import org.wte4j.ui.shared.ModelElementDto;
import org.wte4j.ui.shared.TemplateDto;
import org.wte4j.ui.shared.TemplateServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MappingPresenter {

	private MappingDisplay display;

	private TemplateDto template;
	private String uploadedTemplateFile;

	private List<ModelElementDto> modelElements;
	private List<MappingDto> mappingData;

	private TemplateServiceAsync templateService = TemplateServiceAsync.Util.getInstance();

	public void bind(MappingDisplay aDisplay) {
		if (display != null) {
			throw new IllegalStateException("allready bind to a display");
		}
		display = aDisplay;

		display.addCancelButtonClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cancelEditMapping();

			}
		});

		display.addSaveButtonClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				saveMappingData();

			}
		});
	}

	public void startEditMapping(TemplateDto aTemplate, String aUploadedTemplateFile) {
		template = aTemplate;
		uploadedTemplateFile = aUploadedTemplateFile;
		initModelElements();
	}

	public void displayInvalidTemplateMessage(InvalidTemplateServiceException e) {
		display.showAlert(AlertType.DANGER, e.getMessage(), e.getDetails());

	}

	protected void initMappingData() {
		display.hideAlert();
		display.resetTable();
		templateService.listUniqueContentIds(uploadedTemplateFile, new AsyncCallback<List<String>>() {
			@Override
			public void onSuccess(List<String> contentIds) {
				updateTemplateMappingData(contentIds);
			}

			@Override
			public void onFailure(Throwable caught) {
				onInitMappingDataFailed(caught);
			}
		});
	}

	protected void onInitMappingDataFailed(Throwable caught) {
		displayError(caught);
	}

	protected void updateTemplateMappingData(List<String> contentIds) {
		mappingData = new ArrayList<>();
		for (String contentId : contentIds) {
			MappingDto mapping = createOreFindMapping(contentId);
			mappingData.add(mapping);
		}
		Collections.sort(mappingData, new Comparator<MappingDto>() {

			@Override
			public int compare(MappingDto o1, MappingDto o2) {
				return o1.getConentControlKey().toLowerCase().compareTo(o2.getConentControlKey().toLowerCase());
			}
		});

		display.setMappingData(mappingData);
	}

	private MappingDto createOreFindMapping(String contentControlId) {
		for (MappingDto mapping : template.getMapping()) {
			if (mapping.getConentControlKey().equals(contentControlId)) {
				return mapping;
			}
		}
		MappingDto mapping = new MappingDto();
		mapping.setContentControlKey(contentControlId);
		return mapping;
	}

	protected void initModelElements() {
		templateService.listModelElements(template.getInputType(), template.getProperties(), new AsyncCallback<List<ModelElementDto>>() {
			@Override
			public void onSuccess(List<ModelElementDto> modelElements) {
				onModelElementsloaded(modelElements);
			}

			@Override
			public void onFailure(Throwable caught) {
				onFailedLoadingModelElements(caught);
			}
		});
	}

	protected void onFailedLoadingModelElements(Throwable caught) {
		displayError(caught);
	}

	protected void onModelElementsloaded(List<ModelElementDto> someModelElements) {
		modelElements = someModelElements;
		List<String> modelIdOptions = new ArrayList<String>();
		modelIdOptions.add("");
		for (ModelElementDto element : modelElements) {
			modelIdOptions.add(element.getName());
		}

		Collections.sort(modelIdOptions);

		display.setModelIdOptions(modelIdOptions);
		initMappingData();
	}

	public final void saveMappingData() {
		prepareForSubmit();
		submit();
	}

	protected void prepareForSubmit() {
		display.hideAlert();
		updateMappingOfTemplate();
	}

	private void updateMappingOfTemplate() {
		template.setMapping(mappingData);
	}

	protected void submit() {
		templateService.saveTemplateData(template, uploadedTemplateFile, new AsyncCallback<TemplateDto>() {

			@Override
			public void onSuccess(TemplateDto result) {
				onSubmitSuccess(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				onSubmitFailed(caught);

			}
		});

	}

	protected void onSubmitSuccess(TemplateDto template) {
		display.hideAlert();
	}

	protected void onSubmitFailed(Throwable caught) {
		if (caught instanceof InvalidTemplateServiceException) {
			onSubmitFailed((InvalidTemplateServiceException) caught);
		}
		else {
			displayError(caught);
		}

	}

	protected void onSubmitFailed(InvalidTemplateServiceException e) {
		displayInvalidTemplateMessage(e);
	}

	protected void displayError(Throwable e) {
		new MessageDialog("", e.getMessage(), DialogType.ERROR).show();

	}

	public void cancelEditMapping() {
		template = null;
		uploadedTemplateFile = null;
		modelElements = null;
		mappingData = null;
	}

	protected TemplateDto getTemplate() {
		return template;
	}

	protected String getUploadedTemplateFile() {
		return uploadedTemplateFile;
	}

}
