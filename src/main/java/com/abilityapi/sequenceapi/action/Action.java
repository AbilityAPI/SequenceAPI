package com.abilityapi.sequenceapi.action;

import com.abilityapi.sequenceapi.SequenceContext;
import com.abilityapi.sequenceapi.action.condition.Condition;
import com.abilityapi.sequenceapi.action.condition.ConditionType;

/**
 * Represents an action.
 */
public interface Action {

    /**
     * Adds a {@link Condition} to this {@link Action}.
     *
     * @param condition the condition
     */
    void addCondition(final Condition condition);

    /**
     * Sets a delay to this {@link Action}.
     *
     * @param period the period of delay
     */
    void setDelay(final int period);

    /**
     * Returns the delay period.
     *
     * @return the delay period
     */
    int getDelay();

    /**
     * Applies all {@link ConditionType#NORMAL} {@link Condition}s
     * in this {@link Action} and returns the result of it.
     *
     * @param sequenceContext the sequence context
     * @return true if all of them passed, false if any did not
     */
    boolean apply(final SequenceContext sequenceContext);

    /**
     * Applies all {@link ConditionType#SUCCESS} {@link Condition}s
     * in this {@link Action} and returns the result of it.
     *
     * @param sequenceContext the sequence context
     * @return true if all of them passed, false if any did not
     */
    boolean success(final SequenceContext sequenceContext);

    /**
     * Applies all {@link ConditionType#FAIL} {@link Condition}s
     * in this {@link Action} and returns the result of it.
     *
     * @param sequenceContext the sequence context
     * @return true if any of them passed, false if all did not
     */
    boolean failure(final SequenceContext sequenceContext);

}
