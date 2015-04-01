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
package org.wte4j.examples.showcase.server.hsql;

import java.nio.file.Path;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hsqldb.Database;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Bean to start and shutdown a HSQL Database in server mode. Name of the
 * database and location (directory) where to store the data must be set.
 *
 */
public class HsqlServerBean {
    private static final Logger logger = LoggerFactory.getLogger(HsqlServerBean.class);
    private final Path databaseDirectory;
    private final String databaseName;
    private Server hsqlServer;

    public HsqlServerBean(Path directory, String databaseName) {
        this.databaseDirectory = directory;
        this.databaseName = databaseName;
    }

    @PostConstruct
    public synchronized void startDatabase() {
        logger.info("try to start hsql server");
        if (hsqlServer == null) {
            hsqlServer = createServer();
            hsqlServer.start();
            logger.info("hsql server startet wiht database {} stored at {}", databaseName, databaseDirectory.toAbsolutePath().toString());
        }
        else {
            logger.warn("server is allready running");
        }
    }

    private Server createServer() {
        HsqlProperties p = new HsqlProperties();
        Path databaseFile = databaseDirectory.resolve(databaseName);
        p.setProperty("server.database.0", "file:" + databaseFile.toAbsolutePath().toString());
        p.setProperty("server.dbname.0", databaseName);
        try {
            Server newServer = new Server();
            newServer.setProperties(p);
            return newServer;
        } catch (Exception e) {
            throw new IllegalArgumentException("unable to setup hsql server", e);
        }
    }

    @PreDestroy
    public synchronized void stopDatabase() {
        logger.info("stopping hsql server");
        if (hsqlServer != null) {
            hsqlServer.shutdownCatalogs(Database.CLOSEMODE_NORMAL);
            hsqlServer = null;
            logger.info("hsql server stopped");
        } else {
            logger.warn("hsql server is not running");
        }
    }

    /**
     * create a new connection source as {@link DataSource} for this server
     * 
     * @return a new {@link DataSource} object for this server with pooled
     *         connections
     * 
     */
    public synchronized DataSource createDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:hsqldb:hsql://localhost/" + databaseName);
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUsername("sa");
        return dataSource;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public Path getDatabaseDirectory() {
        return databaseDirectory;
    }
}
