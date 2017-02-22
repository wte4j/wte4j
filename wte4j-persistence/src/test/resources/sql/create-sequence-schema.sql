--
-- Copyright (C) 2015 Born Informatik AG (www.born.ch)
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--         http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--
CREATE SEQUENCE TEMPLATE_SEQ START WITH 0 INCREMENT BY 1;
CREATE TABLE wte4j_template (id BIGINT NOT NULL, content BLOB, created_at TIMESTAMP NOT NULL, document_name VARCHAR(255) NOT NULL, edited_at TIMESTAMP NOT NULL, input_class_name VARCHAR(250), language VARCHAR(255) NOT NULL, locking_date TIMESTAMP, version BIGINT, editor_display_name VARCHAR(100), editor_user_id VARCHAR(50) NOT NULL, locking_user_display_name VARCHAR(100), locking_user_id VARCHAR(50), PRIMARY KEY (id), CONSTRAINT U_WT4JPLT_DOCUMENT_NAME UNIQUE (document_name, language));
CREATE TABLE wte4j_template_content_mapping (template_id BIGINT, conentend_control_id VARCHAR(255) NOT NULL, formatter_definition VARCHAR(250), model_key VARCHAR(250));
CREATE TABLE wte4j_template_properties (template_id BIGINT, property_key VARCHAR(255) NOT NULL, property_value VARCHAR(255));
CREATE INDEX I_WT4JPNG_TEMPLATE_ID ON wte4j_template_content_mapping (template_id);
CREATE INDEX I_WT4JRTS_TEMPLATE_ID ON wte4j_template_properties (template_id);