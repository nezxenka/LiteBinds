package org.nezxenka.litebinds.action.handler;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public abstract class HolyLiteItemHandler {

    protected static final String PLUGIN_NAME = "HolyLiteItems";
    protected static final String PDC_KEY = "item-type";

    protected ItemStack findItemInInventory(Player player, String itemType) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(PLUGIN_NAME);
        if (plugin == null) {
            return null;
        }

        NamespacedKey key = new NamespacedKey(plugin, PDC_KEY);
        for (ItemStack itemStack : player.getInventory()) {
            if (itemStack != null && itemStack.hasItemMeta()) {
                String value = itemStack.getItemMeta()
                    .getPersistentDataContainer()
                    .getOrDefault(key, PersistentDataType.STRING, "none");
                if (value.equalsIgnoreCase(itemType)) {
                    return itemStack;
                }
            }
        }
        return null;
    }
}
