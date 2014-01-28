package ch.born.wte.impl.expression;


public interface ValueResolverFactory {
	ValueResolver<String> createStringResolver(String expression)
			throws InvalidExpressionException;
}
