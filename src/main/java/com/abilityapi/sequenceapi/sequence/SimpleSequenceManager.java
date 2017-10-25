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
        this.sequences.get(origin.getUniqueKey()).removeIf(sequence -> {
            if (predicate.test(sequence)) return false;
            if (this.blockedSequences.containsEntry(origin.getUniqueKey(), sequence.getNextActionClass())) return true;

            return this._invokeObserver(event, origin);
        });
    }

    public void updateScheduler(final Origin origin) {
        this.sequences.get(origin.getUniqueKey()).removeIf(sequence -> {
            if (this.blockedSequences.containsEntry(origin.getUniqueKey(), sequence.getNextActionClass())) return true;

            return this._invokeScheduler(origin);
        });
    }

    public void updateSchedulerIf(final Origin origin, final Predicate<Object> predicate) {
        this.sequences.get(origin.getUniqueKey()).removeIf(sequence -> {
            if (predicate.test(sequence)) return false;
            if (this.blockedSequences.containsEntry(origin.getUniqueKey(), sequence.getNextActionClass())) return true;

            return this._invokeScheduler(origin);
        });
    }

    public void block(final Origin origin) {
        if (this.blockedSequences.containsEntry(origin.getUniqueKey(), origin.getRoot().getClass())) return;

        this.blockedSequences.put(origin.getUniqueKey(), (Class<? extends T>) origin.getRoot().getClass());
    }

    public void unblock(final Origin origin) {
        if (!this.blockedSequences.containsEntry(origin.getUniqueKey(), origin.getRoot().getClass())) return;

        this.blockedSequences.put(origin.getUniqueKey(), (Class<? extends T>) origin.getRoot().getClass());
    }

    public void clean(boolean force) {
        this.sequences.keySet().forEach(uuid -> this.clean(Origin.builder().uniqueKey(uuid).build(), force));
    }

    public void clean(final Origin origin, boolean force) {
        this.sequences.get(origin.getUniqueKey()).removeIf(sequence ->
                force || sequence.getState().equals(Sequence.State.EXPIRED));
    }

    private boolean _invokeObserver(final T event, final Origin origin) {
        return false;
    }

    private boolean _invokeScheduler(final Origin origin) {
        return false;
    }

}
