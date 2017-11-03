package com.abilityapi.sequenceapi.action.condition;

import com.abilityapi.sequenceapi.action.Action;
import com.abilityapi.sequenceapi.context.SequenceContext;

/**
 * Supplier for the result of this condition with
 * context.
 */
@FunctionalInterface
public interface ConditionSupplier {

    /**
     * Returns the result of applying this {@link Action}
     * condition with the {@link SequenceContext} context.
     *
     * @param sequenceContext the sequenceContext
     * @return the condition result
     */
    boolean apply(final SequenceContext sequenceContext);

}
