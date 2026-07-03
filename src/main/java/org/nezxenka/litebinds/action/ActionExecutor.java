package org.nezxenka.litebinds.action;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.nezxenka.litebinds.model.ActionType;

public class ActionExecutor {

    private final ActionRegistry registry;

    public ActionExecutor(ActionRegistry registry) {
        this.registry = registry;
    }

    public void execute(ActionType actionType, Player player, Cancellable event) {
        if (actionType == null || actionType == ActionType.NONE) {
            return;
        }

        ActionHandler handler = registry.get(actionType);
        if (handler != null) {
            ActionContext context = new ActionContext(player, event);
            handler.handle(context);
        }
    }
}
