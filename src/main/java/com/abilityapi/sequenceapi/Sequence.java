package com.abilityapi.sequenceapi;

import com.abilityapi.sequenceapi.action.Action;
import com.abilityapi.sequenceapi.action.Expirable;
import com.abilityapi.sequenceapi.action.type.after.AfterAction;
import com.abilityapi.sequenceapi.action.type.observe.ObserverAction;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleAction;
import com.abilityapi.sequenceapi.util.EventComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Represents an order of {@link Action}s
 * and a means of manipulating them to produce
 * a result.
 *
 * @param <T> the event type
 */
public class Sequence<T> {

    public static <E> EventComparator<Class<? extends E>, E> getComparatorEqual() {
        return (base, that) -> base.equals(that.getClass());
    }

    public static <E> EventComparator<Class<? extends E>, E> getComparatorAssignable() {
        return (base, that) -> base.isAssignableFrom(that.getClass());
    }

    private final EventComparator<Class<? extends T>, T> eventComparator;
    private final SequenceContext sequenceContext;
    private final SequenceBlueprint<T> sequenceBlueprint;

    private final List<Action> actions;
    private final int actionsSize;

    private int index = 0;
    private long scheduleTicks = 0;
    private long lastTime = System.currentTimeMillis();
    private State state = State.INACTIVE;

    public Sequence(final List<Action> actions,
                    final SequenceContext sequenceContext,
                    final SequenceBlueprint<T> sequenceBlueprint,
                    final EventComparator<Class<? extends T>, T> eventComparator) {
        this.eventComparator = eventComparator;
        this.sequenceContext = sequenceContext;
        this.sequenceBlueprint = sequenceBlueprint;

        this.actions = new ArrayList<>(actions);
        this.actionsSize = actions.size();
    }

    /**
     * Applies the next {@link ObserverAction} in the {@link Sequence}
     * with an appropriate {@link T} and {@link SequenceContext}.
     *
     * @param event the event
     * @param sequenceContext the sequence context
     * @return true if the action was successful and false if it was not
     */
    public boolean applyObserve(final T event, final SequenceContext sequenceContext) {
        final ListIterator<Action> iterator = this.actions.listIterator();

        if (iterator.hasNext()) {
            final Action rawAction = iterator.next();

            final ObserverAction<T> action;
            if (rawAction instanceof ObserverAction) {
                action = (ObserverAction<T>) rawAction;
            } else return false;

            if (this.state.equals(State.INACTIVE)) this.state = State.ACTIVE;

            final long current = System.currentTimeMillis();

            // 1. Check that the event is the correct one for this action.

            if (!this.eventComparator.apply(action.getEventClass(), event)) return this.fail(action, sequenceContext);

            // 2. Fail the action if it is being executed before the delay.

            if (this.index > 0 && this.lastTime + ((action.getDelay() / 20) * 1000) > current) {
                return this.fail(action, sequenceContext);
            }

            // 3. Fail the action if it being executed after the expire.

            if (this.index > 0 && this.lastTime + ((action.getExpire() / 20) * 1000) < current) {
                return this.fail(action, sequenceContext);
            }

            // 4. Run the action conditions and fail if they do not pass.

            if (!action.apply(sequenceContext)) {
                return this.fail(action, sequenceContext);
            }

            // 5. Succeed the action, remove it, increment the index and set finish if there are no more actions left.
            this.index++;

            iterator.remove();

            if (!iterator.hasNext()) {
                if (this.index == this.actionsSize) this.state = State.FINISHED;
            }

            return this.succeed(action, sequenceContext);
        }

        return true;
    }

    /**
     * Applies the next {@link AfterAction} in the {@link Sequence}
     * with an appropriate {@link SequenceContext}.
     *
     * @param sequenceContext the sequence context
     */
    public void applyAfter(final SequenceContext sequenceContext) {
        final ListIterator<Action> iterator = this.actions.listIterator();

        if (iterator.hasNext()) {
            final Action rawAction = iterator.next();

            final AfterAction action;
            if (rawAction instanceof AfterAction) {
                action = (AfterAction) rawAction;
            } else return;

            if (this.state.equals(State.INACTIVE)) this.state = State.ACTIVE;

            final long current = System.currentTimeMillis();

            // 1. Fail the action if it is being executed before the delay.

            if (this.lastTime + ((action.getDelay() / 20) * 1000) > current) {
                this.fail(action, sequenceContext);
                return;
            }

            // 2. Run the action conditions and fail if they do not pass.

            if (!action.apply(sequenceContext)) {
                this.fail(action, sequenceContext);
                return;
            }

            // 3. Succeed the action, remove it, increment the index and set finish if there are no more actions left.

            this.index++;

            iterator.remove();

            if (!iterator.hasNext()) {
                if (this.index == this.actionsSize) this.state = State.FINISHED;
            }

            this.succeed(action, sequenceContext);
        }

    }

    /**
     * Applies the next {@link ScheduleAction} in the {@link Sequence}
     * with an appropriate {@link SequenceContext}.
     *
     * @param sequenceContext the sequence context
     */
    public void applySchedule(final SequenceContext sequenceContext) {
        final ListIterator<Action> iterator = this.actions.listIterator();

        if (this.state.equals(State.INACTIVE)) this.state = State.ACTIVE;

        this.scheduleTicks++;

        if (iterator.hasNext()) {
            final Action rawAction = iterator.next();

            final ScheduleAction action;
            if (rawAction instanceof ScheduleAction) {
                action = (ScheduleAction) rawAction;
            } else return;

            if (this.state.equals(State.INACTIVE)) this.state = State.ACTIVE;

            final long current = System.currentTimeMillis();

            // 1. Check that the tick is being executed in the period wanted.

            if (action.getPeriod() != 0 && this.scheduleTicks % action.getPeriod() != 0) return;

            // 2. Fail the action if it is being executed before the delay.

            if (this.lastTime + ((action.getDelay() / 20) * 1000) > current) {
                this.fail(action, sequenceContext);
                return;
            }

            // 3. Fail the action if it being executed after the expire.

            if (this.lastTime + ((action.getExpire() / 20) * 1000) < current) {
                this.fail(action, sequenceContext);
                return;
            }

            // 4. Run the action conditions and fail if they do not pass.

            if (!action.apply(sequenceContext)) {
                this.fail(action, sequenceContext);
                return;
            }

            // If this is a repeating task that will expire next tick remove it.
            if (current + ((action.getPeriod() / 20) * 1000) > this.lastTime + ((action.getExpire() / 20) * 1000)) {
                this.index++;

                iterator.remove();

                if (!iterator.hasNext()) {
                    if (this.index == this.actionsSize) this.state = State.FINISHED;
                }
            }

            this.succeed(action, sequenceContext);
        }

    }

    public boolean succeed(final Action action, final SequenceContext sequenceContext) {
        action.success(sequenceContext);

        this.lastTime = System.currentTimeMillis();

        return true;
    }

    public boolean fail(final Action action, final SequenceContext sequenceContext) {
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
     * Returns the {@link EventComparator}.
     *
     * @return the event comparator
     */
    public final EventComparator<Class<? extends T>, T> getEventComparator() {
        return this.eventComparator;
    }

    /**
     * Returns the time in milliseconds
     * that the last action occurred.
     *
     * @return the last action time
     */
    public final long getLastActionTime() {
        return this.lastTime;
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

        public void update(final Sequence sequence) {
            // Set no more actions as expired.
            if (sequence.actions.isEmpty()) {
                sequence.state = State.FINISHED;
                return;
            }

            if (!(sequence.actions.get(0) instanceof Expirable)) return;

            Expirable action = (Expirable) sequence.actions.get(0);
            if (sequence.getLastActionTime() + ((action.getExpire() / 20) * 1000) < System.currentTimeMillis()) sequence.state = State.EXPIRED;
        }

        public final boolean isSafe() {
            return this.safe;
        }
    }
}
