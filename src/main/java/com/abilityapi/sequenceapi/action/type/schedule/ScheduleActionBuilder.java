package com.abilityapi.sequenceapi.action.type.schedule;

import com.abilityapi.sequenceapi.SequenceBlueprint;
import com.abilityapi.sequenceapi.SequenceBuilder;
import com.abilityapi.sequenceapi.action.ActionBuilder;
import com.abilityapi.sequenceapi.action.condition.Condition;
import com.abilityapi.sequenceapi.action.type.observe.ObserverAction;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBlueprint;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBuilder;
import com.abilityapi.sequenceapi.context.SequenceContext;

public class ScheduleActionBuilder<T> implements ActionBuilder<T> {

    private final SequenceBuilder<T> sequenceBuilder;
    private final ScheduleAction action;

    public ScheduleActionBuilder(final SequenceBuilder<T> sequenceBuilder, ScheduleAction action) {
        this.sequenceBuilder = sequenceBuilder;
        this.action = action;
    }

    public ScheduleActionBuilder<T> condition(final Condition condition) {
        this.action.addCondition(condition);
        return this;
    }

    public ScheduleActionBuilder<T> delay(int value) {
        this.action.setDelay(value);
        return this;
    }

    public ScheduleActionBuilder<T> expire(int value) {
        this.action.setExpire(value);
        return this;
    }

    public ScheduleActionBuilder<T> period(int value) {
        this.action.setPeriod(value);
        return this;
    }

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
        return this.sequenceBuilder.observe(action);
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
    public ScheduleActionBuilder<T> schedule(ScheduleAction scheduleAction) {
        return this.sequenceBuilder.schedule(scheduleAction);
    }

    public SequenceBlueprint<T> build(final SequenceContext sequenceContext) {
        return this.sequenceBuilder.build(sequenceContext);
    }

}
