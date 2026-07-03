package org.nezxenka.litebinds.command;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.nezxenka.litebinds.database.DatabaseManager;
import org.nezxenka.litebinds.menu.BindsMenu;
import org.nezxenka.litebinds.menu.builder.InventoryLayoutBuilder;

public class BindsCommandExecutor implements org.bukkit.command.CommandExecutor {

    private final DatabaseManager databaseManager;
    private final InventoryLayoutBuilder layoutBuilder;

    public BindsCommandExecutor(DatabaseManager databaseManager, InventoryLayoutBuilder layoutBuilder) {
        this.databaseManager = databaseManager;
        this.layoutBuilder = layoutBuilder;
    }

    @Override
    public boolean onCommand(
        @NotNull CommandSender sender,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    ) {
        if (sender instanceof Player player) {
            new BindsMenu(databaseManager, layoutBuilder).setPlayer(player).compile().open();
        }
        return true;
    }
}
