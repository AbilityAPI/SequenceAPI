package com.abilityapi.sequenceapi.action;

import com.abilityapi.sequenceapi.Sequence;
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
    ObserverActionBuilder<T> observe(Class<? extends T> event);

    /**
     * Adds a new {@link ObserverAction} to the {@link Sequence}
     * from a {@link ObserverActionBuilder}.
     *
     * @param actionBlueprint the observer action blueprint
     * @return the observer action builder
     */
    ObserverActionBuilder<T> observe(ObserverActionBlueprint<T> actionBlueprint);

    /**
     * Adds a new {@link ObserverAction} to the {@link Sequence}.
     *
     * @param action the observer action
     * @return the observer action builder
     */
    ObserverActionBuilder<T> observe(ObserverAction<T> action);

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
    ScheduleActionBuilder<T> schedule(ScheduleAction scheduleAction);

}
