package com.abilityapi.sequenceapi.action;

import com.abilityapi.sequenceapi.action.condition.Condition;
import com.abilityapi.sequenceapi.origin.Origin;

/**
 * Represents an action type.
 */
public interface Action {

    void addCondition(Condition condition);

    void setDelay(int period);

    void setExpire(int period);

    int getDelay();

    int getExpire();

    Origin apply(final Origin origin);

    Origin success(final Origin origin);

    Origin failure(final Origin origin);

}
