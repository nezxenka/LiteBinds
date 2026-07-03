package org.nezxenka.litebinds.database.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.nezxenka.litebinds.database.DatabaseConnectionPool;
import org.nezxenka.litebinds.model.ActionType;
import org.nezxenka.litebinds.model.PlayerActions;

public class PlayerActionsRepository {

    private final DatabaseConnectionPool connectionPool;
    private final PlayerActionsQueryProvider queryProvider;
    private final PlayerActionsResultMapper resultMapper;

    public PlayerActionsRepository(
        DatabaseConnectionPool connectionPool,
        PlayerActionsQueryProvider queryProvider,
        PlayerActionsResultMapper resultMapper
    ) {
        this.connectionPool = connectionPool;
        this.queryProvider = queryProvider;
        this.resultMapper = resultMapper;
    }

    public CompletableFuture<Void> save(PlayerActions playerActions) {
        return CompletableFuture.runAsync(() -> {
            String sql = queryProvider.getUpsertQuery();

            try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setString(1, playerActions.getPlayer());
                statement.setString(2, playerActions.getActionDrop().name());
                statement.setString(3, playerActions.getActionSwap().name());
                statement.setString(4, playerActions.getActionInteract().name());

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to save player actions", e);
            }
        });
    }

    public CompletableFuture<Optional<PlayerActions>> findByPlayer(String player) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = queryProvider.getSelectQuery();

            try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setString(1, player);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(resultMapper.map(resultSet));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to get player actions", e);
            }

            return Optional.empty();
        });
    }

    public CompletableFuture<Void> deleteByPlayer(String player) {
        return CompletableFuture.runAsync(() -> {
            String sql = queryProvider.getDeleteQuery();

            try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setString(1, player);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to delete player actions", e);
            }
        });
    }

    public CompletableFuture<Boolean> existsByPlayer(String player) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = queryProvider.getExistsQuery();

            try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setString(1, player);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) > 0;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to check if player exists", e);
            }

            return false;
        });
    }

    public CompletableFuture<Void> updateActionDrop(String player, ActionType actionDrop) {
        return CompletableFuture.runAsync(() -> {
            String sql = queryProvider.getUpdateActionDropQuery();

            try (
                Connection connection = connectionPool.getConnection();
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

    public CompletableFuture<Void> updateActionSwap(String player, ActionType actionSwap) {
        return CompletableFuture.runAsync(() -> {
            String sql = queryProvider.getUpdateActionSwapQuery();

            try (
                Connection connection = connectionPool.getConnection();
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

    public CompletableFuture<Void> updateActionInteract(String player, ActionType actionInteract) {
        return CompletableFuture.runAsync(() -> {
            String sql = queryProvider.getUpdateActionInteractQuery();

            try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ) {
                statement.setString(1, player);
                statement.setString(2, actionInteract.name());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update action interact", e);
            }
        });
    }
}
