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
package org.wte4j.ui.client.dialog;

import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalHeader;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class MessageDialog extends Modal {

	private ClickHandler externalOkClickHander;

	public MessageDialog(String titel, String msg, DialogType type) {
		init(titel, msg, type);
	}

	public MessageDialog(String title, String msg, DialogType type, ClickHandler okClickHander) {
		externalOkClickHander = okClickHander;
		init(title, msg, type);
	}

	private void init(String title, String msg, DialogType type) {
		setClosable(false);
		this.add(createHeader(title, type));
		this.add(createBody(msg, type));
	}

	private ModalBody createBody(String msg, DialogType type) {

		Paragraph message = new Paragraph(msg);
		message.setEmphasis(type.colorStyle());

		DialogPanel dialogPanel = new DialogPanel(type, message);
		dialogPanel.addOkClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (externalOkClickHander != null) {
					externalOkClickHander.onClick(event);
				}
				hide();
			}
		});

		if (type.hasCancelButton()) {
			setDataBackdrop(ModalBackdrop.STATIC);
			dialogPanel.addCancelClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					hide();

				}
			});
		}

		ModalBody body = new ModalBody();
		body.add(dialogPanel);
		return body;
	}

	private ModalHeader createHeader(String title, DialogType type) {
		ModalHeader header = new ModalHeader();

		Heading heading = new Heading(HeadingSize.H3);
		heading.setEmphasis(type.colorStyle());
		heading.setText(title);
		heading.setMarginBottom(0);
		heading.setMarginTop(0);
		header.add(heading);

		return header;
	}

}
