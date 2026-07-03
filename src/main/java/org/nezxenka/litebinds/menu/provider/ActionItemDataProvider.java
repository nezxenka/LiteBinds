package org.nezxenka.litebinds.menu.provider;

import java.util.List;
import org.bukkit.Material;
import org.nezxenka.litebinds.config.GuiConfig;
import org.nezxenka.litebinds.model.ActionType;

public class ActionItemDataProvider {

    private final List<GuiConfig.GuiItemConfig> items;

    public ActionItemDataProvider(GuiConfig guiConfig) {
        this.items = guiConfig.getItems();
    }

    public int getCount() {
        return items.size();
    }

    public String getDisplayName(int index) {
        return items.get(index).getDisplayName();
    }

    public ActionType getActionType(int index) {
        return items.get(index).getAction();
    }

    public Material getMaterial(int index) {
        return items.get(index).getMaterial();
    }

    public int getSlot(int index) {
        return items.get(index).getSlot();
    }

    public ActionType getActionTypeForSlot(int slot) {
        for (GuiConfig.GuiItemConfig item : items) {
            if (item.getSlot() == slot) {
                return item.getAction();
            }
        }
        return null;
    }
}
