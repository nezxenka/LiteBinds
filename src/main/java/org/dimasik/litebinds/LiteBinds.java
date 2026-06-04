package org.nezxenka.litebinds;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.nezxenka.litebinds.command.CommandExecutor;
import org.nezxenka.litebinds.database.DatabaseManager;
import org.nezxenka.litebinds.listeners.EventListener;
import org.nezxenka.litebinds.menu.listeners.MenuListener;
import org.nezxenka.litebinds.menu.menus.Menu;

@Getter
public final class LiteBinds extends JavaPlugin {

    @Getter
    private static LiteBinds instance;

    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        instance = this;
        setupDatabase();
        setupCommands();
        setupListeners();
    }

    private void setupDatabase() {
        super.saveDefaultConfig();
        FileConfiguration config = super.getConfig();
        databaseManager = new DatabaseManager(
            config.getString("database.type", "sqlite"),
            config.getString("mysql.host", "localhost"),
            config.getInt("mysql.port", 3306),
            config.getString("mysql.user", "root"),
            config.getString("mysql.password", ""),
            config.getString("mysql.database", "lite_binds"),
            config.getString("sqlite.file", "plugins/LiteBinds/binds.db")
        );
    }

    private void setupCommands() {
        var cmd = getCommand("binds");
        var cmdExec = new CommandExecutor();
        cmd.setExecutor(cmdExec);
        cmd.setTabCompleter(cmdExec);
    }

    private void setupListeners() {
        new MenuListener().register();
        PluginManager pluginManager = super.getServer().getPluginManager();
        pluginManager.registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (
                player
                        .getOpenInventory()
                        .getTopInventory()
                        .getHolder() instanceof
                    Menu
            ) {
                player.closeInventory();
            }
        }
        databaseManager.close();
    }
}
