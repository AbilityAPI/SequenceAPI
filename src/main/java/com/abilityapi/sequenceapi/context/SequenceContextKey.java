package com.abilityapi.sequenceapi.context;

import java.util.Objects;

public class SequenceContextKey {

    public static SequenceContextKey of(final String id, final Object defaultValue) {
        return new SequenceContextKey(id, defaultValue);
    }

    private final String id;
    private final Object defaultValue;

    public SequenceContextKey(final String id, Object defaultValue) {
        this.id = id;
        this.defaultValue = defaultValue;
    }

    public final String getId() {
        return this.id;
    }

    public final Object getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.defaultValue);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        final SequenceContextKey other = (SequenceContextKey) object;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.defaultValue, other.getDefaultValue());
    }

}
