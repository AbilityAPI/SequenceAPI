package com.abilityapi.sequenceapi;

import com.abilityapi.sequenceapi.context.SequenceContext;

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
     * @param sequenceContext the sequence context
     * @return the new sequence
     */
    Sequence create(SequenceContext sequenceContext);

    /**
     * Returns the {@link Sequence} event trigger
     * class.
     *
     * <p>Also accessed by {@link Sequence#getTrigger()}.</p>
     *
     * @return the event trigger class
     */
    Class<? extends T> getTrigger();

}
