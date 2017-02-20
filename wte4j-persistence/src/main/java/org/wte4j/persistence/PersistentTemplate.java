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
package org.wte4j.persistence;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.wte4j.LockingException;
import org.wte4j.MappingDetail;
import org.wte4j.TemplateData;
import org.wte4j.User;

/**
 * Persistent data of a wte4j Template
 */
@Entity
@Table(name = "wte4j_template", uniqueConstraints = @UniqueConstraint(columnNames = {
		"document_name", "language" }))
@TableGenerator(name = "wte4j_gen", table = "wte4j_gen", valueColumnName = "sequence_next", pkColumnName = "sequence_name", pkColumnValue = "wte4j_template")
public class PersistentTemplate implements TemplateData{

	private static final String TEMPLATE_SUFFIX = ".docx";

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "wte4j_gen")
	private Long id;

	@Column(name = "document_name", nullable = false)
	private String documentName;

	@Column(name = "language", nullable = false)
	private String language;

	@Transient
	private Class<?> inputType;
	@Column(name = "input_class_name", length = 250)
	private String inputClassName;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "wte4j_template_properties", joinColumns = @JoinColumn(name = "template_id"))
	@MapKeyColumn(name = "property_key")
	@Column(name = "property_value")
	private Map<String, String> properties;

	@ElementCollection(fetch = FetchType.EAGER, targetClass = MappingDetail.class)
	@CollectionTable(name = "wte4j_template_content_mapping", joinColumns = @JoinColumn(name = "template_id"))
	@MapKeyColumn(name = "conentend_control_id")
	@AttributeOverrides({
		@AttributeOverride(name="value.formatterDefinition",column=@Column(name="formatter_definition",length=250)),
		@AttributeOverride(name="value.modelKey",column=@Column(name="model_key",length=250))
		
		})
	private Map<String, MappingDetail> contentMapping;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, updatable = false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "edited_at", nullable = false)
	private Date editedAt;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "userId", column = @Column(name = "editor_user_id", length = 50, nullable = false)),
			@AttributeOverride(name = "displayName", column = @Column(name = "editor_display_name", length = 100)) })
	private User editor;

	@Column(name = "locking_date", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lockingDate;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "userId", column = @Column(name = "locking_user_id", length = 50, nullable = true)),
			@AttributeOverride(name = "displayName", column = @Column(name = "locking_user_display_name", length = 100, nullable = true)) })
	private User lockingUser;

	@Lob
	@Column(name = "content", nullable = true, columnDefinition = "BLOB")
	private byte[] content;

	@Version
	private Long version;

	public Long getId() {
		return id;
	}

	public long getVersion() {
		return version;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getInputClassName() {
		return inputClassName;
	}

	public String getTemplateFileName() {
		return documentName + "_" + language + TEMPLATE_SUFFIX;

	}

	public void setInputType(Class<?> inputClass) {
		this.inputClassName = inputClass.getName();
		this.inputType = inputClass;
	}

	public Class<?> getInputType() {
		if (inputType == null) {
			initInputClass();
		}
		return inputType;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> modelProperties) {
		this.properties = modelProperties;
	}

	public Map<String, MappingDetail> getContentMapping() {
		return contentMapping;
	}

	public void setContentMapping(Map<String, MappingDetail> contentMapping) {
		this.contentMapping = contentMapping;
	}

	public User getLockingUser() {
		return lockingUser;
	}

	public void setLockingUser(User lockingUser) {
		this.lockingUser = lockingUser;
	}

	public Date getLockingDate() {
		return lockingDate;
	}

	public void setLockingDate(Date lockingDate) {
		this.lockingDate = lockingDate;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getEditor() {
		return editor;
	}

	public Date getEditedAt() {
		return editedAt;
	}

	public void setEditedAt(Date date) {
		editedAt = date;
	}

	public void setEditor(User user) {
		editor = user;
	}

	public byte[] getContent() {
		return content;
	}

	protected void setContent(byte[] bytes) {
		content = Arrays.copyOf(bytes, bytes.length);
	}

	public void setContent(byte[] newContent, User newEditor)
			throws LockingException {
		if (isLocked() && !isLockedBy(newEditor)) {
			throw new LockingException("Template is locked by an ohter user");
		}
		setContent(newContent);
		setEditor(newEditor);
		setEditedAt(new Date());
		if (getCreatedAt() == null) {
			setCreatedAt(getEditedAt());
		}
	}


	void initInputClass() {
		try {
			inputType = Class.forName(inputClassName);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Error restore inputClass", e);
		}
	}

	public boolean isLocked() {
		return lockingUser != null && lockingUser.getUserId() != null;
	}

	public boolean isLockedBy(User user) {
		return user.equals(lockingUser);
	}

	public void unlock() {
		lockingDate = null;
		lockingUser = null;
	}

	public void lock(User user) throws LockingException {
		if (isLocked() && !isLockedBy(user)) {
			throw new LockingException("Template is locked by an other user");
		}
		lockingDate = new Date();
		lockingUser = user;
	}

}
