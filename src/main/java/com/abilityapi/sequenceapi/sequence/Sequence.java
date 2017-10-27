package com.abilityapi.sequenceapi.sequence;

import com.abilityapi.sequenceapi.action.flag.Flag;
import com.abilityapi.sequenceapi.origin.Origin;

public abstract class Sequence<T> {

    private State state = State.INACTIVE;

    public Sequence() {
        
    }

    public boolean applyObserve(final T event, final Origin origin) {
        return false;
    }

    public boolean applySchedule(final Origin origin) {
        return false;
    }

    public SequenceBlueprint<T> getBlueprint() {
        return null;
    }

    public Origin getOrigin() {
        return null;
    }

    public Class<? extends T> getNextActionClass() {
        return null;
    }

    public State getState() {
        return null;
    }

    enum State {
        ACTIVE(true),
        INACTIVE(true),
        CANCELLED(false),
        EXPIRED(false),
        FINISHED(true);

        private final boolean safe;

        State(boolean safe) {
            this.safe = safe;
        }

        public boolean isSafe() {
            return this.safe;
        }
    }
}
