package ch.born.wte.servlet.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HSQLServerStartupListener implements ServletContextListener {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private Server server;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			String dbPath = sce.getServletContext().getRealPath(
					"/WEB-INF/hsql/demo");
			logger.info("dblocation: {}", dbPath);
			HsqlProperties p = new HsqlProperties();
			p.setProperty("server.database.0", "file:" + dbPath);
			p.setProperty("server.dbname.0", "demo");
			// set up the rest of properties

			// alternative to the above is
			Server newServer = new Server();
			newServer.setProperties(p);
			newServer.start();
			server = newServer;
		} catch (Exception e) {
			logger.error("error start hsql db", e);
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (server != null) {
			server.stop();
		}
	}

}
