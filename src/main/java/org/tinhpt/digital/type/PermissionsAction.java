package org.tinhpt.digital.type;

import lombok.Getter;

@Getter
public enum PermissionsAction {
    MANAGE("manage"),
    CREATE("create"),
    UPDATE("update"),
    READ("read"),
    DELETE("delete");

    private final String action;

    PermissionsAction(String action) {
        this.action = action;
    }

    public boolean canDo(PermissionsAction requiredAction) {
        if (this == MANAGE) return true;
        return this == requiredAction;
    }
}
