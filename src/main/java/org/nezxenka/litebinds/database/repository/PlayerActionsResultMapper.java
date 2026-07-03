package org.nezxenka.litebinds.database.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.nezxenka.litebinds.model.ActionType;
import org.nezxenka.litebinds.model.PlayerActions;

public class PlayerActionsResultMapper {

    public PlayerActions map(ResultSet resultSet) throws SQLException {
        PlayerActions actions = new PlayerActions();
        actions.setPlayer(resultSet.getString("player"));
        actions.setActionDrop(ActionType.valueOf(resultSet.getString("action_drop")));
        actions.setActionSwap(ActionType.valueOf(resultSet.getString("action_swap")));
        actions.setActionInteract(ActionType.valueOf(resultSet.getString("action_interact")));
        return actions;
    }
}
