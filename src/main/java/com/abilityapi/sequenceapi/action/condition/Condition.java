package com.abilityapi.sequenceapi.action.condition;

public abstract class Condition<T, C, E> implements ConditionSupplier<T, C, E> {

    private ConditionTypes conditionType = ConditionTypes.UNDEFINED;

    public void setType(ConditionTypes type) {
        this.conditionType = type;
    }

    public ConditionTypes getType() {
        return this.conditionType;
    }

}
