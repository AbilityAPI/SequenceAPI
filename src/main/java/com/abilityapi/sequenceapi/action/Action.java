package com.abilityapi.sequenceapi.action;

import com.abilityapi.sequenceapi.action.condition.Condition;
import com.abilityapi.sequenceapi.context.SequenceContext;

/**
 * Represents an action.
 */
public interface Action {

    void addCondition(Condition condition);

    void setDelay(int period);

    void setExpire(int period);

    int getDelay();

    int getExpire();

    boolean apply(final SequenceContext sequenceContext);

    boolean success(final SequenceContext sequenceContext);

    boolean failure(final SequenceContext sequenceContext);

}
