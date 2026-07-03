package org.nezxenka.litebinds.database;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.nezxenka.litebinds.database.repository.PlayerActionsRepository;
import org.nezxenka.litebinds.model.ActionType;
import org.nezxenka.litebinds.model.PlayerActions;

public class DatabaseManager {

    private final DatabaseConnectionPool connectionPool;
    private final PlayerActionsRepository playerActionsRepository;

    public DatabaseManager(
        DatabaseConnectionPool connectionPool,
        PlayerActionsRepository playerActionsRepository
    ) {
        this.connectionPool = connectionPool;
        this.playerActionsRepository = playerActionsRepository;
    }

    public CompletableFuture<Void> savePlayerActions(PlayerActions playerActions) {
        return playerActionsRepository.save(playerActions);
    }

    public CompletableFuture<Optional<PlayerActions>> getPlayerActions(String player) {
        return playerActionsRepository.findByPlayer(player);
    }

    public CompletableFuture<Void> deletePlayerActions(String player) {
        return playerActionsRepository.deleteByPlayer(player);
    }

    public CompletableFuture<Boolean> playerExists(String player) {
        return playerActionsRepository.existsByPlayer(player);
    }

    public CompletableFuture<Void> updateActionDrop(String player, ActionType actionDrop) {
        return playerActionsRepository.updateActionDrop(player, actionDrop);
    }

    public CompletableFuture<Void> updateActionSwap(String player, ActionType actionSwap) {
        return playerActionsRepository.updateActionSwap(player, actionSwap);
    }

    public CompletableFuture<Void> updateActionInteract(String player, ActionType actionInteract) {
        return playerActionsRepository.updateActionInteract(player, actionInteract);
    }

    public void close() {
        connectionPool.close();
    }
}
