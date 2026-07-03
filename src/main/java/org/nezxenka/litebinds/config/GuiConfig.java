package org.nezxenka.litebinds.config;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.bukkit.Material;
import org.nezxenka.litebinds.model.ActionType;

@Data
public class GuiConfig {

    private final String title;
    private final int rows;
    private final ResetButtonConfig resetButton;
    private final List<GuiItemConfig> items;
    private final LoreConfig lore;

    @Data
    public static class ResetButtonConfig {

        private final Material material;
        private final int slot;
        private final String name;
        private final List<String> lore;
    }

    @Data
    public static class GuiItemConfig {

        private final ActionType action;
        private final String displayName;
        private final Material material;
        private final int slot;
    }

    @Data
    public static class LoreConfig {

        private final String sectionTitle;
        private final String activeColor;
        private final String inactiveColor;
        private final String inactiveOptionColor;
        private final Map<String, String> optionLabels;
        private final Map<String, List<String>> descriptions;

        public List<String> getDescriptions(String key) {
            return descriptions.getOrDefault(key, Collections.emptyList());
        }
    }
}
