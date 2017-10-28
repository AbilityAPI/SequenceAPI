package com.abilityapi.sequenceapi.action.type.observe;

public abstract class ObserverActionBlueprint<T> {

    public ObserverAction<T> create() {
        return new ObserverAction<>();
    }

}
