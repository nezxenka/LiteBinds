package org.nezxenka.litebinds.util;

import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;

public class ClickableMessageSender {

    private final ColorParser colorParser;
    private final TextComponentParser textComponentParser;

    public ClickableMessageSender(ColorParser colorParser, TextComponentParser textComponentParser) {
        this.colorParser = colorParser;
        this.textComponentParser = textComponentParser;
    }

    public void send(Player player, List<String> lines, String url, String hover) {
        for (String line : lines) {
            hover = colorParser.color(hover);
            BaseComponent[] components = textComponentParser.parse(line);
            for (BaseComponent comp : components) {
                comp.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                comp.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(hover).create()
                ));
            }
            player.spigot().sendMessage(components);
        }
    }
}
