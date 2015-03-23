package ch.born.wte.service;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.web.context.request.RequestScope;

public class TestWebContextPreparationListener extends AbstractTestExecutionListener {

    private static final String SCOPE_SESSION = "session";
    private static final String SCOPE_REQUEST = "request";

    @Override
    public void prepareTestInstance(final TestContext testContext) throws Exception {

        if (testContext.getApplicationContext() instanceof GenericApplicationContext) {
            GenericApplicationContext context = (GenericApplicationContext) testContext.getApplicationContext();
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            registerRequestScope(beanFactory);
            registerSessionScope(beanFactory);
        }
    }

    private void registerRequestScope(final ConfigurableListableBeanFactory beanFactory) {
        Scope requestScope = new RequestScope();
        beanFactory.registerScope(SCOPE_REQUEST, requestScope);
    }

    private void registerSessionScope(final ConfigurableListableBeanFactory beanFactory) {

        // ignore warning about destruction callbacks!
        Scope sessionScope = new SimpleThreadScope();
        beanFactory.registerScope(SCOPE_SESSION, sessionScope);
    }
}