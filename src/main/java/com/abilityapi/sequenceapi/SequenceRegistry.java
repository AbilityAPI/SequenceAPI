package com.abilityapi.sequenceapi;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * Represents the registry for {@link SequenceBlueprint}s.
 *
 * @param <T> the event type
 */
public class SequenceRegistry<T> implements Iterable<SequenceBlueprint<T>> {

    private final HashMap<Class<?>, SequenceBlueprint<T>> registry = Maps.newHashMap();

    /**
     * Puts the {@link SequenceBlueprint} into this registry.
     *
     * <p>The {@link SequenceContext} must contain a unique
     * {@link SequenceContext#OWNER} to provide ownership over
     * a {@link Sequence}.</p>
     *
     * @param sequenceBlueprint the sequence blueprint
     * @return true if the blueprint was added, false if it was not
     */
    public final boolean put(final SequenceBlueprint<T> sequenceBlueprint) {
        this.registry.put(sequenceBlueprint.getContext().getOwner(), sequenceBlueprint);

        return true;
    }

    /**
     * Returns the {@link SequenceBlueprint} from this registry using
     * the corresponding key.
     *
     * @param key the key
     * @return the sequence blueprint
     */
    public final <O> Optional<SequenceBlueprint<T>> get(final Class<? extends O> key) {
        return Optional.ofNullable(this.registry.get(key));
    }

    /**
     * Returns the key that represents its {@link SequenceBlueprint}.
     *
     * @return the sequence key, or {@code null} if they check is not contained in this registry
     */
    public final <O> Set<Class<?>> keySet() {
        return this.registry.keySet();
    }

    @Override
    public final Iterator<SequenceBlueprint<T>> iterator() {
        return this.registry.values().iterator();
    }
}
