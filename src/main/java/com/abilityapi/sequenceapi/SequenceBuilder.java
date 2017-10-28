package com.abilityapi.sequenceapi;

import com.abilityapi.sequenceapi.action.CommonActionBuilder;
import com.abilityapi.sequenceapi.action.type.observe.ObserverAction;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBlueprint;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBuilder;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleAction;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleActionBlueprint;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleActionBuilder;

import java.util.HashMap;
import java.util.Map;

public abstract class SequenceBuilder<T> implements CommonActionBuilder<T> {

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
        this.observerActions.put(action, ++this.index);

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
        this.scheduleActions.put(action, ++this.index);

        return new ScheduleActionBuilder();
    }
}
