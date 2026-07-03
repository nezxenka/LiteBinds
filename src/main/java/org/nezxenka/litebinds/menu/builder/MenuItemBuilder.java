package org.nezxenka.litebinds.menu.builder;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.nezxenka.litebinds.menu.provider.ActionItemDataProvider;
import org.nezxenka.litebinds.model.PlayerActions;
import org.nezxenka.litebinds.util.ColorParser;

public class MenuItemBuilder {

    private final ColorParser colorParser;
    private final ActionItemDataProvider actionItemDataProvider;
    private final LoreBuilder loreBuilder;

    public MenuItemBuilder(ColorParser colorParser, ActionItemDataProvider actionItemDataProvider, LoreBuilder loreBuilder) {
        this.colorParser = colorParser;
        this.actionItemDataProvider = actionItemDataProvider;
        this.loreBuilder = loreBuilder;
    }

    public ItemStack buildActionItem(int index, PlayerActions playerActions) {
        String displayName = actionItemDataProvider.getDisplayName(index);
        ItemStack itemStack = new ItemStack(actionItemDataProvider.getMaterial(index));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(colorParser.color(displayName));
        itemMeta.setLore(loreBuilder.build(
            actionItemDataProvider.getActionType(index),
            playerActions,
            index
        ));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
