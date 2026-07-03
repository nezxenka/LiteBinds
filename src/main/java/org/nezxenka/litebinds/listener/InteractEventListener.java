package org.nezxenka.litebinds.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractEventListener implements Listener {

    private final PlayerEventProcessor eventProcessor;

    public InteractEventListener(PlayerEventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    @EventHandler
    public void on(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (
            event.getAction() != Action.RIGHT_CLICK_AIR &&
            event.getAction() != Action.RIGHT_CLICK_BLOCK
        ) {
            return;
        }
        if (
            event.getItem() != null &&
            event.getItem().getType() == Material.NETHERITE_SWORD
        ) {
            eventProcessor.processInteract(player, event);
        }
    }
}
