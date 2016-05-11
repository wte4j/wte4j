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

create table testDbView (
	id bigint generated by default as identity (start with 1), 
	testTinyint tinyint,
	testSmallint smallint,
	testInt integer,
	testReal real,
	testFloat float,
	testNumeric numeric(5,1),
	testDecimal decimal(6,2),
	testBlob blob, 
	testTimestamp timestamp, 
	testDate date,
	testTime time,
	testVarchar255 varchar(255), 
	testChar255 char(255),
	testLongvarchar255 longvarchar(255), 
	testClob clob,
	testBoolean boolean,
	testBit bit(2),
	
	primary key (id));

insert into testDbView
	(testTimestamp, testDate, testTime, testVarchar255, testChar255, testLongvarchar255, testBoolean,
	testTinyint, testSmallint, testInt, testReal, testFloat, testNumeric, testDecimal)
	values
	(now(), now(), now(), 'testVarchar255', 'testChar255', 'testLongvarchar255', true, 
	1, 1, 1, 1.1, 1.1, 1.1, 1.1)