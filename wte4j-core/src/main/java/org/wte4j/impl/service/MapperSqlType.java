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
package org.wte4j.impl.service;

import org.wte4j.WteException;

/**
 * Map JDBC types (as defined in <code>java.sql.Types</code>) to Java types. The
 * mappings have been taken from [1]
 * "JDBC 4.0 Specification, JSR 221, November 7, 2006, Appendix B, Table B-3"
 * 
 */
final class MapperSqlType {
	private MapperSqlType() {
	};

	public static Class<?> map(int jdbcType) {
		switch (jdbcType) {
		case java.sql.Types.BIT:
		case java.sql.Types.BOOLEAN:
			return java.lang.Boolean.class;
		case java.sql.Types.TINYINT:
		case java.sql.Types.SMALLINT:
		case java.sql.Types.INTEGER:
			return java.lang.Integer.class;
		case java.sql.Types.BIGINT:
			return java.lang.Long.class;
		case java.sql.Types.FLOAT:
		case java.sql.Types.DOUBLE:
			return java.lang.Double.class;
		case java.sql.Types.REAL:
			return java.lang.Float.class;
		case java.sql.Types.NUMERIC: // according to [1] Table B-1
		case java.sql.Types.DECIMAL:
			return java.math.BigDecimal.class;
		case java.sql.Types.CHAR:
		case java.sql.Types.VARCHAR:
		case java.sql.Types.LONGVARCHAR:
			return java.lang.String.class;
		case java.sql.Types.DATE:
			return java.sql.Date.class;
		case java.sql.Types.TIME:
			return java.sql.Time.class;
		case java.sql.Types.TIMESTAMP:
			return java.sql.Timestamp.class;
		case java.sql.Types.STRUCT:
			return java.sql.Struct.class;
		case java.sql.Types.ARRAY:
			return java.sql.Array.class;
		case java.sql.Types.BLOB:
			return java.sql.Blob.class;
		case java.sql.Types.CLOB:
			return java.sql.Clob.class;
		case java.sql.Types.REF:
			return java.sql.Ref.class;
		case java.sql.Types.DATALINK:
			return java.net.URL.class;
		case java.sql.Types.ROWID:
			return java.sql.RowId.class;
		case java.sql.Types.NULL:
		case java.sql.Types.OTHER:
		case java.sql.Types.JAVA_OBJECT:
		case java.sql.Types.DISTINCT:
		case java.sql.Types.BINARY:
		case java.sql.Types.VARBINARY:
		case java.sql.Types.LONGVARBINARY:
		default:
			throw new WteException("invalid or unmapped SQL type (" + jdbcType
					+ ")");
		}

	}
}
