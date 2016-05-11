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
package org.wte4j.ui.client.dialog;

import org.gwtbootstrap3.client.ui.constants.Emphasis;
import org.gwtbootstrap3.client.ui.constants.IconType;

public enum DialogType {
	INFO(IconType.INFO, Emphasis.INFO, false),
	WARNING(IconType.EXCLAMATION_TRIANGLE, Emphasis.WARNING, false),
	ERROR(IconType.EXCLAMATION_CIRCLE, Emphasis.DANGER, false),
	QUESTION(IconType.QUESTION_CIRCLE, Emphasis.PRIMARY, true);

	private final IconType icon;
	private final Emphasis colorStyle;
	private final boolean cancelButton;

	private DialogType(IconType icon, Emphasis colorStyle, boolean cancelButton) {
		this.icon = icon;
		this.colorStyle = colorStyle;
		this.cancelButton = cancelButton;
	}

	public IconType getIcon() {
		return icon;
	}

	public Emphasis colorStyle() {
		return colorStyle;
	}

	public boolean hasCancelButton() {
		return cancelButton;
	}

}
