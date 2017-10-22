package com.abilityapi.sequenceapi.sequence;

import com.abilityapi.sequenceapi.origin.Origin;

import java.util.function.Predicate;

public interface SequenceManager<T> {

    void invokeObserver(final T event, final Origin origin);

    void invokeObserverIf(final T event, final Origin origin, final Predicate<Object> predicate);

    void updateScheduler(final Origin origin);

    void updateSchedulerIf(final Origin origin, final Predicate<Object> predicate);

    void remove(final Origin origin);

    void removeIf(final Origin origin, final Predicate<Object> predicate);

    void clean(boolean force);

    void clean(final Origin origin, boolean force);

}
