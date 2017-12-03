package com.abilityapi.sequenceapi;

import java.util.NoSuchElementException;

public class SequencePreconditions {

    public static void checkContextNotNull(final SequenceContext sequenceContext,
                                           final String key) {
        if (sequenceContext.get(key) == null) throw new NoSuchElementException("Element for key '" + key + "' was null.");
    }

    public static void checkContextType(final SequenceContext sequenceContext,
                                            final String key,
                                            final Class<?> type) throws NoSuchElementException {
        if (sequenceContext.get(key) == null) throw new NoSuchElementException("Element for key '" + key + "' was null.");

        if (!sequenceContext.get(key).getClass().isAssignableFrom(type))
            throw new NoSuchElementException("Element for key '" + key + "' was not found inside { " + sequenceContext.toString() + " }.");
    }

    public static void checkContextType(final SequenceContext sequenceContext,
                                            final String key,
                                            final Class<?> type,
                                            final String message) throws NoSuchElementException {
        if (sequenceContext.get(key) == null) throw new NoSuchElementException("Element for key '" + key + "' was null.");

        if (!sequenceContext.get(key).getClass().isAssignableFrom(type))
            throw new NoSuchElementException(message);
    }

}
