package org.nezxenka.litebinds.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSchemaInitializer {

    private final DatabaseConnectionPool connectionPool;

    public DatabaseSchemaInitializer(DatabaseConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void initialize() {
        String sql;
        if (connectionPool.isMysql()) {
            sql = """
                CREATE TABLE IF NOT EXISTS player_actions (
                    player VARCHAR(16) PRIMARY KEY,
                    action_drop VARCHAR(32) NOT NULL DEFAULT 'NONE',
                    action_swap VARCHAR(32) NOT NULL DEFAULT 'NONE',
                    action_interact VARCHAR(32) NOT NULL DEFAULT 'NONE'
                )
                """;
        } else {
            sql = """
                CREATE TABLE IF NOT EXISTS player_actions (
                    player TEXT PRIMARY KEY,
                    action_drop TEXT NOT NULL DEFAULT 'NONE',
                    action_swap TEXT NOT NULL DEFAULT 'NONE',
                    action_interact TEXT NOT NULL DEFAULT 'NONE'
                )
                """;
        }

        try (
            Connection connection = connectionPool.getConnection();
            Statement statement = connection.createStatement()
        ) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
