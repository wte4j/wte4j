package ch.born.wte.impl.format;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import ch.born.wte.Formatter;
import ch.born.wte.impl.AnnotatedCustomFormatter;
import ch.born.wte.impl.IllegalDefaultFormatter;
import ch.born.wte.impl.Simple;
import ch.born.wte.impl.format.FormatterRegistry;
import ch.born.wte.impl.format.ToStringFormatter;

public class FormatterRegistryTest {

	@Test
	public void registerCostumFormatter() {
		FormatterRegistry registry = new FormatterRegistry();
		registry.registerClass(ToStringFormatter.class);
		assertEquals(
				ToStringFormatter.class,
				registry.createFormatter("ToStringFormatter",
						Collections.<String> emptyList()).getClass());
	}

	@Test
	public void registerAnnotatedFormatter() {
		final Class<AnnotatedCustomFormatter> formatterClass = AnnotatedCustomFormatter.class;
		FormatterRegistry registry = new FormatterRegistry();
		registry.registerClass(formatterClass);
		Formatter formatter = registry.createFormatter("custom",
				Collections.<String> emptyList());
		assertEquals(formatterClass, formatter.getClass());
		assertEquals(formatterClass,
				registry.createDefaultFormatter(Integer.class).getClass());
	}

	@Test(expected = IllegalArgumentException.class)
	public void registerIllegalDefaultFormatter() {
		final Class<IllegalDefaultFormatter> formatterClass = IllegalDefaultFormatter.class;
		FormatterRegistry registry = new FormatterRegistry();
		registry.registerClass(formatterClass);
	}

	@Test
	public void twoArgFormatter() {
		TwoArgFormatter formatter = (TwoArgFormatter) FormatterRegistry
				.createFormatter(TwoArgFormatter.class, Arrays.asList("a", "b"));
		assertEquals("a", formatter.arg1);
		assertEquals("b", formatter.arg2);
	}

	@Test
	public void enumArgFormatter() {
		EnumArgFormatter formatter = (EnumArgFormatter) FormatterRegistry
				.createFormatter(EnumArgFormatter.class,
						Arrays.asList("VALUE_1"));
		assertEquals(Simple.VALUE_1, formatter.value);
	}

	@Test
	public void intArgFormatter() {
		IntArgFormatter formatter = (IntArgFormatter) FormatterRegistry
				.createFormatter(IntArgFormatter.class, Arrays.asList("23"));
		assertEquals(23, formatter.value);
	}

}
