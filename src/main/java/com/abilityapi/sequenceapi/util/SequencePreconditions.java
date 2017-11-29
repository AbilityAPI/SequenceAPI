package com.abilityapi.sequenceapi.util;

import com.abilityapi.sequenceapi.context.SequenceContext;
import com.abilityapi.sequenceapi.context.SequenceContextKey;

import java.util.NoSuchElementException;

public class SequencePreconditions {

    public static <T> void checkOriginType(final SequenceContext sequenceContext,
                                           final SequenceContextKey key,
                                           final Class<? extends T> type) throws NoSuchElementException {
        if (sequenceContext.get(key) == null) throw new NoSuchElementException("Element for key '" + key.getId() + "' was null.");

        if (sequenceContext.get(key).getClass().isAssignableFrom(type))
            throw new NoSuchElementException("Element for key '" + key.getId() + "' was not found inside { " + sequenceContext.toString() + " }.");
    }

    public static <T> void checkOriginType(final SequenceContext sequenceContext,
                                           final SequenceContextKey key,
                                           final Class<? extends T> type,
                                           final String message) throws NoSuchElementException {
        if (sequenceContext.get(key) == null) throw new NoSuchElementException("Element for key '" + key.getId() + "' was null.");

        if (sequenceContext.get(key).getClass().isAssignableFrom(type))
            throw new NoSuchElementException(message);
    }

}
