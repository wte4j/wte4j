package ch.born.wte.impl.expression;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ch.born.wte.ExpressionError;
import ch.born.wte.Formatter;
import ch.born.wte.FormatterFactory;
import ch.born.wte.FormatterInstantiationException;
import ch.born.wte.UnknownFormatterException;

public class ResolverFactoryImpl implements ValueResolverFactory {

	private Locale locale;
	private FormatterFactory formatterFactory;
	private Map<String, Class<?>> contentElements;

	public ResolverFactoryImpl(Locale locale,
			FormatterFactory formatterFactory,
			Map<String, Class<?>> contentElements) {
		super();
		this.locale = locale;
		this.formatterFactory = formatterFactory;
		this.contentElements = contentElements;
	}

	@Override
	public ValueResolver<String> createStringResolver(String expressionString)
			throws InvalidExpressionException {
		WteExpression expression = new WteExpression();
		expression.setExpressionString(expressionString);
		if (!contentElements.containsKey(expression.getContentKey())) {
			throw new InvalidExpressionException(
					ExpressionError.ILLEGAL_CONTENT_KEY);
		}

		Formatter formatter = createFormatter(expression);
		return new StringResolver(expression.getContentKey(), formatter);
	}

	Formatter createFormatter(WteExpression expression) {
		try {
			Formatter formatter = new DefaultFormatterProxy(formatterFactory);
			if (StringUtils.isNotEmpty(expression.getFormattername())) {
				formatter = formatterFactory.createFormatter(
						expression.getFormattername(),
						expression.getFormatterArgs());
			}
			formatter.setLocale(locale);
			return formatter;
		} catch (UnknownFormatterException e) {
			throw new InvalidExpressionException(
					ExpressionError.UNKNOWN_FORMATTER, e);
		} catch (FormatterInstantiationException e) {
			throw new InvalidExpressionException(
					ExpressionError.ILLEGALFORMATTER_DEFINITION, e);
		}
	}

}
