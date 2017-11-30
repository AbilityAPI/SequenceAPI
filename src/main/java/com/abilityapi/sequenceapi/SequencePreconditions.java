package com.abilityapi.sequenceapi;

import java.util.NoSuchElementException;

public class SequencePreconditions {

    public static <T> void checkOriginType(final SequenceContext sequenceContext,
                                           final String key,
                                           final Class<? extends T> type) throws NoSuchElementException {
        if (sequenceContext.get(key) == null) throw new NoSuchElementException("Element for key '" + key + "' was null.");

        if (sequenceContext.get(key).getClass().isAssignableFrom(type))
            throw new NoSuchElementException("Element for key '" + key + "' was not found inside { " + sequenceContext.toString() + " }.");
    }

    public static <T> void checkOriginType(final SequenceContext sequenceContext,
                                           final String key,
                                           final Class<? extends T> type,
                                           final String message) throws NoSuchElementException {
        if (sequenceContext.get(key) == null) throw new NoSuchElementException("Element for key '" + key + "' was null.");

        if (sequenceContext.get(key).getClass().isAssignableFrom(type))
            throw new NoSuchElementException(message);
    }

}
