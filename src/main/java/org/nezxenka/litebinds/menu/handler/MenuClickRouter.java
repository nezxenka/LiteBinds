package org.nezxenka.litebinds.menu.handler;

import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.nezxenka.litebinds.LiteBinds;
import org.nezxenka.litebinds.database.DatabaseManager;
import org.nezxenka.litebinds.menu.BindsMenu;
import org.nezxenka.litebinds.menu.provider.ActionItemDataProvider;
import org.nezxenka.litebinds.model.ActionType;

public class MenuClickRouter {

    private final DatabaseManager databaseManager;
    private final BindAssignmentProcessor bindAssignmentProcessor;
    private final ActionItemDataProvider actionItemDataProvider;

    public MenuClickRouter(
        DatabaseManager databaseManager,
        BindAssignmentProcessor bindAssignmentProcessor,
        ActionItemDataProvider actionItemDataProvider
    ) {
        this.databaseManager = databaseManager;
        this.bindAssignmentProcessor = bindAssignmentProcessor;
        this.actionItemDataProvider = actionItemDataProvider;
    }

    public void route(InventoryClickEvent event, BindsMenu menu) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (slot == 22) {
            handleResetClick(player, menu);
            return;
        }

        ActionType actionType = actionItemDataProvider.getActionTypeForSlot(slot);
        if (actionType == null) {
            return;
        }

        boolean next = event.isLeftClick();
        if (next || event.isRightClick()) {
            handleAssignmentClick(player, menu, next, actionType);
        }
    }

    private void handleResetClick(Player player, BindsMenu menu) {
        CompletableFuture.allOf(
            databaseManager.updateActionDrop(player.getName(), ActionType.NONE),
            databaseManager.updateActionSwap(player.getName(), ActionType.NONE),
            databaseManager.updateActionInteract(player.getName(), ActionType.NONE)
        ).thenAccept(v -> {
            Bukkit.getScheduler().runTask(LiteBinds.getInstance(), () -> {
                menu.compile().open();
            });
        });
    }

    private void handleAssignmentClick(Player player, BindsMenu menu, boolean next, ActionType actionType) {
        bindAssignmentProcessor.processClick(player.getName(), next, actionType).thenAccept(v -> {
            Bukkit.getScheduler().runTask(LiteBinds.getInstance(), () -> {
                menu.compile().open();
            });
        });
    }
}
