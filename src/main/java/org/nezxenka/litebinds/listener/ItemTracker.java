package org.nezxenka.litebinds.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.nezxenka.litebinds.LiteBinds;

public class ItemTracker {

    private final Map<UUID, ItemStack> lastHeldItems = new HashMap<>();

    public ItemTracker() {
        Bukkit.getScheduler().runTaskTimer(
            LiteBinds.getInstance(),
            () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ItemStack newItem = player.getInventory().getItemInHand();
                    Bukkit.getScheduler().runTaskLater(
                        LiteBinds.getInstance(),
                        () -> {
                            lastHeldItems.put(
                                player.getUniqueId(),
                                newItem.clone()
                            );
                        },
                        1
                    );
                }
            },
            0,
            1
        );
    }

    public ItemStack getLastHeldItem(UUID uuid) {
        return lastHeldItems.get(uuid);
    }
}
