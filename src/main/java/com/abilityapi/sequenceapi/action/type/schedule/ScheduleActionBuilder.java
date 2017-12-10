package com.abilityapi.sequenceapi.action.type.schedule;

import com.abilityapi.sequenceapi.SequenceBlueprint;
import com.abilityapi.sequenceapi.SequenceContext;
import com.abilityapi.sequenceapi.action.ActionBuilder;
import com.abilityapi.sequenceapi.action.condition.Condition;
import com.abilityapi.sequenceapi.action.condition.ConditionSupplier;
import com.abilityapi.sequenceapi.action.condition.ConditionType;
import com.abilityapi.sequenceapi.action.type.observe.ObserverAction;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBlueprint;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBuilder;

public class ScheduleActionBuilder<T> implements ActionBuilder<T> {

    private final ActionBuilder<T> sequenceBuilder;
    private final ScheduleAction action;

    public ScheduleActionBuilder(final ActionBuilder<T> sequenceBuilder, ScheduleAction action) {
        this.sequenceBuilder = sequenceBuilder;
        this.action = action;
    }

    /**
     * Adds a {@link Condition} to this {@link ScheduleAction}.
     *
     * @param conditionSupplier the condition supplier
     * @param conditionType the condition type
     * @return this builder
     */
    public final ScheduleActionBuilder<T> condition(final ConditionSupplier conditionSupplier, final ConditionType conditionType) {
        this.action.addCondition(new Condition(conditionSupplier, conditionType));
        return this;
    }

    /**
     * Sets a delay to this {@link ScheduleAction}.
     *
     * @param value the period of delay
     * @return this builder
     */
    public final ScheduleActionBuilder<T> delay(final int value) {
        this.action.setDelay(value);
        return this;
    }

    /**
     * Sets an expire to this {@link ScheduleAction}.
     *
     * @param value the period to expire
     * @return this builder
     */
    public final ScheduleActionBuilder<T> expire(final int value) {
        this.action.setExpire(value);
        return this;
    }

    /**
     * Sets a period to this {@link ScheduleAction}.
     *
     * @param value the period of execution
     * @return this builder
     */
    public final ScheduleActionBuilder<T> period(final int value) {
        this.action.setPeriod(value);
        return this;
    }

    @Override
    public final ObserverActionBuilder<T> observe(final Class<? extends T> event) {
        return this.observe(new ObserverAction<>(event));
    }

    @Override
    public final ObserverActionBuilder<T> observe(final ObserverActionBlueprint<T> actionBlueprint) {
        return this.observe(actionBlueprint.create());
    }

    @Override
    public final ObserverActionBuilder<T> observe(final ObserverAction<T> action) {
        return this.sequenceBuilder.observe(action);
    }

    @Override
    public final ScheduleActionBuilder<T> schedule() {
        return this.schedule(new ScheduleAction());
    }

    @Override
    public final ScheduleActionBuilder<T> schedule(final ScheduleAction scheduleAction) {
        return this.sequenceBuilder.schedule(scheduleAction);
    }

    @Override
    public final SequenceBlueprint<T> build(final SequenceContext sequenceContext) {
        return this.sequenceBuilder.build(sequenceContext);
    }

}
