package com.abilityapi.sequenceapi;

/**
 * Represents a way to create a new {@link Sequence}
 * from a pre-existing order.
 *
 * @param <T> the event type
 */
public interface SequenceBlueprint<T> {

    /**
     * Returns a new {@link Sequence} created with
     * the existing order.
     *
     * @param rootEvent the sequence trigger instance
     * @param sequenceContext the sequence context
     * @return the new sequence
     */
    Sequence<T> create(final T rootEvent, final SequenceContext sequenceContext);

    /**
     * Returns the {@link Sequence} event trigger
     * class.
     *
     * <p>Also accessed by {@link Sequence#getTriggerClass()}.</p>
     *
     * @return the event trigger class
     */
    Class<? extends T> getTrigger();

    /**
     * Returns the {@link SequenceContext} for the creation
     * of this blueprint.
     *
     * @return the creation context
     */
    SequenceContext getContext();

}
