package org.nezxenka.litebinds.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {

    private final HikariDataSource dataSource;
    private final boolean mysql;

    public DatabaseManager(
        String type,
        String host,
        int port,
        String user,
        String password,
        String database,
        String sqliteFile
    ) {
        this.mysql = type.equalsIgnoreCase("mysql");
        HikariConfig config = new HikariConfig();

        if (mysql) {
            config.setJdbcUrl(
                "jdbc:mysql://" +
                    host +
                    ":" +
                    port +
                    "/" +
                    database +
                    "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
            );
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setUsername(user);
            config.setPassword(password);

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
            config.setJdbcUrl("jdbc:sqlite:" + sqliteFile);
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

        this.dataSource = new HikariDataSource(config);
        initializeDatabase();
    }

    private void initializeDatabase() {
        String sql;
        if (mysql) {
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
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()
        ) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public CompletableFuture<Void> savePlayerActions(
        PlayerActions playerActions
    ) {
        return CompletableFuture.runAsync(() -> {
            String sql;
            if (mysql) {
                sql = """
                    INSERT INTO player_actions (player, action_drop, action_swap, action_interact)
                    VALUES (?, ?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                    action_drop = VALUES(action_drop),
                    action_swap = VALUES(action_swap),
                    action_interact = VALUES(action_interact)
                    """;
            } else {
                sql = """
                    INSERT INTO player_actions (player, action_drop, action_swap, action_interact)
                    VALUES (?, ?, ?, ?)
                    ON CONFLICT(player) DO UPDATE SET
                    action_drop = excluded.action_drop,
                    action_swap = excluded.action_swap,
                    action_interact = excluded.action_interact
                    """;
            }

            try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setString(1, playerActions.getPlayer());
                statement.setString(2, playerActions.getActionDrop().name());
                statement.setString(3, playerActions.getActionSwap().name());
                statement.setString(
                    4,
                    playerActions.getActionInteract().name()
                );

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save player actions", e);
            }
        });
    }

    public CompletableFuture<Optional<PlayerActions>> getPlayerActions(
        String player
    ) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM player_actions WHERE player = ?";

            try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setString(1, player);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        PlayerActions actions = new PlayerActions();
                        actions.setPlayer(resultSet.getString("player"));
                        actions.setActionDrop(
                            ActionType.valueOf(
                                resultSet.getString("action_drop")
                            )
                        );
                        actions.setActionSwap(
                            ActionType.valueOf(
                                resultSet.getString("action_swap")
                            )
                        );
                        actions.setActionInteract(
                            ActionType.valueOf(
                                resultSet.getString("action_interact")
                            )
                        );

                        return Optional.of(actions);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to get player actions", e);
            }

            return Optional.empty();
        });
    }

    public CompletableFuture<Void> deletePlayerActions(String player) {
        return CompletableFuture.runAsync(() -> {
            String sql = "DELETE FROM player_actions WHERE player = ?";

            try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setString(1, player);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(
                    "Failed to delete player actions",
                    e
                );
            }
        });
    }

    public CompletableFuture<Boolean> playerExists(String player) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT COUNT(*) FROM player_actions WHERE player = ?";

            try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setString(1, player);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(
                    "Failed to check if player exists",
                    e
                );
            }

            return false;
        });
    }

    public CompletableFuture<Void> updateActionDrop(
        String player,
        ActionType actionDrop
    ) {
        return CompletableFuture.runAsync(() -> {
            String sql;
            if (mysql) {
                sql =
                    "INSERT INTO player_actions (player, action_drop) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE action_drop = VALUES(action_drop)";
            } else {
                sql =
                    "INSERT INTO player_actions (player, action_drop, action_swap, action_interact) " +
                    "VALUES (?, ?, 'NONE', 'NONE') " +
                    "ON CONFLICT(player) DO UPDATE SET action_drop = excluded.action_drop";
            }

            try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setString(1, player);
                statement.setString(2, actionDrop.name());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update action drop", e);
            }
        });
    }

    public CompletableFuture<Void> updateActionSwap(
        String player,
        ActionType actionSwap
    ) {
        return CompletableFuture.runAsync(() -> {
            String sql;
            if (mysql) {
                sql =
                    "INSERT INTO player_actions (player, action_swap) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE action_swap = VALUES(action_swap)";
            } else {
                sql =
                    "INSERT INTO player_actions (player, action_drop, action_swap, action_interact) " +
                    "VALUES (?, 'NONE', ?, 'NONE') " +
                    "ON CONFLICT(player) DO UPDATE SET action_swap = excluded.action_swap";
            }

            try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setString(1, player);
                statement.setString(2, actionSwap.name());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update action swap", e);
            }
        });
    }

    public CompletableFuture<Void> updateActionInteract(
        String player,
        ActionType actionInteract
    ) {
        return CompletableFuture.runAsync(() -> {
            String sql;
            if (mysql) {
                sql =
                    "INSERT INTO player_actions (player, action_interact) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE action_interact = VALUES(action_interact)";
            } else {
                sql =
                    "INSERT INTO player_actions (player, action_drop, action_swap, action_interact) " +
                    "VALUES (?, 'NONE', 'NONE', ?) " +
                    "ON CONFLICT(player) DO UPDATE SET action_interact = excluded.action_interact";
            }

            try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setString(1, player);
                statement.setString(2, actionInteract.name());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(
                    "Failed to update action interact",
                    e
                );
            }
        });
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
