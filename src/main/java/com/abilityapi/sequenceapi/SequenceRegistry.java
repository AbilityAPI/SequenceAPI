package com.abilityapi.sequenceapi;

import com.abilityapi.sequenceapi.origin.Origin;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class SequenceRegistry<T> implements Iterable<SequenceBlueprint<T>> {

    private final BiMap<Class<?>, SequenceBlueprint<T>> registry = HashBiMap.create();

    /**
     * Puts the {@link SequenceBlueprint} into this registry.
     *
     * <p>Default implementation takes into account the {@link Origin#OWNER}
     * with the {@link Origin#ROOT} set as the blueprint if it does not exist
     * already.</p>
     *
     * @param origin the origin
     * @param sequenceBlueprint the sequence blueprint
     * @return the updated origin, or the original if it did not get added
     */
    public Origin put(final Origin origin, final SequenceBlueprint<T> sequenceBlueprint) {
        final Origin updatedOrigin = Origin.from(origin).root(sequenceBlueprint).build();

        if (this.registry.containsKey(sequenceBlueprint.getClass())) return updatedOrigin;
        this.registry.put(sequenceBlueprint.getClass(), sequenceBlueprint);

        return updatedOrigin;
    }

    /**
     * Returns the {@link SequenceBlueprint} from this registry using
     * the corresponding key.
     *
     * @param key the key
     * @return the sequence blueprint
     */
    public Optional<SequenceBlueprint<T>> get(final Class<?> key) {
        if (!this.registry.containsKey(key)) return Optional.empty();
        return Optional.ofNullable(this.registry.get(key));
    }

    /**
     * Returns the {@link Class} key that represents its {@link SequenceBlueprint}.
     *
     * @param sequenceBlueprint the blueprint
     * @return the sequence key, or {@code null} if they check is not contained in this registry
     */
    public Class<?> key(final SequenceBlueprint<T> sequenceBlueprint) {
        return this.registry.inverse().get(sequenceBlueprint);
    }

    /**
     * Returns the key that represents its {@link SequenceBlueprint}.
     *
     * @return the sequence key, or {@code null} if they check is not contained in this registry
     */
    public Set<Class<?>> keySet() {
        return this.registry.keySet();
    }

    @Override
    public Iterator<SequenceBlueprint<T>> iterator() {
        return this.registry.values().iterator();
    }
}
