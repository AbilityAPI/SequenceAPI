package com.abilityapi.sequenceapi.sequence;

import com.abilityapi.sequenceapi.action.flag.Flag;
import com.abilityapi.sequenceapi.origin.Origin;

public interface Sequence<T> {

    boolean applyObserve(final T event, final Origin origin);

    boolean applySchedule(final Origin origin);

    SequenceBlueprint<T> getBlueprint();

    Origin getOrigin();

    Class<? extends T> getNextActionClass();

    Flag getNextAction();

    State getState();

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
