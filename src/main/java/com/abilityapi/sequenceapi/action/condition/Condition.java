package com.abilityapi.sequenceapi.action.condition;

/**
 * Represents a condition that can be assigned a
 * {@link ConditionType}.
 */
public class Condition {

    private final ConditionSupplier conditionSupplier;
    private final ConditionType conditionType;

    public Condition(final ConditionSupplier conditionSupplier) {
        this(conditionSupplier, ConditionType.UNDEFINED);
    }

    public Condition(final ConditionSupplier conditionSupplier, final ConditionType conditionType) {
        this.conditionSupplier = conditionSupplier;
        this.conditionType = conditionType;
    }

    /**
     * Returns the {@link ConditionSupplier} for this.
     *
     * @return the condition supplier
     */
    public ConditionSupplier getSupplier() {
        return this.conditionSupplier;
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
