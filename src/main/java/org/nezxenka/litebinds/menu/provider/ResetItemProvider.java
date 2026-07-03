package org.nezxenka.litebinds.menu.provider;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.nezxenka.litebinds.config.GuiConfig.ResetButtonConfig;
import org.nezxenka.litebinds.util.ColorParser;

public class ResetItemProvider {

    private final ColorParser colorParser;
    private final ResetButtonConfig resetButtonConfig;

    public ResetItemProvider(
        ColorParser colorParser,
        ResetButtonConfig resetButtonConfig
    ) {
        this.colorParser = colorParser;
        this.resetButtonConfig = resetButtonConfig;
    }

    public ItemStack create() {
        ItemStack itemStack = new ItemStack(resetButtonConfig.getMaterial());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(colorParser.color(resetButtonConfig.getName()));

        List<String> lore = new ArrayList<>();
        for (String line : resetButtonConfig.getLore()) {
            lore.add(colorParser.color(line));
        }

        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addEnchant(Enchantment.SOUL_SPEED, 1, true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
