package org.nezxenka.litebinds.action.handler;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.nezxenka.litebinds.action.ActionContext;
import org.nezxenka.litebinds.action.ActionHandler;

public class SnowballHandler extends HolyLiteItemHandler implements ActionHandler {

    @Override
    public void handle(ActionContext context) {
        ItemStack itemStack = findItemInInventory(context.getPlayer(), "snowball");
        if (itemStack == null) {
            return;
        }

        context.getEvent().setCancelled(true);

        Snowball snowball = context.getPlayer().launchProjectile(Snowball.class);
        snowball.setItem(itemStack);
        snowball.setShooter(context.getPlayer());

        PlayerLaunchProjectileEvent ev = new PlayerLaunchProjectileEvent(
            context.getPlayer(),
            itemStack,
            snowball
        );
        Bukkit.getServer().getPluginManager().callEvent(ev);

        if (!ev.isCancelled()) {
            itemStack.setAmount(itemStack.getAmount() - 1);
        }
        context.markHandled();
    }
}
