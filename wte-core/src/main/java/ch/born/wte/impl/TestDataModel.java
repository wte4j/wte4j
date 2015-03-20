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
package ch.born.wte.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

import ch.born.wte.WteDataModel;

/**
 * A {@link WteDataModel} which creates dummy values for the given keys and
 * types.
 * 
 * The following types are suported:
 * <ul>
 * <li>java.lang.String
 * <li>java.lang.Boolean
 * <li>Subclasses of java.lang.Number (Integer, Long, Float, Double, BigInteger,
 * BigDecimal)
 * <li>Subclasses of java.lang.Date
 * </ul>
 */
class TestDataModel implements WteDataModel {

	public static final String STRING_TEXT = "Lorem ipsum";
	public static final BigDecimal DEZIMAL_NUMBER = new BigDecimal("1236.1123");
	public static final BigInteger INTEGER_NUMER = new BigInteger("1236");
	public static final Boolean BOOLEAN_VALUE = Boolean.TRUE;
	private Map<String, Class<?>> elements;

	public TestDataModel(Map<String, Class<?>> someElements) {
		elements = someElements;
	}

	@Override
	public Object getValue(String key) throws IllegalArgumentException {
		Class<?> expectedType = elements.get(key);
		if (expectedType == null) {
			throw new IllegalArgumentException("key is not supported");
		}
		return createValue(key, expectedType);

	}

	Object createValue(String key, Class<?> expectedType) {
		if (String.class.equals(expectedType)) {
			return STRING_TEXT;
		}

		else if (Number.class.isAssignableFrom(expectedType)) {
			return createNumber(expectedType);
		}

		else if (Date.class.isAssignableFrom(expectedType)) {
			return createDateValue((Class<? extends Date>) expectedType);
		} else if (Boolean.class.isAssignableFrom(expectedType)) {
			return BOOLEAN_VALUE;
		}
		throw createUnsuportedTypeException(expectedType);
	}

	private Date createDateValue(Class<? extends java.util.Date> expectedType) {
		long time = System.currentTimeMillis();
		if (java.util.Date.class.equals(expectedType)) {
			return new Date(time);
		}
		if (java.sql.Date.class.equals(expectedType)) {
			return new java.sql.Date(time);
		}
		if (java.sql.Time.class.equals(expectedType)) {
			return new java.sql.Time(time);
		}
		if (java.sql.Timestamp.class.equals(expectedType)) {
			return new java.sql.Timestamp(time);
		}
		throw createUnsuportedTypeException(expectedType);

	}

	Object createNumber(Class<?> expectedType) {
		if (Integer.class.equals(expectedType)
				|| Integer.TYPE.equals(expectedType)) {
			return INTEGER_NUMER.intValue();
		}
		if (Long.class.equals(expectedType) || Long.TYPE.equals(expectedType)) {
			return INTEGER_NUMER.longValue();
		}
		if (BigInteger.class.equals(expectedType)) {
			return INTEGER_NUMER;
		}
		if (Float.class.equals(expectedType) || Float.TYPE.equals(expectedType)) {
			return DEZIMAL_NUMBER.floatValue();
		}
		if (Double.class.isAssignableFrom(expectedType)
				|| Double.TYPE.equals(expectedType)) {
			return DEZIMAL_NUMBER.doubleValue();
		}
		if (BigDecimal.class.equals(expectedType)) {
			return DEZIMAL_NUMBER;
		}
		throw createUnsuportedTypeException(expectedType);
	}

	private RuntimeException createUnsuportedTypeException(Class<?> expectedType) {
		return new IllegalArgumentException("Can not create value for "
				+ expectedType.getName());
	}
}
