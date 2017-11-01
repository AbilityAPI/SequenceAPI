package com.abilityapi.sequenceapi;

import com.abilityapi.sequenceapi.action.ActionBuilder;
import com.abilityapi.sequenceapi.action.type.observe.ObserverAction;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBlueprint;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBuilder;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleAction;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleActionBlueprint;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleActionBuilder;
import com.abilityapi.sequenceapi.context.SequenceContext;
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
    public ObserverActionBuilder<T> observe(Class<T> event) {
        return this.observe(new ObserverAction<>(event));
    }

    @Override
    public ObserverActionBuilder<T> observe(ObserverActionBlueprint<T> actionBlueprint) {
        return this.observe(actionBlueprint.create());
    }

    @Override
    public ObserverActionBuilder<T> observe(ObserverAction<T> action) {
        this.observerActions.put(action, this.index++);

        return new ObserverActionBuilder<>(this, action);
    }

    @Override
    public ScheduleActionBuilder<T> schedule() {
        return this.schedule(new ScheduleAction());
    }

    @Override
    public ScheduleActionBuilder<T> schedule(ScheduleActionBlueprint actionBlueprint) {
        return this.schedule(actionBlueprint.create());
    }

    @Override
    public ScheduleActionBuilder<T> schedule(ScheduleAction action) {
        this.scheduleActions.put(action, this.index++);

        return new ScheduleActionBuilder<>(this, action);
    }

    public SequenceBlueprint<T> build(SequenceContext sequenceContext) {
        return new SequenceBlueprint<T>() {
            @Override
            public Sequence create(SequenceContext createSequenceContext) {
                final SequenceContext.Builder newOrigin = SequenceContext.from(createSequenceContext);
                if (sequenceContext != null) newOrigin.merge(sequenceContext);

                return new Sequence<>(newOrigin.build(), this, scheduleActions, this.validateSequence());
            }

            @Override
            public Class<? extends T> getTriggerClass() {
                final BiMap<Integer, ObserverAction<T>> observers = HashBiMap.create(validateSequence()).inverse();

                return observers.get(0).getEventClass();
            }

            private Map<ObserverAction<T>, Integer> validateSequence() throws NoSuchElementException {
                if (observerActions.isEmpty() || !observerActions.containsValue(0)) throw
                        new NoSuchElementException("Sequence could not be established without an initial observer.");

                return observerActions;
            }
        };
    }
}
