package com.abilityapi.sequenceapi.action.type.observe;

/**
 * Represents a blueprint for an {@link ObserverAction}.
 *
 * @param <T> the event type
 */
public class ObserverActionBlueprint<T> {

    private final Class<? extends T> eventClass;

    public ObserverActionBlueprint(Class<? extends T> eventClass) {
        this.eventClass = eventClass;
    }

    /**
     * Creates a new {@link ObserverAction} from the blueprint.
     *
     * @return the new observer action
     */
    public ObserverAction<T> create() {
        return new ObserverAction<>(this.eventClass);
    }

}
