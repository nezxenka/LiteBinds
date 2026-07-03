package org.nezxenka.litebinds.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.nezxenka.litebinds.menu.abstraction.AbstractMenuListener;
import org.nezxenka.litebinds.menu.handler.MenuClickRouter;

public class BindsMenuListener extends AbstractMenuListener {

    private final MenuClickRouter menuClickRouter;

    public BindsMenuListener(MenuClickRouter menuClickRouter) {
        this.menuClickRouter = menuClickRouter;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getView().getTopInventory();
        if (inventory.getHolder() instanceof BindsMenu menu) {
            event.setCancelled(true);
            if (
                event.getClickedInventory() == null ||
                event.getClickedInventory() != inventory
            ) {
                return;
            }

            menuClickRouter.route(event, menu);
        }
    }
}
