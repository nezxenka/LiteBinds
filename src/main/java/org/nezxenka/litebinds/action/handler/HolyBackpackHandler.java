package org.nezxenka.litebinds.action.handler;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public abstract class HolyBackpackHandler {

    protected static final String PLUGIN_NAME = "HolyBackPack";
    protected static final String PDC_KEY = "backpack-level";

    protected ItemStack findBackpackInInventory(Player player, int requiredLevel) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
        if (plugin == null) {
            return null;
        }

        NamespacedKey key = new NamespacedKey(plugin, PDC_KEY);
        for (ItemStack itemStack : player.getInventory()) {
            if (itemStack != null && itemStack.hasItemMeta()) {
                int level = itemStack.getItemMeta()
                    .getPersistentDataContainer()
                    .getOrDefault(key, PersistentDataType.INTEGER, -1);
                if (level == requiredLevel) {
                    return itemStack;
                }
            }
        }
        return null;
    }
}
