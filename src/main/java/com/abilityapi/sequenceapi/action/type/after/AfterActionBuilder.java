package com.abilityapi.sequenceapi.action.type.after;

import com.abilityapi.sequenceapi.SequenceBlueprint;
import com.abilityapi.sequenceapi.SequenceContext;
import com.abilityapi.sequenceapi.action.ActionBuilder;
import com.abilityapi.sequenceapi.action.condition.Condition;
import com.abilityapi.sequenceapi.action.condition.ConditionSupplier;
import com.abilityapi.sequenceapi.action.condition.ConditionType;
import com.abilityapi.sequenceapi.action.type.observe.ObserverAction;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBlueprint;
import com.abilityapi.sequenceapi.action.type.observe.ObserverActionBuilder;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleAction;
import com.abilityapi.sequenceapi.action.type.schedule.ScheduleActionBuilder;

public class AfterActionBuilder<T> implements ActionBuilder<T> {

    private final ActionBuilder<T> sequenceBuilder;
    private final AfterAction action;

    public AfterActionBuilder(final ActionBuilder<T> sequenceBuilder, AfterAction action) {
        this.sequenceBuilder = sequenceBuilder;
        this.action = action;
    }

    /**
     * Adds a {@link Condition} to this {@link AfterAction}.
     *
     * @param conditionSupplier the condition supplier
     * @param conditionType the condition type
     * @return this builder
     */
    public final AfterActionBuilder<T> condition(final ConditionSupplier conditionSupplier, final ConditionType conditionType) {
        this.action.addCondition(new Condition(conditionSupplier, conditionType));
        return this;
    }

    /**
     * Sets a delay to this {@link AfterAction}.
     *
     * @param value the period of delay
     * @return this builder
     */
    public final AfterActionBuilder<T> delay(final int value) {
        this.action.setDelay(value);
        this.action.setExpire(value + 1);
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
    public AfterActionBuilder<T> after() {
        return this.after(new AfterAction());
    }

    @Override
    public AfterActionBuilder<T> after(AfterAction afterAction) {
        return this.sequenceBuilder.after(afterAction);
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
