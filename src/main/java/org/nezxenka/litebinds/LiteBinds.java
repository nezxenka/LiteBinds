package org.nezxenka.litebinds;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.nezxenka.litebinds.bootstrap.PluginBootstrap;
import org.nezxenka.litebinds.database.DatabaseManager;
import org.nezxenka.litebinds.menu.BindsMenu;

@Getter
public final class LiteBinds extends JavaPlugin {

    @Getter
    private static LiteBinds instance;

    private PluginBootstrap bootstrap;

    @Override
    public void onEnable() {
        instance = this;
        bootstrap = new PluginBootstrap(this);
        bootstrap.boot();
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (
                player
                    .getOpenInventory()
                    .getTopInventory()
                    .getHolder() instanceof BindsMenu
            ) {
                player.closeInventory();
            }
        }
        bootstrap.getDatabaseManager().close();
    }

    public DatabaseManager getDatabaseManager() {
        return bootstrap.getDatabaseManager();
    }
}
