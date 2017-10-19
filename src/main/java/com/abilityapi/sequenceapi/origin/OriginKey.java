package com.abilityapi.sequenceapi.origin;

import java.util.Objects;

public class OriginKey {

    public static OriginKey of(final String id, final Object defaultValue) {
        return new OriginKey(id, defaultValue);
    }

    private final String id;
    private final Object defaultValue;

    public OriginKey(final String id, Object defaultValue) {
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
        final OriginKey other = (OriginKey) object;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.defaultValue, other.getDefaultValue());
    }

}
