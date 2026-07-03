package org.nezxenka.litebinds.action;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

public class ActionContext {

    private final Player player;
    private final Cancellable event;
    private ItemStack foundItem;
    private boolean handled;

    public ActionContext(Player player, Cancellable event) {
        this.player = player;
        this.event = event;
        this.handled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public Cancellable getEvent() {
        return event;
    }

    public ItemStack getFoundItem() {
        return foundItem;
    }

    public void setFoundItem(ItemStack foundItem) {
        this.foundItem = foundItem;
    }

    public boolean isHandled() {
        return handled;
    }

    public void markHandled() {
        this.handled = true;
    }
}
