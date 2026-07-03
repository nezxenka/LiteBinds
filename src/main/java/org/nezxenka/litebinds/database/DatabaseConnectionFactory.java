package org.nezxenka.litebinds.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConnectionFactory {

    public HikariDataSource create(DatabaseCredentials credentials) {
        HikariConfig config = new HikariConfig();
        boolean mysql = credentials.getType() == DatabaseType.MYSQL;

        if (mysql) {
            config.setJdbcUrl(
                "jdbc:mysql://" +
                    credentials.getMysqlHost() +
                    ":" +
                    credentials.getMysqlPort() +
                    "/" +
                    credentials.getMysqlDatabase() +
                    "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
            );
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setUsername(credentials.getMysqlUser());
            config.setPassword(credentials.getMysqlPassword());

            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");
            config.addDataSourceProperty("useLocalSessionState", "true");
            config.addDataSourceProperty("rewriteBatchedStatements", "true");
            config.addDataSourceProperty("cacheResultSetMetadata", "true");
            config.addDataSourceProperty("cacheServerConfiguration", "true");
            config.addDataSourceProperty("elideSetAutoCommits", "true");
            config.addDataSourceProperty("maintainTimeStats", "false");
        } else {
            config.setJdbcUrl("jdbc:sqlite:" + credentials.getSqliteFile());
            config.setDriverClassName("org.sqlite.JDBC");

            config.addDataSourceProperty("journal_mode", "WAL");
            config.addDataSourceProperty("busy_timeout", "5000");
            config.addDataSourceProperty("synchronous", "NORMAL");
            config.addDataSourceProperty("cache_size", "10000");
            config.addDataSourceProperty("temp_store", "MEMORY");
            config.addDataSourceProperty("mmap_size", "268435456");
        }

        config.setMaximumPoolSize(mysql ? 10 : 1);
        config.setMinimumIdle(mysql ? 2 : 1);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        return new HikariDataSource(config);
    }
}
