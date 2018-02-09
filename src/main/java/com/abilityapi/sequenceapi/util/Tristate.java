package com.abilityapi.sequenceapi.util;

public enum Tristate {

    TRUE,
    FALSE,
    UNDEFINED;

    Tristate() {}

    public static Tristate from(Boolean value) {
        if (value == null) return Tristate.UNDEFINED;
        return value ? Tristate.TRUE : Tristate.FALSE;
    }
}
