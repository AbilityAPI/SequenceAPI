package com.abilityapi.sequenceapi;

import com.abilityapi.sequenceapi.action.CommonActionBuilder;
import com.abilityapi.sequenceapi.action.type.observe.ObserverAction;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBlueprint;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBuilder;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleAction;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleActionBlueprint;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleActionBuilder;
import com.abilityapi.sequenceapi.origin.Origin;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class SequenceBuilder<T> implements CommonActionBuilder<T> {

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

        return new ObserverActionBuilder<>();
    }

    @Override
    public ScheduleActionBuilder schedule() {
        return this.schedule(new ScheduleAction());
    }

    @Override
    public ScheduleActionBuilder schedule(ScheduleActionBlueprint actionBlueprint) {
        return this.schedule(actionBlueprint.create());
    }

    @Override
    public ScheduleActionBuilder schedule(ScheduleAction action) {
        this.scheduleActions.put(action, this.index++);

        return new ScheduleActionBuilder();
    }

    public SequenceBlueprint<T> build(Origin origin) {
        return new SequenceBlueprint<T>() {
            @Override
            public Sequence create(Origin createOrigin) {
                final Origin newOrigin = Origin.from(createOrigin).merge(origin).build();

                return new Sequence<>(newOrigin, this, scheduleActions, this.validateSequence());
            }

            @Override
            public Class<? extends T> getTriggerClass() {
                if (observerActions.isEmpty()) return null;
                final BiMap<ObserverAction<T>, Integer> observers = this.validateSequence();

                return observers.inverse().get(1).getEventClass();
            }

            private BiMap<ObserverAction<T>, Integer> validateSequence() throws NoSuchElementException {
                final BiMap<ObserverAction<T>, Integer> observers = HashBiMap.create(observerActions);

                if (observers.isEmpty() || !observers.containsValue(0)) throw
                        new NoSuchElementException("Sequence could not be established without an initial observer.");

                return observers;
            }
        };
    }
}
