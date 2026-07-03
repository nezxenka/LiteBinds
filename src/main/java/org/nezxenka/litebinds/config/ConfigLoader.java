package org.nezxenka.litebinds.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.nezxenka.litebinds.model.ActionType;

public class ConfigLoader {

    public PluginConfig load(
        FileConfiguration config,
        FileConfiguration dbConfig
    ) {
        DatabaseConfig databaseConfig = loadDatabaseConfig(dbConfig);
        GuiConfig guiConfig = loadGuiConfig(config);
        Map<String, String> messages = loadMessages(config);
        return new PluginConfig(databaseConfig, guiConfig, messages);
    }

    private DatabaseConfig loadDatabaseConfig(FileConfiguration dbConfig) {
        return DatabaseConfig.builder()
            .type(dbConfig.getString("database.type", "sqlite"))
            .mysqlHost(dbConfig.getString("database.mysql.host", "localhost"))
            .mysqlPort(dbConfig.getInt("database.mysql.port", 3306))
            .mysqlUser(dbConfig.getString("database.mysql.user", "root"))
            .mysqlPassword(dbConfig.getString("database.mysql.password", ""))
            .mysqlDatabase(
                dbConfig.getString("database.mysql.database", "lite_binds")
            )
            .sqliteFile(
                dbConfig.getString(
                    "database.sqlite.file",
                    "plugins/LiteBinds/binds.db"
                )
            )
            .build();
    }

    private GuiConfig loadGuiConfig(FileConfiguration config) {
        ConfigurationSection gui = config.getConfigurationSection("gui");

        GuiConfig.ResetButtonConfig resetButton = loadResetButton(
            gui.getConfigurationSection("reset_button")
        );

        List<GuiConfig.GuiItemConfig> items = loadItems(
            gui.getConfigurationSection("items")
        );

        GuiConfig.LoreConfig lore = loadLoreConfig(
            gui.getConfigurationSection("lore")
        );

        return new GuiConfig(
            gui.getString("title", "Binds"),
            gui.getInt("rows", 3),
            resetButton,
            items,
            lore
        );
    }

    private GuiConfig.ResetButtonConfig loadResetButton(
        ConfigurationSection section
    ) {
        Material material = Material.getMaterial(
            section.getString("material", "RED_STAINED_GLASS_PANE")
        );
        return new GuiConfig.ResetButtonConfig(
            material != null ? material : Material.RED_STAINED_GLASS_PANE,
            section.getInt("slot", 22),
            section.getString("name", ""),
            section.getStringList("lore")
        );
    }

    private List<GuiConfig.GuiItemConfig> loadItems(
        ConfigurationSection itemsSection
    ) {
        List<GuiConfig.GuiItemConfig> items = new ArrayList<>();

        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection item = itemsSection.getConfigurationSection(
                key
            );
            ActionType action = ActionType.valueOf(key.toUpperCase());
            Material material = Material.getMaterial(
                item.getString("material", "BARRIER")
            );

            items.add(
                new GuiConfig.GuiItemConfig(
                    action,
                    item.getString("display_name", ""),
                    material != null ? material : Material.BARRIER,
                    item.getInt("slot", 0)
                )
            );
        }

        return items;
    }

    private GuiConfig.LoreConfig loadLoreConfig(
        ConfigurationSection loreSection
    ) {
        Map<String, String> optionLabels = new LinkedHashMap<>();
        ConfigurationSection labels = loreSection.getConfigurationSection(
            "option_labels"
        );
        for (String key : labels.getKeys(false)) {
            optionLabels.put(key, labels.getString(key));
        }

        Map<String, List<String>> descriptions = new LinkedHashMap<>();
        ConfigurationSection descSection = loreSection.getConfigurationSection(
            "descriptions"
        );
        for (String key : descSection.getKeys(false)) {
            descriptions.put(key.toLowerCase(), descSection.getStringList(key));
        }

        return new GuiConfig.LoreConfig(
            loreSection.getString("section_title", ""),
            loreSection.getString("active_color", "&6"),
            loreSection.getString("inactive_color", "&7"),
            loreSection.getString("inactive_option_color", "&7"),
            optionLabels,
            descriptions
        );
    }

    private Map<String, String> loadMessages(FileConfiguration config) {
        Map<String, String> messages = new HashMap<>();
        ConfigurationSection messagesSection = config.getConfigurationSection(
            "messages"
        );
        if (messagesSection != null) {
            for (String key : messagesSection.getKeys(false)) {
                messages.put(key, messagesSection.getString(key));
            }
        }
        return messages;
    }
}
