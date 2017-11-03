package com.abilityapi.sequenceapi.action.type.observe;

import com.abilityapi.sequenceapi.SequenceBlueprint;
import com.abilityapi.sequenceapi.SequenceBuilder;
import com.abilityapi.sequenceapi.action.ActionBuilder;
import com.abilityapi.sequenceapi.action.condition.Condition;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleAction;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleActionBuilder;
import com.abilityapi.sequenceapi.context.SequenceContext;

public class ObserverActionBuilder<T> implements ActionBuilder<T> {

    private final ObserverAction<T> action;
    private final SequenceBuilder<T> sequenceBuilder;

    public ObserverActionBuilder(final SequenceBuilder<T> sequenceBuilder, final ObserverAction<T> action) {
        this.action = action;
        this.sequenceBuilder = sequenceBuilder;
    }

    public ObserverActionBuilder<T> condition(final Condition condition) {
        this.action.addCondition(condition);
        return this;
    }

    public ObserverActionBuilder<T> delay(int value) {
        this.action.setDelay(value);
        return this;
    }

    public ObserverActionBuilder<T> expire(int value) {
        this.action.setExpire(value);
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
    public ScheduleActionBuilder<T> schedule(ScheduleAction scheduleAction) {
        return this.sequenceBuilder.schedule(scheduleAction);
    }

    public SequenceBlueprint<T> build(final SequenceContext sequenceContext) {
        return this.sequenceBuilder.build(sequenceContext);
    }

}
