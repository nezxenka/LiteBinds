package org.nezxenka.litebinds.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class SwapEventListener implements Listener {

    private final PlayerEventProcessor eventProcessor;

    public SwapEventListener(PlayerEventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    @EventHandler
    public void on(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (
            event.getOffHandItem() != null &&
            event.getOffHandItem().getType() == Material.NETHERITE_SWORD
        ) {
            eventProcessor.processSwap(player, event);
        }
    }
}
