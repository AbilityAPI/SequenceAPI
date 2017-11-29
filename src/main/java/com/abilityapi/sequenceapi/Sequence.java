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

    private int index = 0;
    private long ticks = 0;
    private long lastExecutionTime = System.currentTimeMillis();
    private State state = State.INACTIVE;

    public Sequence(final SequenceContext sequenceContext,
                    final SequenceBlueprint<T> sequenceBlueprint,
                    final Map<ScheduleAction, Integer> scheduleActions,
                    final Map<ObserverAction<T>, Integer> observerActions) {
        this.sequenceContext = sequenceContext;
        this.sequenceBlueprint = sequenceBlueprint;

        this.scheduleActions.putAll(scheduleActions);
        this.observerActions.putAll(observerActions);
    }

    /**
     * Applies the next {@link ObserverAction} in the {@link Sequence}
     * with an appropriate {@link T} and {@link SequenceContext}.
     *
     * @param event the event
     * @param sequenceContext the sequence context
     * @return true if the action was successful and false if it was not
     */
    public final boolean applyObserve(final T event, final SequenceContext sequenceContext) {
        final Iterator<ObserverAction<T>> iterator = this.observerActions.keySet().iterator();

        if (this.state.equals(State.INACTIVE)) this.state = State.ACTIVE;

        if (iterator.hasNext()) {
            final ObserverAction<T> action = iterator.next();

            if (this.observerActions.get(action) != this.index) return false;
            this.index++;

            final long current = System.currentTimeMillis();

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

            return this.succeed(action, sequenceContext);
        }

        return true;
    }

    /**
     * Applies the next {@link ScheduleAction} in the {@link Sequence}
     * with an appropriate {@link SequenceContext}.
     *
     * @param sequenceContext the sequence context
     * @return true if the action was successful and false if it was not
     */
    public final boolean applySchedule(final SequenceContext sequenceContext) {
        final Iterator<ScheduleAction> iterator = this.scheduleActions.keySet().iterator();

        if (this.state.equals(State.INACTIVE)) this.state = State.ACTIVE;

        this.ticks++;

        if (iterator.hasNext()) {
            final ScheduleAction action = iterator.next();

            if (this.scheduleActions.get(action) != this.index) return false;
            this.index++;

            final long current = System.currentTimeMillis();

            // 1. Check that the tick is being executed in the period wanted.

            if (this.ticks % action.getPeriod() != 0) return false;

            // 2. Fail the action if it is being executed before the delay.

            if (this.lastExecutionTime + ((action.getDelay() / 20) * 1000) > current)
                return this.fail(action, sequenceContext);

            // 3. Fail the action if it being executed after the expire.

            if (this.lastExecutionTime + ((action.getExpire() / 20) * 1000) < current) {
                if (action.getRepeats() != 0) {
                    iterator.remove();

                    return this.succeed(action, sequenceContext);
                }

                return this.fail(action, sequenceContext);
            }

            // 4. Run the action conditions and fail if they do not pass.

            if (!action.apply(sequenceContext)) return this.fail(action, sequenceContext);

            // 5. Succeed the action, remove it and set finish if there are no more actions left.
            iterator.remove();

            return this.succeed(action, sequenceContext);
        }

        return true;
    }

    public final boolean succeed(final Action action, final SequenceContext sequenceContext) {
        action.success(sequenceContext);

        this.lastExecutionTime = System.currentTimeMillis();

        if (this.index >= this.observerActions.size() + this.scheduleActions.size()) this.state = State.FINISHED;

        return true;
    }

    public final boolean fail(final Action action, final SequenceContext sequenceContext) {
        this.state = action.failure(sequenceContext) ? State.CANCELLED : this.state;
        return false;
    }

    /**
     * Returns the {@link SequenceBlueprint}.
     *
     * @return the sequence blueprint
     */
    public final SequenceBlueprint<T> getBlueprint() {
        return this.sequenceBlueprint;
    }

    /**
     * Returns the {@link SequenceContext}.
     *
     * @return the sequence context
     */
    public final SequenceContext getSequenceContext() {
        return this.sequenceContext;
    }

    /**
     * Returns the trigger {@link T} class.
     *
     * @return the trigger class
     */
    public final Class<? extends T> getTrigger() {
        return this.sequenceBlueprint.getTrigger();
    }

    /**
     * Returns the {@link Sequence.State}.
     * @return the sequence state
     */
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

        public final boolean isSafe() {
            return this.safe;
        }
    }
}
