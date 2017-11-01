package com.abilityapi.sequenceapi;

import com.abilityapi.sequenceapi.action.Action;
import com.abilityapi.sequenceapi.action.type.observe.ObserverAction;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleAction;
import com.abilityapi.sequenceapi.origin.Origin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Sequence<T> {

    private final Origin origin;
    private final SequenceBlueprint<T> sequenceBlueprint;
    private final Map<ScheduleAction, Integer> scheduleActions = new HashMap<>();
    private final Map<ObserverAction<T>, Integer> observerActions = new HashMap<>();

    private long lastExecutionTime = System.currentTimeMillis();
    private int index = 0;
    private long ticks = 0;
    private State state = State.INACTIVE;

    public Sequence(final Origin origin, final SequenceBlueprint<T> sequenceBlueprint,
                    final Map<ScheduleAction, Integer> scheduleActions,
                    final Map<ObserverAction<T>, Integer> observerActions) {
        this.origin = origin;
        this.sequenceBlueprint = sequenceBlueprint;

        this.scheduleActions.putAll(scheduleActions);
        this.observerActions.putAll(observerActions);
    }

    public boolean applyObserve(final T event, final Origin origin) {
        Iterator<ObserverAction<T>> iterator = this.observerActions.keySet().iterator();

        if (this.state.equals(State.INACTIVE)) this.state = State.ACTIVE;

        if (iterator.hasNext()) {
            ObserverAction<T> action = iterator.next();

            if (this.observerActions.get(action) != this.index) return false;
            this.index++;

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

            if (this.index >= this.observerActions.size() + this.scheduleActions.size()) {
                this.state = State.FINISHED;
            }
        }

        return true;
    }

    public boolean applySchedule(final Origin origin) {
        Iterator<ScheduleAction> iterator = this.scheduleActions.keySet().iterator();

        if (this.state.equals(State.INACTIVE)) this.state = State.ACTIVE;

        this.ticks++;

        if (iterator.hasNext()) {
            ScheduleAction action = iterator.next();

            if (this.scheduleActions.get(action) != this.index) return false;
            this.index++;

            long current = System.currentTimeMillis();

            // 1. Fail the action if it is being executed before the delay.

            if (this.lastExecutionTime + ((action.getDelay() / 20) * 1000) > current) {
                return this.fail(action, origin);
            }

            // 2. Fail the action if it being executed after the expire.

            if (this.lastExecutionTime + ((action.getExpire() / 20) * 1000) < current) {
                return this.fail(action, origin);
            }

            // 3. Check that the tick is being executed in the period wanted.

            if (this.ticks % action.getPeriod() != 0) {
                return false;
            }

            // 4. Run the action conditions and fail if they do not pass.

            if (!action.apply(origin)) {
                return this.fail(action, origin);
            }

            // 5. Succeed the action, remove it and set finish if there are no more actions left.

            iterator.remove();

            action.success(origin);

            this.lastExecutionTime = System.currentTimeMillis();

            if (this.index >= this.observerActions.size() + this.scheduleActions.size()) {
                this.state = State.FINISHED;
            }
        }

        return true;
    }

    private boolean fail(final Action action, final Origin origin) {
        this.state = action.failure(origin) ? State.CANCELLED : this.state;
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
