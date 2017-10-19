package com.abilityapi.sequenceapi.action;

/**
 * Represents an action type.
 */
public interface ActionType {

    void addCondition();

    void setDelay(int period);

    void setExpire(int period);

    int getDelay();

    int getExpire();

}
