package com.abilityapi.sequenceapi;

import com.abilityapi.sequenceapi.action.ActionBuilder;
import com.abilityapi.sequenceapi.action.type.observe.ObserverAction;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBlueprint;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBuilder;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleAction;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleActionBuilder;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class SequenceBuilder<T> implements ActionBuilder<T> {

    private int index = 0;
    private final Map<ScheduleAction, Integer> scheduleActions = new HashMap<>();
    private final Map<ObserverAction<T>, Integer> observerActions = new HashMap<>();

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
        this.observerActions.put(action, this.index++);

        return new ObserverActionBuilder<>(this, action);
    }

    @Override
    public ScheduleActionBuilder<T> schedule() {
        return this.schedule(new ScheduleAction());
    }

    @Override
    public ScheduleActionBuilder<T> schedule(final ScheduleAction action) {
        this.scheduleActions.put(action, this.index++);

        return new ScheduleActionBuilder<>(this, action);
    }

    @Override
    public SequenceBlueprint<T> build(final SequenceContext buildContext) {
        return new SequenceBlueprint<T>() {
            @Override
            public final Sequence<T> create(final SequenceContext createSequenceContext) {
                final SequenceContext createContext = SequenceContext.from(createSequenceContext).merge(buildContext).build();
                this.validateSequence();

                return new Sequence<>(createContext, this, SequenceBuilder.this.scheduleActions, SequenceBuilder.this.observerActions);
            }

            @Override
            public final Class<? extends T> getTrigger() {
                final BiMap<Integer, ObserverAction<T>> observers = HashBiMap.create(this.validateSequence()).inverse();

                return observers.get(0).getEventClass();
            }

            private Map<ObserverAction<T>, Integer> validateSequence() throws NoSuchElementException {
                if (SequenceBuilder.this.observerActions.isEmpty() || !SequenceBuilder.this.observerActions.containsValue(0)) throw
                        new NoSuchElementException("Sequence could not be established without an initial observer.");

                return SequenceBuilder.this.observerActions;
            }
        };
    }
}
