package com.abilityapi.sequenceapi.action;

import com.abilityapi.sequenceapi.action.condition.Condition;

/**
 * Represents an action type.
 */
public interface ActionType {

    void addCondition(Condition condition);

    void setDelay(int period);

    void setExpire(int period);

    int getDelay();

    int getExpire();

}
