package org.nezxenka.litebinds.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

public class ColorParser {

    static final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6})");
    private final ServerVersionChecker versionChecker;

    public ColorParser(ServerVersionChecker versionChecker) {
        this.versionChecker = versionChecker;
    }

    public String color(String message) {
        if (message == null) return "";

        if (versionChecker.supportsRgb()) {
            message = applyHexColors(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String stripColor(String text) {
        if (text == null) return "";

        String result = text;
        if (versionChecker.supportsRgb()) {
            result = HEX_PATTERN.matcher(result).replaceAll("");
        }

        return ChatColor.stripColor(result);
    }

    private String applyHexColors(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuilder buffer = new StringBuilder();

        while (matcher.find()) {
            String hex = matcher.group(1);
            try {
                String replacement = ChatColor.of("#" + hex).toString();
                matcher.appendReplacement(buffer, replacement);
            } catch (Exception e) {
                matcher.appendReplacement(buffer, "");
            }
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
