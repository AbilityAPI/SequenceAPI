package com.abilityapi.sequenceapi.action.type.observe;

import com.abilityapi.sequenceapi.action.Action;

public abstract class ObserverActionBlueprint<T> {

    public ObserverAction<T> create() {
        return new ObserverAction<>();
    }

}
