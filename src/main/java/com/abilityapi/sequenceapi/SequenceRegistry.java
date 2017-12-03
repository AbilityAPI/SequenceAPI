package com.abilityapi.sequenceapi;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * Represents the registry for {@link SequenceBlueprint}s.
 *
 * @param <T> the event type
 */
public class SequenceRegistry<T> implements Iterable<SequenceBlueprint<T>> {

    private final BiMap<Class<? extends SequenceBlueprint>, SequenceBlueprint<T>> registry = HashBiMap.create();

    /**
     * Puts the {@link SequenceBlueprint} into this registry.
     *
     * @param sequenceBlueprint the sequence blueprint
     * @return true if the blueprint was added, false if it was not
     */
    public final boolean put(final SequenceBlueprint<T> sequenceBlueprint) {
        if (this.registry.containsKey(sequenceBlueprint.getClass())) return false;
        this.registry.put(sequenceBlueprint.getClass(), sequenceBlueprint);

        return true;
    }

    /**
     * Returns the {@link SequenceBlueprint} from this registry using
     * the corresponding key.
     *
     * @param key the key
     * @return the sequence blueprint
     */
    public final Optional<SequenceBlueprint<T>> get(final Class<? extends SequenceBlueprint> key) {
        if (!this.registry.containsKey(key)) return Optional.empty();
        return Optional.ofNullable(this.registry.get(key));
    }

    /**
     * Returns the {@link Class} key that represents its {@link SequenceBlueprint}.
     *
     * @param sequenceBlueprint the blueprint
     * @return the sequence key, or {@code null} if they check is not contained in this registry
     */
    public final Class<?> key(final SequenceBlueprint<T> sequenceBlueprint) {
        return this.registry.inverse().get(sequenceBlueprint);
    }

    /**
     * Returns the key that represents its {@link SequenceBlueprint}.
     *
     * @return the sequence key, or {@code null} if they check is not contained in this registry
     */
    public final Set<Class<? extends SequenceBlueprint>> keySet() {
        return this.registry.keySet();
    }

    @Override
    public final Iterator<SequenceBlueprint<T>> iterator() {
        return this.registry.values().iterator();
    }
}
