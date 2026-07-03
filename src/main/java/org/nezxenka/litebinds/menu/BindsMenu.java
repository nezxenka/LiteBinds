package org.nezxenka.litebinds.menu;

import java.util.Optional;
import org.bukkit.Bukkit;
import org.nezxenka.litebinds.LiteBinds;
import org.nezxenka.litebinds.database.DatabaseManager;
import org.nezxenka.litebinds.menu.abstraction.AbstractMenu;
import org.nezxenka.litebinds.menu.builder.InventoryLayoutBuilder;
import org.nezxenka.litebinds.model.ActionType;
import org.nezxenka.litebinds.model.PlayerActions;

public class BindsMenu extends AbstractMenu {

    private final DatabaseManager databaseManager;
    private final InventoryLayoutBuilder layoutBuilder;

    public BindsMenu(DatabaseManager databaseManager, InventoryLayoutBuilder layoutBuilder) {
        this.databaseManager = databaseManager;
        this.layoutBuilder = layoutBuilder;
    }

    @Override
    public AbstractMenu compile() {
        try {
            Optional<PlayerActions> playerActionsOptional =
                databaseManager.getPlayerActions(viewer.getName()).get();
            PlayerActions playerActions = playerActionsOptional.orElseGet(
                () -> new PlayerActions(viewer.getName(), ActionType.NONE, ActionType.NONE, ActionType.NONE)
            );

            inventory = layoutBuilder.build(this, playerActions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }
}
