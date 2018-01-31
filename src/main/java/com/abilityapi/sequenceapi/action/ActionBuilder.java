package com.abilityapi.sequenceapi.action;

import com.abilityapi.sequenceapi.Sequence;
import com.abilityapi.sequenceapi.SequenceBlueprint;
import com.abilityapi.sequenceapi.SequenceContext;
import com.abilityapi.sequenceapi.action.type.after.AfterAction;
import com.abilityapi.sequenceapi.action.type.after.AfterActionBuilder;
import com.abilityapi.sequenceapi.action.type.observe.ObserverAction;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBlueprint;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBuilder;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleAction;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleActionBuilder;

/**
 * Represents a basic {@link Action} builder.
 *
 * @param <T> the event type
 */
public interface ActionBuilder<T> {

    /**
     * Adds a new {@link ObserverAction} to the {@link Sequence}
     * using a {@link Class} from a specific {@link T}.
     *
     * @param event the event
     * @return the observer action builder
     */
    ObserverActionBuilder<T> observe(final Class<? extends T> event);

    /**
     * Adds a new {@link ObserverAction} to the {@link Sequence}
     * from a {@link ObserverActionBuilder}.
     *
     * @param actionBlueprint the observer action blueprint
     * @return the observer action builder
     */
    ObserverActionBuilder<T> observe(final ObserverActionBlueprint<T> actionBlueprint);

    /**
     * Adds a new {@link ObserverAction} to the {@link Sequence}.
     *
     * @param action the observer action
     * @return the observer action builder
     */
    ObserverActionBuilder<T> observe(final ObserverAction<T> action);

    /**
     * Adds a new {@link AfterAction} to the {@link Sequence}.
     *
     * @return the after action builder
     */
    AfterActionBuilder<T> after();

    /**
     * Adds a new {@link AfterAction} to the {@link Sequence}.
     *
     * @param afterAction the after action
     * @return the after action builder
     */
    AfterActionBuilder<T> after(final AfterAction afterAction);

    /**
     * Adds a new {@link ScheduleAction} to the {@link Sequence}.
     *
     * @return the schedule action builder
     */
    ScheduleActionBuilder<T> schedule();

    /**
     * Adds a new {@link ScheduleAction} to the {@link Sequence}.
     *
     * @param scheduleAction the schedule action
     * @return the schedule action builder
     */
    ScheduleActionBuilder<T> schedule(final ScheduleAction scheduleAction);

    /**
     * Returns a new {@link SequenceBlueprint} containing
     * the {@link Sequence} of {@link ObserverAction}s and
     * {@link ScheduleAction}s.
     *
     * @param buildContext the sequence context
     * @return the sequence blueprint
     */
    SequenceBlueprint<T> build(final SequenceContext buildContext);

}
