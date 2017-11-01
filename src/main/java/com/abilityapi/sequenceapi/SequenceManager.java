package com.abilityapi.sequenceapi;

import com.abilityapi.sequenceapi.context.SequenceContext;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.UUID;
import java.util.function.Predicate;

import static com.abilityapi.sequenceapi.util.SequencePreconditions.checkOriginType;
import static com.google.common.base.Preconditions.checkNotNull;

public class SequenceManager<T> {

    private final SequenceRegistry<T> sequenceRegistry;
    private final Multimap<UUID, Sequence<T>> sequences = HashMultimap.create();
    private final Multimap<UUID, Class<? extends T>> blockedSequences = HashMultimap.create();

    public SequenceManager(final SequenceRegistry<T> sequenceRegistry) {
        this.sequenceRegistry = sequenceRegistry;
    }

    /**
     * Invokes the observer check with the provided event
     * and {@link SequenceContext}.
     *
     * @param event the event
     * @param sequenceContext the sequenceContext
     */
    public void invokeObserver(final T event, final SequenceContext sequenceContext) {
        checkNotNull(event);
        checkNotNull(sequenceContext);
        checkOriginType(sequenceContext, SequenceContext.ID, UUID.class);

        this.sequences.get((UUID) sequenceContext.getId()).removeIf(sequence -> {
           if (this.blockedSequences.containsEntry(sequenceContext.getId(), sequence.getTrigger())) return true;

           return this._invokeObserver(event, sequence, sequenceContext);
        });

        // Creates a new sequence from a blueprint.

        this._createBlueprints(event, sequenceContext);
    }

    /**
     * Invokes the observer check with the provided event
     * and {@link SequenceContext} if the predicate returns true.
     *
     * @param event the event
     * @param sequenceContext the sequenceContext
     * @param predicate the predicate
     */
    public void invokeObserverIf(final T event, final SequenceContext sequenceContext, final Predicate<Object> predicate) {
        checkNotNull(event);
        checkNotNull(sequenceContext);
        checkNotNull(predicate);
        checkOriginType(sequenceContext, SequenceContext.ID, UUID.class);

        this.sequences.get((UUID) sequenceContext.getId()).removeIf(sequence -> {
            if (!predicate.test(sequence)) return false;
            if (this.blockedSequences.containsEntry(sequenceContext.getId(), sequence.getTrigger())) return true;

            return this._invokeObserver(event, sequence, sequenceContext);
        });

        // Creates a new sequence from a blueprint.

        this._createBlueprints(event, sequenceContext);
    }

    /**
     * Invokes the scheduler update with the provided
     * {@link SequenceContext}.
     *
     * @param sequenceContext the sequenceContext
     */
    public void updateScheduler(final SequenceContext sequenceContext) {
        checkNotNull(sequenceContext);
        checkOriginType(sequenceContext, SequenceContext.ID, UUID.class);

        this.sequences.get((UUID) sequenceContext.getId()).removeIf(sequence -> {
            if (this.blockedSequences.containsEntry(sequenceContext.getId(), sequence.getTrigger())) return true;

            return this._invokeScheduler(sequence, sequenceContext);
        });
    }

    /**
     * Invokes the scheduler update with the provided
     * {@link SequenceContext} if the predicate returns true.
     *
     * @param sequenceContext the sequenceContext
     * @param predicate the predicate
     */
    public void updateSchedulerIf(final SequenceContext sequenceContext, final Predicate<Object> predicate) {
        checkNotNull(sequenceContext);
        checkNotNull(predicate);
        checkOriginType(sequenceContext, SequenceContext.ID, UUID.class);

        this.sequences.get((UUID) sequenceContext.getId()).removeIf(sequence -> {
            if (predicate.test(sequence)) return false;
            if (this.blockedSequences.containsEntry(sequenceContext.getId(), sequence.getTrigger())) return true;

            return this._invokeScheduler(sequence, sequenceContext);
        });
    }

    /**
     * Adds the trigger class from the {@link SequenceContext} root
     * to the block list, which is removed on the next invocation.
     *
     * @param sequenceContext the sequenceContext
     */
    public void block(final SequenceContext sequenceContext) {
        checkNotNull(sequenceContext);
        checkNotNull(sequenceContext.getRoot());
        checkOriginType(sequenceContext, SequenceContext.ID, UUID.class);

        if (this.blockedSequences.containsEntry(sequenceContext.getId(), sequenceContext.getRoot().getClass())) return;
        this.blockedSequences.put((UUID) sequenceContext.getId(), (Class<? extends T>) sequenceContext.getRoot().getClass());
    }

    /**
     * Removes the trigger class found in the
     * {@link SequenceContext} root in the block list.
     *
     * @param sequenceContext the sequenceContext
     */
    public void unblock(final SequenceContext sequenceContext) {
        checkNotNull(sequenceContext);
        checkNotNull(sequenceContext.getRoot());
        checkOriginType(sequenceContext, SequenceContext.ID, UUID.class);

        if (!this.blockedSequences.containsEntry(sequenceContext.getId(), sequenceContext.getRoot().getClass())) return;
        this.blockedSequences.put((UUID) sequenceContext.getId(), (Class<? extends T>) sequenceContext.getRoot().getClass());
    }

    /**
     * Removes all the {@link Sequence} from the running
     * list if it has expired or cancelled.
     *
     * <p>If force is set to true, it will remove all of the
     * running sequences regardless whether it has been
     * cancelled or expired.</p>
     *
     * @param force false to remove safely, true to remove with force
     */
    public void clean(boolean force) {
        this.sequences.keySet().forEach(uuid -> this.clean(SequenceContext.builder().id(uuid).build(), force));
    }

    /**
     * Removes an {@link SequenceContext}s unique id specific
     * {@link Sequence} from the running list if it has
     * expired or cancelled.
     *
     * <p>If force is set to true, it will remove all of the
     * running sequences regardless whether it has been
     * cancelled or expired.</p>
     *
     * @param sequenceContext the sequenceContext
     * @param force false to remove safely, true to remove with force
     */
    public void clean(final SequenceContext sequenceContext, boolean force) {
        checkNotNull(sequenceContext);
        checkOriginType(sequenceContext, SequenceContext.ID, UUID.class);

        this.sequences.get((UUID) sequenceContext.getId()).removeIf(sequence ->
                force || sequence.getState().equals(Sequence.State.EXPIRED));
    }

    private boolean _invokeObserver(final T event, final Sequence<T> sequence, final SequenceContext sequenceContext) {
        checkNotNull(event);
        checkNotNull(sequence);
        checkNotNull(sequenceContext);
        checkOriginType(sequenceContext, SequenceContext.ID, UUID.class);

        boolean remove = false;

        if (this.blockedSequences.containsEntry(sequenceContext.getId(), sequence.getTrigger())) return true;

        // 1. Apply the sequence.

        sequence.applyObserve(event, sequenceContext);

        // 2. Check if the sequence is cancelled, or is expired.

        Sequence.State sequenceState = sequence.getState();

        if (!sequenceState.isSafe()) {
            remove = true;
        }

        // 3. Check if the sequence has finished and fire the hook and remove.

        if (sequenceState.equals(Sequence.State.FINISHED)) {
            // Fire sequence finish hook.

            remove = true;
        }

        return remove;
    }

    private boolean _invokeScheduler(final Sequence<T> sequence, final SequenceContext sequenceContext) {
        checkNotNull(sequence);
        checkNotNull(sequenceContext);
        checkOriginType(sequenceContext, SequenceContext.ID, UUID.class);

        boolean remove = false;

        // 1. Apply the sequence.

        sequence.applySchedule(sequenceContext);

        // 2. Check if the sequence is cancelled, or is expired.

        Sequence.State sequenceState = sequence.getState();

        if (!sequenceState.isSafe()) {
            remove = true;
        }

        // 3. Check if the sequence has finished and fire the hook and remove.

        if (sequenceState.equals(Sequence.State.FINISHED)) {
            // Fire sequence finish hook.

            remove = true;
        }

        return remove;
    }

    private void _createBlueprints(final T event, final SequenceContext sequenceContext) {
        checkNotNull(event);
        checkNotNull(sequenceContext);
        checkOriginType(sequenceContext, SequenceContext.ID, UUID.class);

        for (SequenceBlueprint<T> sequenceBlueprint : this.sequenceRegistry) {

            if (this.blockedSequences.containsEntry(sequenceContext.getId(), sequenceBlueprint.getTriggerClass())) continue;

            // 1. Check for matching sequence.

            if (this.sequences.get((UUID) sequenceContext.getId()).stream()
                    .anyMatch(playerSequence -> playerSequence.getBlueprint()
                    .equals(sequenceBlueprint))) continue;

            // 2. Apply the sequence for the first time to check the observer or leave it.

            Sequence<T> sequence = sequenceBlueprint.create(sequenceContext);

            if (sequence.applyObserve(event, sequenceContext)) {
                if (!sequence.getState().isSafe() || sequence.getState().equals(Sequence.State.FINISHED)) {
                    continue;
                }

                this.sequences.put((UUID) sequenceContext.getId(), sequence);
            }
        }
    }

}
