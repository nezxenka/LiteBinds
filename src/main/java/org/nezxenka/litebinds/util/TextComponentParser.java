package org.nezxenka.litebinds.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import net.md_5.bungee.api.ChatColor;
import static org.nezxenka.litebinds.util.ColorParser.HEX_PATTERN;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class TextComponentParser {

    private final ColorParser colorParser;

    public TextComponentParser(ColorParser colorParser) {
        this.colorParser = colorParser;
    }

    public BaseComponent[] parse(String message) {
        List<BaseComponent> components = new ArrayList<>();

        if (message == null) return new BaseComponent[0];

        message = ChatColor.translateAlternateColorCodes('&', message);

        StringBuilder builder = new StringBuilder();
        ChatColor currentColor = null;
        boolean bold = false;
        boolean italic = false;
        boolean underlined = false;
        boolean strikethrough = false;
        boolean obfuscated = false;

        int lastIndex = 0;

        Matcher matcher = HEX_PATTERN.matcher(message);

        while (matcher.find()) {
            if (matcher.start() > lastIndex) {
                String preceding = message.substring(lastIndex, matcher.start());
                builder.append(preceding);
            }

            String hex = matcher.group(1);
            try {
                currentColor = ChatColor.of("#" + hex);
            } catch (Exception e) {
                currentColor = null;
            }

            lastIndex = matcher.end();
        }

        builder.append(message.substring(lastIndex));

        if (!builder.isEmpty()) {
            TextComponent comp = new TextComponent(builder.toString());
            applyFormatting(comp, bold, italic, underlined, strikethrough, obfuscated, currentColor);
            components.add(comp);
        }

        return components.toArray(new BaseComponent[0]);
    }

    private void applyFormatting(
        TextComponent component,
        boolean bold,
        boolean italic,
        boolean underlined,
        boolean strikethrough,
        boolean obfuscated,
        ChatColor color
    ) {
        if (bold) component.setBold(true);
        if (italic) component.setItalic(true);
        if (underlined) component.setUnderlined(true);
        if (strikethrough) component.setStrikethrough(true);
        if (obfuscated) component.setObfuscated(true);
        if (color != null) component.setColor(color);
    }
}
