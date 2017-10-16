package com.abilityapi.sequenceapi.action.condition;

import com.abilityapi.sequenceapi.util.ImmutableContext;
import com.abilityapi.sequenceapi.util.MutableContext;

/**
 * Supplier of an operation for acquiring
 * the summary over a set of conditions.
 *
 * @param <T> the event type
 * @param <E> the data type
 */
@FunctionalInterface
public interface ConditionSupplier<T, C, E> {

    /**
     * Returns the result of applying this condition
     * to its arguments.
     *
     * @param event the event
     * @param immutableContext the immutable context provided to this condition
     * @param mutableContext the mutable context provided to this condition
     * @param timeSinceLast the period between now and the last action
     * @return the condition result
     */
    ConditionResult apply(T event, ImmutableContext<C> immutableContext, MutableContext<E> mutableContext, long timeSinceLast);

}
