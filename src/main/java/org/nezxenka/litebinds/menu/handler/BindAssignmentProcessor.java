package org.nezxenka.litebinds.menu.handler;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.nezxenka.litebinds.database.DatabaseManager;
import org.nezxenka.litebinds.model.ActionType;
import org.nezxenka.litebinds.model.PlayerActions;

public class BindAssignmentProcessor {

    private final DatabaseManager databaseManager;

    public BindAssignmentProcessor(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public CompletableFuture<Void> processClick(String playerName, boolean next, ActionType actionType) {
        return CompletableFuture.runAsync(() -> {
            try {
                Optional<PlayerActions> playerActionsOptional =
                    databaseManager.getPlayerActions(playerName).get();
                PlayerActions playerActions = playerActionsOptional.orElseGet(
                    () -> new PlayerActions(playerName, ActionType.NONE, ActionType.NONE, ActionType.NONE)
                );
                ActionType currentDrop = playerActions.getActionDrop();
                ActionType currentSwap = playerActions.getActionSwap();
                ActionType currentInteract = playerActions.getActionInteract();

                if (next) {
                    processNextAssignment(playerActions, actionType, currentDrop, currentSwap, currentInteract);
                } else {
                    processPreviousAssignment(playerActions, actionType, currentDrop, currentSwap, currentInteract);
                }

                databaseManager.savePlayerActions(playerActions).get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void processNextAssignment(
        PlayerActions actions, ActionType actionType,
        ActionType currentDrop, ActionType currentSwap, ActionType currentInteract
    ) {
        if (actionType == currentDrop) {
            if (currentSwap == ActionType.NONE) {
                actions.setActionDrop(ActionType.NONE);
                actions.setActionSwap(actionType);
            } else if (currentInteract == ActionType.NONE) {
                actions.setActionDrop(ActionType.NONE);
                actions.setActionInteract(actionType);
            } else {
                actions.setActionDrop(ActionType.NONE);
            }
        } else if (actionType == currentSwap) {
            if (currentInteract == ActionType.NONE) {
                actions.setActionSwap(ActionType.NONE);
                actions.setActionInteract(actionType);
            } else {
                actions.setActionSwap(ActionType.NONE);
            }
        } else if (actionType == currentInteract) {
            actions.setActionInteract(ActionType.NONE);
        } else {
            if (currentDrop == ActionType.NONE) {
                actions.setActionDrop(actionType);
            } else if (currentSwap == ActionType.NONE) {
                actions.setActionSwap(actionType);
            } else if (currentInteract == ActionType.NONE) {
                actions.setActionInteract(actionType);
            }
        }
    }

    private void processPreviousAssignment(
        PlayerActions actions, ActionType actionType,
        ActionType currentDrop, ActionType currentSwap, ActionType currentInteract
    ) {
        if (actionType == currentDrop) {
            actions.setActionDrop(ActionType.NONE);
        } else if (actionType == currentSwap) {
            if (currentDrop == ActionType.NONE) {
                actions.setActionSwap(ActionType.NONE);
                actions.setActionDrop(actionType);
            } else {
                actions.setActionSwap(ActionType.NONE);
            }
        } else if (actionType == currentInteract) {
            if (currentSwap == ActionType.NONE) {
                actions.setActionInteract(ActionType.NONE);
                actions.setActionSwap(actionType);
            } else if (currentDrop == ActionType.NONE) {
                actions.setActionInteract(ActionType.NONE);
                actions.setActionDrop(actionType);
            } else {
                actions.setActionInteract(ActionType.NONE);
            }
        } else {
            if (currentInteract == ActionType.NONE) {
                actions.setActionInteract(actionType);
            } else if (currentSwap == ActionType.NONE) {
                actions.setActionSwap(actionType);
            } else if (currentDrop == ActionType.NONE) {
                actions.setActionDrop(actionType);
            }
        }
    }
}
