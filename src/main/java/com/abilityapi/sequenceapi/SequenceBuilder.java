package com.abilityapi.sequenceapi;

import com.abilityapi.sequenceapi.action.Action;
import com.abilityapi.sequenceapi.action.ActionBuilder;
import com.abilityapi.sequenceapi.action.type.observe.ObserverAction;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBlueprint;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBuilder;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleAction;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleActionBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class SequenceBuilder<T> implements ActionBuilder<T> {

    private final List<Action> actions = new ArrayList<>();

    @Override
    public ObserverActionBuilder<T> observe(final Class<? extends T> event) {
        return this.observe(new ObserverAction<>(event));
    }

    @Override
    public ObserverActionBuilder<T> observe(final ObserverActionBlueprint<T> actionBlueprint) {
        return this.observe(actionBlueprint.create());
    }

    @Override
    public ObserverActionBuilder<T> observe(final ObserverAction<T> action) {
        this.actions.add(action);

        return new ObserverActionBuilder<>(this, action);
    }

    @Override
    public ScheduleActionBuilder<T> schedule() {
        return this.schedule(new ScheduleAction());
    }

    @Override
    public ScheduleActionBuilder<T> schedule(final ScheduleAction action) {
        this.actions.add(action);

        return new ScheduleActionBuilder<>(this, action);
    }

    @Override
    public SequenceBlueprint<T> build(final SequenceContext buildContext) {
        return new SequenceBlueprint<T>() {
            @Override
            public final Sequence<T> create(final SequenceContext createSequenceContext) {
                final SequenceContext createContext = SequenceContext.from(createSequenceContext)
                        .custom("trigger", getTrigger())
                        .merge(buildContext).build();

                return new Sequence<>(createContext, this, SequenceBuilder.this.actions);
            }

            @Override
            public final Class<? extends T> getTrigger() {
                if (SequenceBuilder.this.actions.isEmpty()) throw new NoSuchElementException("Sequence could not be established without an initial observer action.");
                if (SequenceBuilder.this.actions.get(0) instanceof ObserverAction) {
                    return ((ObserverAction<T>) SequenceBuilder.this.actions.get(0)).getEventClass();
                } else throw new ClassCastException("Sequence could not be established without an initial observer action.");
            }
        };
    }
}
