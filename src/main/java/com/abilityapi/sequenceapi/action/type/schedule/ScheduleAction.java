package com.abilityapi.sequenceapi.action.type.schedule;

import com.abilityapi.sequenceapi.Sequence;
import com.abilityapi.sequenceapi.action.Action;
import com.abilityapi.sequenceapi.action.condition.Condition;
import com.abilityapi.sequenceapi.action.condition.ConditionType;
import com.abilityapi.sequenceapi.context.SequenceContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a schedule task for a {@link Sequence}.
 *
 * <p>A schedule that is updated every tick on this
 * {@link Sequence} where this action is in the current position,
 * executed at the correct period interval and after the delay
 * period, will have the scheduler set active.
 *
 * When it is active the {@link Sequence} will execute the
 * {@link ConditionType#UNDEFINED} {@link Condition}s.
 *
 * If the {@link ConditionType#UNDEFINED} {@link Condition}s
 * fail the {@link Sequence} will execute the {@link ConditionType#FAIL}
 * {@link Condition}s.
 *
 * If the {@link ConditionType#UNDEFINED} {@link Condition}s
 * succeed the {@link Sequence} will execute the
 * {@link ConditionType#SUCCESS} {@link Condition}s.
 *
 * If the period interval is set, this will repeat execution
 * till the {@link Sequence} cancels OR the schedule expires
 * which it will then execute the {@link ConditionType#SUCCESS}
 * {@link Condition}s one last time.</p>
 */
public class ScheduleAction implements Action {

    private List<Condition> conditions = new ArrayList<>();
    private int delay = 0;
    private int expire = 0;
    private int period = 0;
    private int repeat = 0;

    public ScheduleAction() {}

    @Override
    public void addCondition(final Condition condition) {
        this.conditions.add(condition);
    }

    @Override
    public void setDelay(final int period) {
        this.delay = period;
    }

    @Override
    public void setExpire(final int period) {
        this.expire = period;
    }

    /**
     * Set the period of ticks that must pass
     * in order to execute.
     *
     * @param period the period
     */
    public void setPeriod(final int period) {
        this.period = period;
    }

    @Override
    public final int getDelay() {
        return this.delay;
    }

    @Override
    public final int getExpire() {
        return this.expire;
    }

    /**
     * Returns the period of ticks that must pass
     * in order to execute.
     *
     * @return the period
     */
    public final int getPeriod() {
        return this.period;
    }

    /**
     * Returns the amount of times the action has
     * been executed.
     *
     * @return the amount of repeats
     */
    public final int getRepeats() {
        return this.repeat;
    }

    @Override
    public final boolean apply(final SequenceContext sequenceContext) {
        boolean applyResult = this.conditions.stream()
                .filter(condition -> condition.getType().equals(ConditionType.UNDEFINED))
                .allMatch(condition -> condition.getSupplier().apply(sequenceContext));

        if (applyResult && this.period != 0) this.repeat++;
        return applyResult;
    }

    @Override
    public final boolean success(final SequenceContext sequenceContext) {
        return this.conditions.stream()
                .filter(condition -> condition.getType().equals(ConditionType.SUCCESS))
                .allMatch(condition -> condition.getSupplier().apply(sequenceContext));
    }

    @Override
    public final boolean failure(final SequenceContext sequenceContext) {
        return this.conditions.stream()
                .filter(condition -> condition.getType().equals(ConditionType.FAIL))
                .anyMatch(condition -> condition.getSupplier().apply(sequenceContext));
    }

}
