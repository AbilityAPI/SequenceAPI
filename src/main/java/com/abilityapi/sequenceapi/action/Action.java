package com.abilityapi.sequenceapi.action;

import com.abilityapi.sequenceapi.action.condition.Condition;
import com.abilityapi.sequenceapi.origin.Origin;
import com.abilityapi.sequenceapi.sequence.Sequence;

/**
 * Represents an action type.
 */
public interface Action {

    void addCondition(Condition condition);

    void setDelay(int period);

    void setExpire(int period);

    int getDelay();

    int getExpire();

    boolean apply(final Origin origin);

    boolean success(final Origin origin);

    boolean failure(final Origin origin);

}
