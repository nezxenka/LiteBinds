package org.nezxenka.litebinds.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class DropEventListener implements Listener {

    private final ItemTracker itemTracker;
    private final PlayerEventProcessor eventProcessor;

    public DropEventListener(ItemTracker itemTracker, PlayerEventProcessor eventProcessor) {
        this.itemTracker = itemTracker;
        this.eventProcessor = eventProcessor;
    }

    @EventHandler
    public void on(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack droppedItem = event.getItemDrop().getItemStack();
        ItemStack lastHeldItem = itemTracker.getLastHeldItem(player.getUniqueId());

        if (
            lastHeldItem != null &&
            lastHeldItem.isSimilar(droppedItem) &&
            droppedItem.getType() == Material.NETHERITE_SWORD
        ) {
            eventProcessor.processDrop(player, event);
        }
    }
}
