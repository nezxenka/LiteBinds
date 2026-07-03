package org.nezxenka.litebinds.menu.builder;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.nezxenka.litebinds.config.GuiConfig.ResetButtonConfig;
import org.nezxenka.litebinds.menu.provider.ActionItemDataProvider;
import org.nezxenka.litebinds.menu.provider.ResetItemProvider;
import org.nezxenka.litebinds.model.PlayerActions;
import org.nezxenka.litebinds.util.ColorParser;

public class InventoryLayoutBuilder {

    private final ColorParser colorParser;
    private final String title;
    private final int rows;
    private final ActionItemDataProvider actionItemDataProvider;
    private final MenuItemBuilder menuItemBuilder;
    private final ResetItemProvider resetItemProvider;
    private final ResetButtonConfig resetButtonConfig;

    public InventoryLayoutBuilder(
        ColorParser colorParser,
        String title,
        int rows,
        ActionItemDataProvider actionItemDataProvider,
        MenuItemBuilder menuItemBuilder,
        ResetItemProvider resetItemProvider,
        ResetButtonConfig resetButtonConfig
    ) {
        this.colorParser = colorParser;
        this.title = title;
        this.rows = rows;
        this.actionItemDataProvider = actionItemDataProvider;
        this.menuItemBuilder = menuItemBuilder;
        this.resetItemProvider = resetItemProvider;
        this.resetButtonConfig = resetButtonConfig;
    }

    public Inventory build(
        InventoryHolder holder,
        PlayerActions playerActions
    ) {
        Inventory inventory = Bukkit.createInventory(
            holder,
            rows * 9,
            colorParser.color(title)
        );

        for (int i = 0; i < actionItemDataProvider.getCount(); i++) {
            ItemStack item = menuItemBuilder.buildActionItem(i, playerActions);
            inventory.setItem(actionItemDataProvider.getSlot(i), item);
        }

        ItemStack resetItem = resetItemProvider.create();
        inventory.setItem(resetButtonConfig.getSlot(), resetItem);

        return inventory;
    }
}
