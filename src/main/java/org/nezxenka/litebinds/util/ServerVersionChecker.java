package org.nezxenka.litebinds.util;

import org.bukkit.Bukkit;

public class ServerVersionChecker {

    private static final boolean SUPPORTS_RGB;

    static {
        String version = Bukkit.getServer()
            .getClass()
            .getPackage()
            .getName()
            .split("\\.")[3];
        int subVersion = Integer.parseInt(
            version.replace("v", "").replace("1_", "").replaceAll("_R\\d", "")
        );
        SUPPORTS_RGB = subVersion >= 16;
    }

    public boolean supportsRgb() {
        return SUPPORTS_RGB;
    }
}
