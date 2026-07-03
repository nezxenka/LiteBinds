package org.nezxenka.litebinds.action.handler;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.nezxenka.litebinds.action.ActionContext;
import org.nezxenka.litebinds.action.ActionHandler;

public class BackpackHandler extends HolyBackpackHandler implements ActionHandler {

    @Override
    public void handle(ActionContext context) {
        ItemStack itemStack = findBackpackInInventory(context.getPlayer(), 4);
        if (itemStack == null) {
            return;
        }

        context.getEvent().setCancelled(true);

        Bukkit.getServer().getPluginManager().callEvent(
            new PlayerInteractEvent(
                context.getPlayer(),
                Action.RIGHT_CLICK_AIR,
                itemStack,
                null,
                BlockFace.DOWN
            )
        );
        context.markHandled();
    }
}
