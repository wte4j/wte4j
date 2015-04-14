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
package org.wte4j.impl.word;

import java.util.Collection;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang3.StringUtils;
import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.w15.CTSdtAppearance;
import org.docx4j.w15.STSdtAppearance;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTSdtText;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.RStyle;
import org.docx4j.wml.SdtBlock;
import org.docx4j.wml.SdtElement;
import org.docx4j.wml.SdtPr;
import org.docx4j.wml.SdtPr.Alias;
import org.docx4j.wml.Tag;
import org.docx4j.wml.Text;

public class PlainTextContent {

	private static final ObjectFactory wFactory = Context.getWmlObjectFactory();
	private static final org.docx4j.w15.ObjectFactory w15Factory = new org.docx4j.w15.ObjectFactory();

	private SdtElement contentControl;
	private RPr runProperties;
	private CTSdtAppearance appearance;
	private Alias alias;

	public PlainTextContent(SdtElement sdtElement) {
		this.contentControl = sdtElement;
		initAlias();
		initAppearanceSettings();
		initRunProperties();
	}

	public static SdtBlock createSdtBlockElement() {
		SdtBlock block = wFactory.createSdtBlock();
		block.setSdtPr(createDefaultSdtProperties());
		block.setSdtContent(wFactory.createSdtContentBlock());
		return block;
	}

	private static SdtPr createDefaultSdtProperties() {
		SdtPr sdtProperties = wFactory.createSdtPr();
		sdtProperties.setTag(wFactory.createTag());
		sdtProperties.getRPrOrAliasOrLock().add(createPlainText());
		sdtProperties.getRPrOrAliasOrLock().add(createShowPlaceHolder());
		sdtProperties.getRPrOrAliasOrLock().add(
				creatAppearance(STSdtAppearance.TAGS));
		return sdtProperties;
	}

	private static JAXBElement<CTSdtText> createPlainText() {
		CTSdtText txtTag = wFactory.createCTSdtText();
		return wFactory.createSdtPrText(txtTag);
	}

	private static JAXBElement<BooleanDefaultTrue> createShowPlaceHolder() {
		BooleanDefaultTrue booleanDefaultTrue = wFactory
				.createBooleanDefaultTrue();
		return wFactory.createSdtPrShowingPlcHdr(booleanDefaultTrue);
	}

	private static JAXBElement<CTSdtAppearance> creatAppearance(
			STSdtAppearance value) {
		CTSdtAppearance appearance = w15Factory.createCTSdtAppearance();
		appearance.setVal(value);
		JAXBElement<CTSdtAppearance> appearanceElement = w15Factory
				.createAppearance(appearance);
		return appearanceElement;
	}

	public String getExpression() {
		String tag = "";
		if (hasExpression()) {
			tag = contentControl.getSdtPr().getTag().getVal();
		}
		return tag;
	}

	public void setExpression(String expression) {
		if (contentControl.getSdtPr() == null) {
			SdtPr sdtPr = wFactory.createSdtPr();
			contentControl.setSdtPr(sdtPr);
		}
		if (contentControl.getSdtPr().getTag() == null) {
			Tag tag = wFactory.createTag();
			contentControl.getSdtPr().setTag(tag);
		}
		contentControl.getSdtPr().getTag().setVal(expression);
	}

	public boolean hasExpression() {
		return contentControl.getSdtPr() != null
				&& contentControl.getSdtPr().getTag() != null
				&& StringUtils.isNotEmpty(contentControl.getSdtPr().getTag()
						.getVal());
	}

	public void setTitle(String value) {
		alias.setVal(value);
	}

	public void setPlaceHolderContent(String text) {
		contentControl.getSdtContent().getContent().clear();
		Text t = wFactory.createText();
		t.setValue(text);

		RStyle style = wFactory.createRStyle();
		style.setVal("PlaceholderText");
		RPr placeHolderProperties = wFactory.createRPr();
		placeHolderProperties.setRStyle(style);

		R run = wFactory.createR();
		run.setRPr(placeHolderProperties);
		run.getContent().add(t);
		setContent(run);
	}

	public void setContent(String value) {
		contentControl.getSdtContent().getContent().clear();

		Text t = wFactory.createText();
		t.setValue(value);

		R run = wFactory.createR();
		run.getContent().add(t);
		run.setRPr(runProperties);
		setContent(run);
	}

	public void hideMarkers() {
		appearance.setVal(STSdtAppearance.BOUNDING_BOX);
	}

	public void showMarkgers() {
		appearance.setVal(STSdtAppearance.TAGS);
	}

	void setContent(R run) {
		Object newContent = run;
		if (contentControl instanceof SdtBlock) {
			P paragraph = wFactory.createP();
			paragraph.getContent().add(run);
			newContent = paragraph;
		}
		contentControl.getSdtContent().getContent().add(newContent);
	}

	private void initRunProperties() {

		if (contentControl.getSdtPr() != null) {
			contentControl.getSdtPr().getRPrOrAliasOrLock();
			for (Object object : contentControl.getSdtPr()
					.getRPrOrAliasOrLock()) {
				object = XmlUtils.unwrap(object);
				if (object instanceof RPr) {
					runProperties = (RPr) object;
				}
			}
		}
	}

	private void initAppearanceSettings() {
		appearance = findElement(contentControl.getSdtPr()
				.getRPrOrAliasOrLock(), CTSdtAppearance.class);
		if (appearance == null) {
			JAXBElement<CTSdtAppearance> element = creatAppearance(STSdtAppearance.TAGS);
			contentControl.getSdtPr().getRPrOrAliasOrLock().add(element);
			appearance = element.getValue();
		}
	}

	private void initAlias() {
		alias = findElement(contentControl.getSdtPr().getRPrOrAliasOrLock(),
				Alias.class);
		if (alias == null) {
			alias = wFactory.createSdtPrAlias();
			JAXBElement<Alias> aliasElement = wFactory.createSdtPrAlias(alias);
			contentControl.getSdtPr().getRPrOrAliasOrLock().add(aliasElement);
		}
	}

	private static <E> E findElement(Collection<?> elements,
			Class<E> elementType) {
		for (Object object : elements) {
			object = XmlUtils.unwrap(object);
			if (elementType.isInstance(object)) {
				@SuppressWarnings("unchecked")
				E element = (E) object;
				return element;
			}
		}
		return null;
	}

	public static boolean isPlainTextContent(SdtElement element) {
		SdtPr properties = element.getSdtPr();
		for (Object object : properties.getRPrOrAliasOrLock()) {
			object = XmlUtils.unwrap(object);
			if (object instanceof CTSdtText) {
				return true;
			}
		}
		return false;
	}

	protected SdtElement getXmlElement() {
		return contentControl;
	}

}
