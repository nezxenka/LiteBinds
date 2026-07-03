package org.nezxenka.litebinds.menu.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.nezxenka.litebinds.config.GuiConfig.LoreConfig;
import org.nezxenka.litebinds.menu.provider.ActionItemDataProvider;
import org.nezxenka.litebinds.model.ActionType;
import org.nezxenka.litebinds.model.PlayerActions;
import org.nezxenka.litebinds.util.ColorParser;

public class LoreBuilder {

    private final ColorParser colorParser;
    private final ActionItemDataProvider actionItemDataProvider;
    private final LoreConfig loreConfig;

    public LoreBuilder(
        ColorParser colorParser,
        ActionItemDataProvider actionItemDataProvider,
        LoreConfig loreConfig
    ) {
        this.colorParser = colorParser;
        this.actionItemDataProvider = actionItemDataProvider;
        this.loreConfig = loreConfig;
    }

    public List<String> build(
        ActionType itemActionType,
        PlayerActions playerActions,
        int itemIndex
    ) {
        List<String> lore = new ArrayList<>();
        String displayName = actionItemDataProvider.getDisplayName(itemIndex);

        lore.add(colorParser.color(""));
        lore.add(colorParser.color(loreConfig.getSectionTitle()));

        String descKey;
        if (itemActionType == playerActions.getActionDrop()) {
            descKey = "drop";
        } else if (itemActionType == playerActions.getActionSwap()) {
            descKey = "swap";
        } else if (itemActionType == playerActions.getActionInteract()) {
            descKey = "interact";
        } else {
            descKey = "none";
        }

        appendOptionLine(lore, "disabled", "none".equals(descKey));
        appendOptionLine(lore, "drop", "drop".equals(descKey));
        appendOptionLine(lore, "swap", "swap".equals(descKey));
        appendOptionLine(lore, "interact", "interact".equals(descKey));

        lore.add(colorParser.color(""));

        List<String> descriptionTemplate = loreConfig.getDescriptions(descKey);
        for (String line : descriptionTemplate) {
            lore.add(
                colorParser.color(line.replace("{displayName}", displayName))
            );
        }

        return lore;
    }

    private void appendOptionLine(
        List<String> lore,
        String optionKey,
        boolean active
    ) {
        Map<String, String> labels = loreConfig.getOptionLabels();
        String label = labels.getOrDefault(optionKey, optionKey);

        if (active) {
            lore.add(
                colorParser.color(
                    "   " + loreConfig.getActiveColor() + "● " + label
                )
            );
        } else {
            lore.add(
                colorParser.color(
                    "   " +
                        loreConfig.getInactiveColor() +
                        "● " +
                        loreConfig.getInactiveOptionColor() +
                        label
                )
            );
        }
    }
}
