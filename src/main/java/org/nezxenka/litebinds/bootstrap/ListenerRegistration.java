package org.nezxenka.litebinds.bootstrap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.nezxenka.litebinds.LiteBinds;
import org.nezxenka.litebinds.listener.DropEventListener;
import org.nezxenka.litebinds.listener.InteractEventListener;
import org.nezxenka.litebinds.listener.ItemTracker;
import org.nezxenka.litebinds.listener.PlayerEventProcessor;
import org.nezxenka.litebinds.listener.SwapEventListener;
import org.nezxenka.litebinds.menu.BindsMenu;
import org.nezxenka.litebinds.menu.BindsMenuListener;

public class ListenerRegistration {

    private final LiteBinds plugin;
    private final PlayerEventProcessor eventProcessor;
    private final BindsMenuListener bindsMenuListener;

    public ListenerRegistration(
        LiteBinds plugin,
        PlayerEventProcessor eventProcessor,
        BindsMenuListener bindsMenuListener
    ) {
        this.plugin = plugin;
        this.eventProcessor = eventProcessor;
        this.bindsMenuListener = bindsMenuListener;
    }

    public void register() {
        ItemTracker itemTracker = new ItemTracker();

        bindsMenuListener.register();
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new DropEventListener(itemTracker, eventProcessor), plugin);
        pluginManager.registerEvents(new SwapEventListener(eventProcessor), plugin);
        pluginManager.registerEvents(new InteractEventListener(eventProcessor), plugin);
    }
}
