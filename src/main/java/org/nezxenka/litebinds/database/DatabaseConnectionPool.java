package org.nezxenka.litebinds.database;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionPool {

    private final HikariDataSource dataSource;

    public DatabaseConnectionPool(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public boolean isMysql() {
        return dataSource.getJdbcUrl().startsWith("jdbc:mysql");
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
