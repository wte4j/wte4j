--
-- Copyright (C) 2015 adesso Schweiz AG (www.adesso.ch)
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

INSERT INTO WTE4J_TEMPLATE (ID, CONTENT, CREATED_AT, DOCUMENT_NAME, EDITED_AT, EDITOR_DISPLAY_NAME, EDITOR_USER_ID, INPUT_CLASS_NAME, LANGUAGE, LOCKING_DATE, LOCKING_USER_DISPLAY_NAME, LOCKING_USER_ID, VERSION) VALUES (1, null, '2014-01-12 10:09:08', 'test1', '2014-02-12 11:10:09', 'Test User', 'test_user', 'java.lang.Integer', 'en', null, null, null, 1);
INSERT INTO WTE4J_TEMPLATE (ID, CONTENT, CREATED_AT, DOCUMENT_NAME, EDITED_AT, EDITOR_DISPLAY_NAME, EDITOR_USER_ID, INPUT_CLASS_NAME, LANGUAGE, LOCKING_DATE, LOCKING_USER_DISPLAY_NAME, LOCKING_USER_ID, VERSION) VALUES (2, null, '2014-01-13 10:09:08', 'test2', '2014-01-13 10:09:08', 'Test User 2', 'test_user2', 'java.lang.Long', 'de', '2014-01-15 10:00:00', 'Test User', 'test_user', 0);
INSERT INTO WTE4J_TEMPLATE (ID, CONTENT, CREATED_AT, DOCUMENT_NAME, EDITED_AT, EDITOR_DISPLAY_NAME, EDITOR_USER_ID, INPUT_CLASS_NAME, LANGUAGE, LOCKING_DATE, LOCKING_USER_DISPLAY_NAME, LOCKING_USER_ID, VERSION) VALUES (3, null, '2014-01-13 10:00:00', 'test2', '2014-01-13 12:00:00', 'Test User 2', 'test_user2', 'java.lang.Long', 'fr', null, null, null, 1);
INSERT INTO WTE4J_TEMPLATE (ID, CONTENT, CREATED_AT, DOCUMENT_NAME, EDITED_AT, EDITOR_DISPLAY_NAME, EDITOR_USER_ID, INPUT_CLASS_NAME, LANGUAGE, LOCKING_DATE, LOCKING_USER_DISPLAY_NAME, LOCKING_USER_ID, VERSION) VALUES (4, null, '2014-01-10 08:33:15', 'document1', '2014-01-10 08:33:15', 'Karz-Heinz', 'karl-heinz', 'java.lang.String', 'de', null, null, null, 0);

INSERT INTO WTE4J_GEN (sequence_name, sequence_next) VALUES ('wte4j_template', 5);
