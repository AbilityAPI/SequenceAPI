package com.abilityapi.sequenceapi.sequence;

import com.abilityapi.sequenceapi.action.type.observe.ObserverAction;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleAction;
import com.abilityapi.sequenceapi.origin.Origin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Sequence<T> {

    private final Origin origin;
    private final SequenceBlueprint<T> sequenceBlueprint;
    private final List<ScheduleAction> scheduleActions = new ArrayList<>();
    private final List<ObserverAction<T>> observerActions = new ArrayList<>();

    private long lastExecutionTime = System.currentTimeMillis();
    private State state = State.INACTIVE;
    private int queue = 0;

    public Sequence(final Origin origin, final SequenceBlueprint<T> sequenceBlueprint) {
        this.origin = origin;
        this.sequenceBlueprint = sequenceBlueprint;
    }

    public boolean applyObserve(final T event, final Origin origin) {
        Iterator<ObserverAction<T>> iterator = this.observerActions.iterator();

        if (this.state.equals(State.INACTIVE)) this.state = State.ACTIVE;

        if (iterator.hasNext()) {
            ObserverAction<T> action = iterator.next();

            this.queue += 1;

            long current = System.currentTimeMillis();

            // 1. Check that the event is the correct one for this action.

            if (!action.getEventClass().equals(event.getClass())) return this.fail(action, origin);

            // 2. Fail the action if it is being executed before the delay.

            if (this.lastExecutionTime + ((action.getDelay() / 20) * 1000) > current) {
                return this.fail(action, origin);
            }

            // 3. Fail the action if it being executed after the expire.

            if (this.lastExecutionTime + ((action.getExpire() / 20) * 1000) < current) {
                return this.fail(action, origin);
            }

            // 4. Run the action conditions and fail if they do not pass.

            if (!action.apply(origin)) {
                return this.fail(action, origin);
            }

            // 5. Succeed the action, remove it and set finish if there are no more actions left.

            iterator.remove();

            action.success(origin);

            this.lastExecutionTime = System.currentTimeMillis();

            if (this.queue >= this.observerActions.size() + this.scheduleActions.size()) {
                this.state = State.FINISHED;
            }
        }

        return true;
    }

    private boolean fail(final ObserverAction<T> action, final Origin origin) {
        this.state = action.failure(origin) ? State.CANCELLED : this.state;
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

    public Class<? extends T> getTrigger() {
        return null;
    }

    public State getState() {
        return null;
    }

    public enum State {
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
