package com.abilityapi.sequenceapi.action.condition;

/**
 * Represents a condition that can be assigned a
 * {@link ConditionType}.
 */
public abstract class Condition implements ConditionSupplier {

    private ConditionType conditionType = ConditionType.UNDEFINED;

    /**
     * Sets the {@link ConditionType} for this.
     *
     * @param type the condition type
     */
    public void setType(ConditionType type) {
        this.conditionType = type;
    }

    /**
     * Returns the {@link ConditionType} for this.
     *
     * @return the condition type
     */
    public ConditionType getType() {
        return this.conditionType;
    }

}
