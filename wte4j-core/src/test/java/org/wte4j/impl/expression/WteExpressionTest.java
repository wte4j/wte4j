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
package org.wte4j.impl.expression;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class WteExpressionTest {

	private WteExpression parser;

	@Before
	public void init() {
		parser = new WteExpression();
	}

	void assertParser(String contentKey, String formatterName, int argSize) {
		assertEquals(contentKey, parser.getContentKey());
		assertEquals(formatterName, parser.getFormattername());
		assertEquals(argSize, parser.getFormatterArgs().size());
	}

	@Test
	public void simpleExpression() {
		parser.parse("format:formatter");
		assertParser("", "formatter", 0);
	}

	@Test
	public void parseExpressionWithNoArgs() {
		parser.parse("format:formatter gugus");
		assertParser("gugus", "formatter", 0);

	}

	@Test
	public void parseeExpressionWithArgs() {
		parser.parse("format:formatter(arg1, arg2,arg3) gugus");
		assertParser("gugus", "formatter", 3);
		assertEquals("arg1", parser.getFormatterArgs().get(0));
		assertEquals("arg2", parser.getFormatterArgs().get(1));
		assertEquals("arg3", parser.getFormatterArgs().get(2));
	}

	@Test
	public void parseeExpressionWithEmptyArgs() {
		parser.parse("format:formatter() gugus");
		assertParser("gugus", "formatter", 0);
	}

	@Test
	public void parseNonFormatter() {
		parser.parse("test:gugus");
		assertParser("test:gugus", null, 0);
	}

}
