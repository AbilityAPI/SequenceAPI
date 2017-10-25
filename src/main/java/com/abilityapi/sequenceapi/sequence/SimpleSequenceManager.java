package com.abilityapi.sequenceapi.sequence;

import com.abilityapi.sequenceapi.origin.Origin;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.UUID;
import java.util.function.Predicate;

public abstract class SimpleSequenceManager<T> {


    private final SequenceRegistry<T> sequenceRegistry;
    private final Multimap<UUID, Sequence<T>> sequences = HashMultimap.create();
    private final Multimap<UUID, Class<? extends T>> blockedSequences = HashMultimap.create();

    public SimpleSequenceManager(SequenceRegistry<T> sequenceRegistry) {
        this.sequenceRegistry = sequenceRegistry;
    }

    public void invokeObserver(final T event, final Origin origin) {
        this.sequences.get(origin.getUniqueKey()).removeIf(sequence -> {
           if (this.blockedSequences.containsEntry(origin.getUniqueKey(), sequence.getNextActionClass())) return true;

           return this._invokeObserver(event, origin);
        });
    }

    public void invokeObserverIf(final T event, final Origin origin, final Predicate<Object> predicate) {

    }

    public void updateScheduler(final Origin origin) {

    }

    public void updateSchedulerIf(final Origin origin, final Predicate<Object> predicate) {

    }

    public void remove(final Origin origin) {

    }

    public void removeIf(final Origin origin, final Predicate<Object> predicate) {

    }

    public void clean(boolean force) {

    }

    public void clean(final Origin origin, boolean force) {

    }

    private boolean _invokeObserver(final T event, final Origin origin) {
        return false;
    }

}
