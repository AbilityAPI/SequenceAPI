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

           return this._invokeObserver(event, sequence, origin);
        });

        this._createBlueprints(event, origin);
    }

    public void invokeObserverIf(final T event, final Origin origin, final Predicate<Object> predicate) {
        this.sequences.get(origin.getUniqueKey()).removeIf(sequence -> {
            if (predicate.test(sequence)) return false;
            if (this.blockedSequences.containsEntry(origin.getUniqueKey(), sequence.getNextActionClass())) return true;

            return this._invokeObserver(event, sequence, origin);
        });

        this._createBlueprints(event, origin);
    }

    public void updateScheduler(final Origin origin) {
        this.sequences.get(origin.getUniqueKey()).removeIf(sequence -> {
            if (this.blockedSequences.containsEntry(origin.getUniqueKey(), sequence.getNextActionClass())) return true;

            return this._invokeScheduler(sequence);
        });
    }

    public void updateSchedulerIf(final Origin origin, final Predicate<Object> predicate) {
        this.sequences.get(origin.getUniqueKey()).removeIf(sequence -> {
            if (predicate.test(sequence)) return false;
            if (this.blockedSequences.containsEntry(origin.getUniqueKey(), sequence.getNextActionClass())) return true;

            return this._invokeScheduler(sequence);
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

    private boolean _invokeObserver(final T event, final Sequence<T> sequence, final Origin origin) {
        boolean remove = false;

        if (this.blockedSequences.containsEntry(origin.getUniqueKey(), sequence.getNextActionClass())) return true;

        // 1. Apply the sequence.

        sequence.applyObserve(event, origin);

        // 2. Check if the sequence is cancelled, or is expired.

        Sequence.State sequenceState = sequence.getState();

        if (!sequenceState.isSafe()) {
            remove = true;
        }

        // 3. Check if the sequence has finished and fire the event and remove.

        return false;
    }

    private boolean _invokeScheduler(final Sequence<T> sequence) {
        return false;
    }

    private void _createBlueprints(final T event, final Origin origin) {
        for (SequenceBlueprint<T> sequenceBlueprint : this.sequenceRegistry) {

            // 1. Check for matching sequence.

            if (this.sequences.get(origin.getUniqueKey()).stream()
                    .anyMatch(playerSequence -> playerSequence.getBlueprint()
                    .equals(sequenceBlueprint))) continue;
            // 2. Apply the sequence for the first time to check the observer or leave it.

            Sequence<T> sequence = sequenceBlueprint.create(origin);

            if (sequence.applyObserve(event, origin)) {
                if (!sequence.getState().isSafe() || sequence.getState().equals(Sequence.State.FINISHED)) {
                    continue;
                }

                this.sequences.put(origin.getUniqueKey(), sequence);
            }
        }
    }

}
