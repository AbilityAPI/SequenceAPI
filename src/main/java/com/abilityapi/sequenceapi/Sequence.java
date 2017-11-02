package com.abilityapi.sequenceapi;

import com.abilityapi.sequenceapi.action.Action;
import com.abilityapi.sequenceapi.action.type.observe.ObserverAction;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleAction;
import com.abilityapi.sequenceapi.context.SequenceContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Represents an order of {@link Action}s
 * and a means of manipulating them to produce
 * a result.
 *
 * @param <T> the event type
 */
public class Sequence<T> {

    private final SequenceContext sequenceContext;
    private final SequenceBlueprint<T> sequenceBlueprint;
    private final Map<ScheduleAction, Integer> scheduleActions = new HashMap<>();
    private final Map<ObserverAction<T>, Integer> observerActions = new HashMap<>();

    private long lastExecutionTime = System.currentTimeMillis();
    private int index = 0;
    private long ticks = 0;
    private State state = State.INACTIVE;

    public Sequence(final SequenceContext sequenceContext, final SequenceBlueprint<T> sequenceBlueprint,
                    final Map<ScheduleAction, Integer> scheduleActions,
                    final Map<ObserverAction<T>, Integer> observerActions) {
        this.sequenceContext = sequenceContext;
        this.sequenceBlueprint = sequenceBlueprint;

        this.scheduleActions.putAll(scheduleActions);
        this.observerActions.putAll(observerActions);
    }

    public boolean applyObserve(final T event, final SequenceContext sequenceContext) {
        Iterator<ObserverAction<T>> iterator = this.observerActions.keySet().iterator();

        if (this.state.equals(State.INACTIVE)) this.state = State.ACTIVE;

        if (iterator.hasNext()) {
            ObserverAction<T> action = iterator.next();

            if (this.observerActions.get(action) != this.index) return false;
            this.index++;

            long current = System.currentTimeMillis();

            // 1. Check that the event is the correct one for this action.

            if (!action.getEventClass().equals(event.getClass())) return this.fail(action, sequenceContext);

            // 2. Fail the action if it is being executed before the delay.

            if (this.lastExecutionTime + ((action.getDelay() / 20) * 1000) > current) {
                return this.fail(action, sequenceContext);
            }

            // 3. Fail the action if it being executed after the expire.

            if (this.lastExecutionTime + ((action.getExpire() / 20) * 1000) < current) {
                return this.fail(action, sequenceContext);
            }

            // 4. Run the action conditions and fail if they do not pass.

            if (!action.apply(sequenceContext)) {
                return this.fail(action, sequenceContext);
            }

            // 5. Succeed the action, remove it and set finish if there are no more actions left.

            iterator.remove();

            action.success(sequenceContext);

            this.lastExecutionTime = System.currentTimeMillis();

            if (this.index >= this.observerActions.size() + this.scheduleActions.size()) {
                this.state = State.FINISHED;
            }
        }

        return true;
    }

    public boolean applySchedule(final SequenceContext sequenceContext) {
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
                return this.fail(action, sequenceContext);
            }

            // 2. Fail the action if it being executed after the expire.

            if (this.lastExecutionTime + ((action.getExpire() / 20) * 1000) < current) {
                return this.fail(action, sequenceContext);
            }

            // 3. Check that the tick is being executed in the period wanted.

            if (this.ticks % action.getPeriod() != 0) {
                return false;
            }

            // 4. Run the action conditions and fail if they do not pass.

            if (!action.apply(sequenceContext)) {
                return this.fail(action, sequenceContext);
            }

            // 5. Succeed the action, remove it and set finish if there are no more actions left.

            iterator.remove();

            action.success(sequenceContext);

            this.lastExecutionTime = System.currentTimeMillis();

            if (this.index >= this.observerActions.size() + this.scheduleActions.size()) {
                this.state = State.FINISHED;
            }
        }

        return true;
    }

    private boolean fail(final Action action, final SequenceContext sequenceContext) {
        this.state = action.failure(sequenceContext) ? State.CANCELLED : this.state;
        return false;
    }

    public final SequenceBlueprint<T> getBlueprint() {
        return this.sequenceBlueprint;
    }

    public final SequenceContext getSequenceContext() {
        return this.sequenceContext;
    }

    public final Class<? extends T> getTrigger() {
        return this.sequenceBlueprint.getTrigger();
    }

    public final State getState() {
        return this.state;
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
