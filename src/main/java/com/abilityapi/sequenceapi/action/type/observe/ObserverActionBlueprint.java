package com.abilityapi.sequenceapi.action.type.observe;

public class ObserverActionBlueprint<T> {

    private final Class<? extends T> eventClass;

    public ObserverActionBlueprint(Class<? extends T> eventClass) {
        this.eventClass = eventClass;
    }

    public ObserverAction<T> create() {
        return new ObserverAction<>(this.eventClass);
    }

}
