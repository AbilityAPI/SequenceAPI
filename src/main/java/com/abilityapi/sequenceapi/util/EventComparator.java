package com.abilityapi.sequenceapi.util;

/**
 * A functional interface that is used to
 * compare two events and return true
 * if they are equal.
 *
 * @param <A> the base type
 * @param <B> the comparing type
 */
@FunctionalInterface
public interface EventComparator<A, B> {

    /**
     * Checks if the base event is equal to
     * the comparing event.
     *
     * @param base the base event
     * @param that the comparing event
     * @return the result
     */
    boolean apply(A base, B that);

}
