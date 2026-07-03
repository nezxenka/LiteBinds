package org.nezxenka.litebinds.database.repository;

public class PlayerActionsQueryProvider {

    private final boolean mysql;

    public PlayerActionsQueryProvider(boolean mysql) {
        this.mysql = mysql;
    }

    public String getUpsertQuery() {
        if (mysql) {
            return """
                INSERT INTO player_actions (player, action_drop, action_swap, action_interact)
                VALUES (?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                action_drop = VALUES(action_drop),
                action_swap = VALUES(action_swap),
                action_interact = VALUES(action_interact)
                """;
        } else {
            return """
                INSERT INTO player_actions (player, action_drop, action_swap, action_interact)
                VALUES (?, ?, ?, ?)
                ON CONFLICT(player) DO UPDATE SET
                action_drop = excluded.action_drop,
                action_swap = excluded.action_swap,
                action_interact = excluded.action_interact
                """;
        }
    }

    public String getSelectQuery() {
        return "SELECT * FROM player_actions WHERE player = ?";
    }

    public String getDeleteQuery() {
        return "DELETE FROM player_actions WHERE player = ?";
    }

    public String getExistsQuery() {
        return "SELECT COUNT(*) FROM player_actions WHERE player = ?";
    }

    public String getUpdateActionDropQuery() {
        if (mysql) {
            return "INSERT INTO player_actions (player, action_drop) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE action_drop = VALUES(action_drop)";
        } else {
            return "INSERT INTO player_actions (player, action_drop, action_swap, action_interact) " +
                "VALUES (?, ?, 'NONE', 'NONE') " +
                "ON CONFLICT(player) DO UPDATE SET action_drop = excluded.action_drop";
        }
    }

    public String getUpdateActionSwapQuery() {
        if (mysql) {
            return "INSERT INTO player_actions (player, action_swap) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE action_swap = VALUES(action_swap)";
        } else {
            return "INSERT INTO player_actions (player, action_drop, action_swap, action_interact) " +
                "VALUES (?, 'NONE', ?, 'NONE') " +
                "ON CONFLICT(player) DO UPDATE SET action_swap = excluded.action_swap";
        }
    }

    public String getUpdateActionInteractQuery() {
        if (mysql) {
            return "INSERT INTO player_actions (player, action_interact) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE action_interact = VALUES(action_interact)";
        } else {
            return "INSERT INTO player_actions (player, action_drop, action_swap, action_interact) " +
                "VALUES (?, 'NONE', 'NONE', ?) " +
                "ON CONFLICT(player) DO UPDATE SET action_interact = excluded.action_interact";
        }
    }
}
