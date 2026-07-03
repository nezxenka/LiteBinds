package org.nezxenka.litebinds.config;

import java.util.Map;
import lombok.Data;

@Data
public class PluginConfig {

    private final DatabaseConfig database;
    private final GuiConfig gui;
    private final Map<String, String> messages;
}
