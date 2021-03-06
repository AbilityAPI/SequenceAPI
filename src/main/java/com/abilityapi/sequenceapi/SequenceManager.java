package com.abilityapi.sequenceapi;

import com.abilityapi.sequenceapi.util.Ordered;
import com.abilityapi.sequenceapi.util.Tristate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import java.util.UUID;
import java.util.function.Predicate;

import static com.abilityapi.sequenceapi.SequencePreconditions.checkContextNotNull;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a way to manage the life cycle of
 * an {@link Sequence} provided by the {@link SequenceRegistry}
 * as a {@link SequenceBlueprint}.
 *
 * @param <T> the event type
 */
public class SequenceManager<T> {

    private final SequenceRegistry<T> sequenceRegistry;
    private final Multimap<UUID, Sequence<T>> sequences = HashMultimap.create();
    private final Multimap<UUID, Ordered<Class<? extends T>>> blockedSequences = HashMultimap.create();

    private long blockOrderIndex = 0;

    public SequenceManager(final SequenceRegistry<T> sequenceRegistry) {
        this.sequenceRegistry = sequenceRegistry;
    }

    /**
     * Invokes the observer check with the provided event
     * and {@link SequenceContext}.
     *
     * <p>The {@link SequenceContext} must contain a unique
     * {@link SequenceContext#ID} to provide ownership over
     * a running {@link Sequence}.</p>
     *
     * @param event the event
     * @param sequenceContext the sequence context
     */
    public void invokeObserver(final T event, final SequenceContext sequenceContext) {
        checkNotNull(event);
        checkNotNull(sequenceContext);
        SequencePreconditions.checkContextType(sequenceContext, SequenceContext.ID, UUID.class);

        this.sequences.get(sequenceContext.getId()).removeIf(sequence -> {
            for (Ordered<Class<? extends T>> block : this.blockedSequences.get(sequenceContext.getId())) {
                if (block.getElement().equals(sequence.getTriggerClass())) return true;
            }

            return this._invokeObserver(event, sequence, sequenceContext);
        });

        // Creates a new sequence from a blueprint.
        this._createBlueprints(event, sequenceContext);
    }

    /**
     * Invokes the observer check with the provided event
     * and {@link SequenceContext} if the predicate returns true.
     *
     * <p>The {@link SequenceContext} must contain a unique
     * {@link SequenceContext#ID} to provide ownership over
     * a running {@link Sequence}.</p>
     *
     * @param event the event
     * @param sequenceContext the sequence context
     * @param predicate the predicate
     */
    public void invokeObserverIf(final T event, final SequenceContext sequenceContext, final Predicate<Object> predicate) {
        checkNotNull(event);
        checkNotNull(sequenceContext);
        checkNotNull(predicate);
        SequencePreconditions.checkContextType(sequenceContext, SequenceContext.ID, UUID.class);

        this.sequences.get(sequenceContext.getId()).removeIf(sequence -> {
            for (Ordered<Class<? extends T>> block : this.blockedSequences.get(sequenceContext.getId())) {
                if (block.getElement().equals(sequence.getTriggerClass())) return true;
            }

            if (!predicate.test(sequence)) return false;

            return this._invokeObserver(event, sequence, sequenceContext);
        });

        // Creates a new sequence from a blueprint.
        this._createBlueprints(event, sequenceContext);
    }

    /**
     * Invokes the scheduler update with the provided
     * {@link SequenceContext}.
     *
     * <p>The {@link SequenceContext} must contain a unique
     * {@link SequenceContext#ID} to provide ownership over
     * a running {@link Sequence}.</p>
     *
     * @param sequenceContext the sequence context
     */
    public void updateScheduler(final SequenceContext sequenceContext) {
        checkNotNull(sequenceContext);
        SequencePreconditions.checkContextType(sequenceContext, SequenceContext.ID, UUID.class);

        this.sequences.get(sequenceContext.getId()).removeIf(sequence -> {
            for (Ordered<Class<? extends T>> block : this.blockedSequences.get(sequenceContext.getId())) {
                if (block.getElement().equals(sequence.getTriggerClass())) return true;
            }

            return this._invokeScheduler(sequence, sequenceContext) || this._invokeAfter(sequence, sequenceContext);
        });
    }

    /**
     * Invokes the scheduler update with the provided
     * {@link SequenceContext} if the predicate returns true.
     *
     * <p>The {@link SequenceContext} must contain a unique
     * {@link SequenceContext#ID} to provide ownership over
     * a running {@link Sequence}.</p>
     *
     * @param sequenceContext the sequence context
     * @param predicate the predicate
     */
    public void updateSchedulerIf(final SequenceContext sequenceContext, final Predicate<Sequence<T>> predicate) {
        checkNotNull(sequenceContext);
        checkNotNull(predicate);
        SequencePreconditions.checkContextType(sequenceContext, SequenceContext.ID, UUID.class);

        this.sequences.get(sequenceContext.getId()).removeIf(sequence -> {
            for (Ordered<Class<? extends T>> block : this.blockedSequences.get(sequenceContext.getId())) {
                if (block.getElement().equals(sequence.getTriggerClass())) return true;
            }

            if (!predicate.test(sequence)) return false;

            return this._invokeScheduler(sequence, sequenceContext) || this._invokeAfter(sequence, sequenceContext);
        });
    }

    /**
     * Adds the trigger class from the {@link SequenceContext} root
     * to the block list, which is removed on the next invocation.
     *
     * <p>The {@link SequenceContext} must contain a unique
     * {@link SequenceContext#OWNER} to provide ownership over
     * a running {@link Sequence}.
     *
     * It must also contain a {@link SequenceContext#ROOT} that
     * is an event class trigger of {@link T} to filter.</p>
     *
     * @param sequenceContext the sequence context
     * @return the block id
     */
    public long block(final SequenceContext sequenceContext) {
        checkNotNull(sequenceContext);
        checkContextNotNull(sequenceContext, SequenceContext.ROOT);
        SequencePreconditions.checkContextType(sequenceContext, SequenceContext.OWNER, UUID.class);

        final long index = this.blockOrderIndex++;

        if (this.blockedSequences.containsEntry(sequenceContext.getOwner(), new Ordered<>(index, sequenceContext.getRoot()))) return index;
        this.blockedSequences.put(sequenceContext.getOwner(), new Ordered<>(index, sequenceContext.getRoot()));

        return index;
    }

    /**
     * Removes the trigger class found in the
     * {@link SequenceContext} root in the block list.
     *
     * <p>The {@link SequenceContext} must contain a unique
     * {@link SequenceContext#OWNER} to provide ownership over
     * a running {@link Sequence}.
     *
     * It will also look for a {@link SequenceContext#ID} to
     * provide the order index.
     *
     * It must also contain a {@link SequenceContext#ROOT} that
     * is an event class trigger of {@link T} to filter.</p>
     *
     * @param sequenceContext the sequence context
     */
    public void unblock(final SequenceContext sequenceContext) {
        checkNotNull(sequenceContext);
        checkContextNotNull(sequenceContext, SequenceContext.ROOT);
        SequencePreconditions.checkContextType(sequenceContext, SequenceContext.ID, Long.class);
        SequencePreconditions.checkContextType(sequenceContext, SequenceContext.OWNER, UUID.class);

        if (!this.blockedSequences.containsEntry(sequenceContext.getOwner(), new Ordered<>(sequenceContext.getId(), sequenceContext.getRoot()))) return;
        this.blockedSequences.remove(sequenceContext.getOwner(), new Ordered<>(sequenceContext.getId(), sequenceContext.getRoot()));
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
        ImmutableSet.copyOf(this.sequences.keySet()).forEach(uuid -> this.clean(SequenceContext.builder().id(uuid).build(), force));
    }

    /**
     * Removes a specific {@link Sequence} from the running
     * list if it has expired or cancelled.
     *
     * <p>If force is set to true, it will remove all of the
     * running sequences regardless whether it has been
     * cancelled or expired.</p>
     *
     * <p>The {@link SequenceContext} must contain a unique
     * {@link SequenceContext#ID} to provide ownership over
     * a running {@link Sequence}.</p>
     *
     * @param sequenceContext the sequence context
     * @param force false to remove safely, true to remove with force
     */
    public void clean(final SequenceContext sequenceContext, boolean force) {
        checkNotNull(sequenceContext);
        SequencePreconditions.checkContextType(sequenceContext, SequenceContext.ID, UUID.class);

        this.sequences.get(sequenceContext.getId()).removeIf(sequence -> {
            if (sequence.getState().equals(Sequence.State.FINISHED)) return force;

            return force || sequence.getState().equals(Sequence.State.EXPIRED);
        });
    }

    public boolean _invokeObserver(final T event, final Sequence<T> sequence, final SequenceContext sequenceContext) {
        boolean remove = false;

        // 1. Apply the sequence.

        sequence.applyObserve(event, sequenceContext);

        // 2. Check if the sequence is cancelled, or is expired.

        final Sequence.State sequenceState = sequence.getState();

        // Check for an updated state first.
        sequenceState.update(sequence);

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

    public boolean _invokeAfter(final Sequence<T> sequence, final SequenceContext sequenceContext) {
        boolean remove;

        // 1. Apply the sequence, this has one chance to pass, so if it fails it can be removed.

        remove = sequence.applyAfter(sequenceContext).equals(Tristate.FALSE);

        // 2. Check if the sequence is cancelled, or is expired.

        final Sequence.State sequenceState = sequence.getState();

        // Check for an updated state first.
        sequenceState.update(sequence);

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

    public boolean _invokeScheduler(final Sequence<T> sequence, final SequenceContext sequenceContext) {
        boolean remove = false;

        // 1. Apply the sequence.

        sequence.applySchedule(sequenceContext);

        // 2. Check if the sequence is cancelled, or is expired.

        final Sequence.State sequenceState = sequence.getState();

        // Check for an updated state first.
        sequenceState.update(sequence);

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

    public void _createBlueprints(final T event, final SequenceContext sequenceContext) {
        for (SequenceBlueprint<T> sequenceBlueprint : this.sequenceRegistry) {

            // 1. Check for matching sequence.

            if (this.sequences.get(sequenceContext.getId()).stream()
                    .anyMatch(playerSequence -> playerSequence.getBlueprint()
                    .equals(sequenceBlueprint))) continue;

            // 2. Apply the sequence for the first time to check the observer or leave it.

            final Sequence<T> sequence = sequenceBlueprint.create(event, sequenceContext);

            if (sequence.applyObserve(event, sequenceContext)) {

                // Check for an updated state first.
                sequence.getState().update(sequence);

                if (!sequence.getState().isSafe()) {
                    continue;
                }

                if (sequence.getState().equals(Sequence.State.FINISHED)) {
                    // Fire sequence finish hook.

                    continue;
                }

                this.sequences.put(sequenceContext.getId(), sequence);
            }
        }
    }

}
