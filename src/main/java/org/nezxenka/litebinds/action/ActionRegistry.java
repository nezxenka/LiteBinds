package org.nezxenka.litebinds.action;

import java.util.EnumMap;
import java.util.Map;
import org.nezxenka.litebinds.model.ActionType;

public class ActionRegistry {

    private final Map<ActionType, ActionHandler> handlers = new EnumMap<>(ActionType.class);

    public void register(ActionType type, ActionHandler handler) {
        handlers.put(type, handler);
    }

    public ActionHandler get(ActionType type) {
        return handlers.get(type);
    }
}
