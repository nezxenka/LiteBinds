package org.nezxenka.litebinds.bootstrap;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.nezxenka.litebinds.command.BindsCommandExecutor;
import org.nezxenka.litebinds.command.BindsTabCompleter;

public class CommandRegistration {

    private final JavaPlugin plugin;
    private final BindsCommandExecutor executor;
    private final BindsTabCompleter tabCompleter;

    public CommandRegistration(JavaPlugin plugin, BindsCommandExecutor executor, BindsTabCompleter tabCompleter) {
        this.plugin = plugin;
        this.executor = executor;
        this.tabCompleter = tabCompleter;
    }

    public void register() {
        PluginCommand cmd = plugin.getCommand("binds");
        cmd.setExecutor(executor);
        cmd.setTabCompleter(tabCompleter);
    }
}
