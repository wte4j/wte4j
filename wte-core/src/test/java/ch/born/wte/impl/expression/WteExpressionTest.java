package ch.born.wte.impl.expression;

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
