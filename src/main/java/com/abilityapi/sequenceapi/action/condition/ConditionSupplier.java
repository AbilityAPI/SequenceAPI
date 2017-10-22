package com.abilityapi.sequenceapi.action.condition;

import com.abilityapi.sequenceapi.action.ActionType;
import com.abilityapi.sequenceapi.origin.Origin;

/**
 * Supplier for the result of this condition with
 * context.
 */
@FunctionalInterface
public interface ConditionSupplier {

    /**
     * Returns the {@link ConditionResult} of applying this {@link ActionType}
     * condition with the {@link Origin} context.
     *
     * @param origin the origin
     * @return the condition result
     */
    ConditionResult apply(final Origin origin);

}
