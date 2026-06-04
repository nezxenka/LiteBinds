package org.nezxenka.litebinds.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerActions {

    private String player;
    private ActionType actionDrop;
    private ActionType actionSwap;
    private ActionType actionInteract;
}
