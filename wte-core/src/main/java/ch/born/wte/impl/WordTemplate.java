package ch.born.wte.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import ch.born.wte.FormatterFactory;
import ch.born.wte.InvalidTemplateException;
import ch.born.wte.LockingException;
import ch.born.wte.Template;
import ch.born.wte.User;
import ch.born.wte.WteDataModel;
import ch.born.wte.WteException;
import ch.born.wte.WteModelService;
import ch.born.wte.impl.expression.ResolverFactoryImpl;
import ch.born.wte.impl.expression.ValueResolverFactory;
import ch.born.wte.impl.word.Docx4JWordTemplate;

/**
 * {@link Template} implementation for word documents (docx)
 * 
 * @param <E>
 *            see {@link Template}
 */
public class WordTemplate<E> implements Template<E> {

	private WteModelService modelService;
	private FormatterFactory formatterFactory;

	private PersistentTemplate persistentData;
	private DocumentGenerator documentGenerator;

	public WordTemplate(PersistentTemplate template,
			WteModelService modelService, FormatterFactory formatterFactory) {
		this.persistentData = template;
		this.modelService = modelService;
		this.formatterFactory = formatterFactory;
	}

	@Override
	public void toDocument(E data, File file) throws IOException,
			InvalidTemplateException, WteException {
		OutputStream out = FileUtils.openOutputStream(file);
		toDocument(data, out);
	}

	@Override
	public void toDocument(E data, OutputStream out) throws IOException,
			InvalidTemplateException {
		prepareWorkDocument();
		WteDataModel model = modelService.createModel(this, data);
		documentGenerator.setContent(model);
		documentGenerator.writeAsOpenXML(out);
	}

	private void prepareWorkDocument() throws InvalidTemplateException,
			IOException {
		if (documentGenerator == null) {
			documentGenerator = createDocumentGenerator(persistentData
					.getContent());
		}
		documentGenerator.parseExpressions();
	}

	private DocumentGenerator createDocumentGenerator(byte[] content)
			throws IOException {
		Docx4JWordTemplate docx = new Docx4JWordTemplate(
				new ByteArrayInputStream(content));
		Map<String, Class<?>> elements = modelService.listModelElements(
				getInputType(), getProperties());
		ValueResolverFactory context = new ResolverFactoryImpl(getLocale(),
				formatterFactory, elements);
		return new DocumentGenerator(docx, context);
	}

	@Override
	public void toTestDocument(OutputStream out)
			throws InvalidTemplateException, IOException {
		Map<String, Class<?>> elements = modelService.listModelElements(
				persistentData.getInputType(), persistentData.getProperties());
		WteDataModel dataModel = new TestDataModel(elements);
		prepareWorkDocument();
		documentGenerator.setContent(dataModel);
		documentGenerator.writeAsOpenXML(out);
	}

	@Override
	public void toTestDocument(File file) throws IOException,
			InvalidTemplateException {
		OutputStream out = FileUtils.openOutputStream(file);
		toTestDocument(out);

	}

	@Override
	public String getDocumentName() {
		return persistentData.getDocumentName();
	}

	@Override
	public Class<?> getInputType() {
		return persistentData.getInputType();
	}

	@Override
	public Map<String, String> getProperties() {
		return persistentData.getProperties();
	}

	public String getLanguage() {
		return persistentData.getLanguage();
	}

	public Locale getLocale() {
		if (persistentData.getLanguage() == null) {
			return null;
		}
		return new Locale(persistentData.getLanguage());
	}

	public User getLockingUser() {
		return persistentData.getLockingUser();
	}

	public Date getCreatedAt() {
		return persistentData.getCreatedAt();
	}

	public User getEditor() {
		return persistentData.getEditor();
	}

	public Date getEditedAt() {
		return persistentData.getEditedAt();
	}

	@Override
	public void update(InputStream in, User editor) throws IOException,
			InvalidTemplateException, LockingException {
		byte[] content = IOUtils.toByteArray(in);
		DocumentGenerator newGenerator = createDocumentGenerator(content);
		newGenerator.parseExpressions();
		documentGenerator = newGenerator;
		persistentData.setContent(content, editor);
	}

	@Override
	public void write(File file) throws IOException {
		OutputStream out = FileUtils.openOutputStream(file);
		write(out);

	}

	@Override
	public void write(OutputStream out) throws IOException {
		persistentData.writeContent(out);
	}

	protected PersistentTemplate getPersistentData() {
		return persistentData;
	}
}
