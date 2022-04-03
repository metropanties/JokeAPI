package me.metropanties.jokeapi.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.metropanties.jokeapi.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);

    private final String host;
    private final String username;
    private final String password;
    private final String database;
    private final int port;

    private volatile boolean isReady = false;
    private HikariDataSource dataSource;

    public Database(String host, String username, String password, String database, int port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
        this.port = port;
    }

    public void connect() {
        Server.getInstance().getExecutor().submit(() -> {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://" + host + ":" + port + "/" + database);
            config.setUsername(username);
            config.setPassword(password);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            this.dataSource = new HikariDataSource(config);
            setupTables();
            isReady = true;
            LOGGER.info("Successfully connected to database!");
        });
    }

    public void awaitReady() {
        while (!isReady) {
            Thread.onSpinWait();
        }
    }

    public void disconnect() {
        dataSource.close();
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private void setupTables() {
        String[] tables = {
                "CREATE TABLE IF NOT EXISTS jokes (id SERIAL PRIMARY KEY, joke VARCHAR NOT NULL);"
        };

        try {
            Connection connection = dataSource.getConnection();
            if (connection == null) {
                LOGGER.error("Failed loading tables, connection is null!");
                return;
            }

            for (String table : tables) {
                try (PreparedStatement statement = connection.prepareStatement(table)) {
                    statement.execute();
                    LOGGER.info("Executed: '{}'", table);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error occurred loading tables!", e);
        }
    }

}
