package org.nezxenka.litebinds.listener;

import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.nezxenka.litebinds.LiteBinds;
import org.nezxenka.litebinds.action.ActionExecutor;
import org.nezxenka.litebinds.database.DatabaseManager;
import org.nezxenka.litebinds.model.ActionType;
import org.nezxenka.litebinds.model.PlayerActions;

public class PlayerEventProcessor {

    private final DatabaseManager databaseManager;
    private final ActionExecutor actionExecutor;

    public PlayerEventProcessor(DatabaseManager databaseManager, ActionExecutor actionExecutor) {
        this.databaseManager = databaseManager;
        this.actionExecutor = actionExecutor;
    }

    public void processDrop(Player player, Cancellable event) {
        process(player, event, PlayerActions::getActionDrop);
    }

    public void processSwap(Player player, Cancellable event) {
        process(player, event, PlayerActions::getActionSwap);
    }

    public void processInteract(Player player, Cancellable event) {
        process(player, event, PlayerActions::getActionInteract);
    }

    @FunctionalInterface
    private interface ActionTypeExtractor {
        ActionType extract(PlayerActions actions);
    }

    private void process(Player player, Cancellable event, ActionTypeExtractor extractor) {
        try {
            Optional<PlayerActions> playerActionsOptional =
                databaseManager.getPlayerActions(player.getName()).get();
            PlayerActions playerActions = playerActionsOptional.orElseGet(
                () -> new PlayerActions(
                    player.getName(),
                    ActionType.NONE,
                    ActionType.NONE,
                    ActionType.NONE
                )
            );

            ActionType actionType = extractor.extract(playerActions);
            actionExecutor.execute(actionType, player, event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
