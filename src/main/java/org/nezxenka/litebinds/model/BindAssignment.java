package org.nezxenka.litebinds.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BindAssignment {

    private ActionType actionType;
    private AssignmentSlot assignedSlot;

    public enum AssignmentSlot {
        NONE,
        DROP,
        SWAP,
        INTERACT
    }
}
