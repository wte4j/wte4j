package ch.born.wte.impl.format;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.born.wte.FormatterFactory;
import ch.born.wte.impl.expression.ResolverFactoryImpl;
import ch.born.wte.impl.expression.ValueResolver;
import ch.born.wte.impl.expression.ValueResolverFactory;

/**
 * Ueberprueft, dass die Formatierungen von Zahlen via Formatierungsanweisung in
 * den Tags funktioniert.
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { FormatNumberExpressionTest.Config.class })
public class FormatNumberExpressionTest {

	private static final Map<String, Class<?>> ELEMENTS = Collections
			.<String, Class<?>> singletonMap("value", Number.class);

	private static final Locale LOCALE = Locale.GERMAN;

	@Autowired
	FormatterFactory formatterRegistry;

	public ValueResolverFactory templateContext() {
		return new ResolverFactoryImpl(LOCALE, formatterRegistry, ELEMENTS);
	}

	@Test()
	public void formateWithMoreDigitsTest() {
		ValueResolver<String> expression = templateContext()
				.createStringResolver("format:number(3) value");
		String formated = expression
				.resolve(new SingleValueDataModel(1000.5555));
		assertEquals("1000.556", formated);
	}

	@Test()
	public void formateWithLessDigitsTest() {
		ValueResolver<String> expression = templateContext()
				.createStringResolver("format:number(3) value");
		String formated = expression.resolve(new SingleValueDataModel(1000.55));
		assertEquals("1000.550", formated);
	}

	@Test()
	public void formateNoDigitsTest() {
		ValueResolver<String> expression = templateContext()
				.createStringResolver("format:number(0) value");
		String formated = expression.resolve(new SingleValueDataModel(1000.55));
		assertEquals("1001", formated);
	}

	@Test()
	public void formateGroupingTest() {
		ValueResolver<String> expression = templateContext()
				.createStringResolver("format:number(0, true) value");
		String formated = expression.resolve(new SingleValueDataModel(10000));
		assertEquals("10'000", formated);
	}

	@Test()
	public void formateNoGroupingTest() {
		ValueResolver<String> expression = templateContext()
				.createStringResolver("format:number(0, false) value");
		String formated = expression.resolve(new SingleValueDataModel(10000));
		assertEquals("10000", formated);
	}

	@Configuration
	public static class Config {
		@Bean
		public FormatterFactory formatterRegistry() {
			return new FormatterRegistry();
		}
	}

}
