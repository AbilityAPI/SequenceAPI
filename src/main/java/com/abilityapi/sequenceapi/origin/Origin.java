package com.abilityapi.sequenceapi.origin;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Origin {

    public static OriginKey ROOT = OriginKey.of("root", null);

    public static OriginKey SOURCE = OriginKey.of("source", null);

    public static OriginKey OWNER = OriginKey.of("owner", null);

    public static OriginKey STATE = OriginKey.of("state", null);

    public static Builder builder() {
        return new Builder();
    }

    public static Builder from(Origin origin) {
        return new Builder(origin);
    }

    private final ImmutableMap<OriginKey, Object> origins;

    private Origin(Map<OriginKey, Object> origins) {
        this.origins = ImmutableMap.copyOf(origins);
    }

    public final Object getRoot() {
        return this.origins.get(ROOT);
    }

    public final Object getSource() {
        return this.origins.get(SOURCE);
    }

    public final Object getOwner() {
        return this.origins.get(OWNER);
    }

    public final Object getState() {
        return this.origins.get(STATE);
    }

    public final Object get(final OriginKey key) {
        return this.origins.get(key);
    }

    public static class Builder {

        Map<OriginKey, Object> origins = new HashMap<>();
        Set<String> keysUsed = new HashSet<>();

        private Builder() {}

        private Builder(final Origin origin) {
            origin.origins.forEach(this::custom);
        }

        Builder root(final Object value) {
            if (!this.keysUsed.contains(Origin.ROOT.getId())) {
                this.origins.put(Origin.ROOT, value);
                this.keysUsed.add(Origin.ROOT.getId());
            }
            return this;
        }

        Builder source(final Object value) {
            if (!this.keysUsed.contains(Origin.SOURCE.getId())) {
                this.origins.put(Origin.SOURCE, value);
                this.keysUsed.add(Origin.SOURCE.getId());
            }
            return this;
        }

        Builder owner(final Object value) {
            if (!this.keysUsed.contains(Origin.OWNER.getId())) {
                this.origins.put(Origin.OWNER, value);
                this.keysUsed.add(Origin.OWNER.getId());
            }
            return this;
        }

        Builder state(final Object value) {
            if (!this.keysUsed.contains(Origin.STATE.getId())) {
                this.origins.put(Origin.STATE, value);
                this.keysUsed.add(Origin.STATE.getId());
            }
            return this;
        }

        Builder custom(final OriginKey key, final Object value) {
            if (!this.keysUsed.contains(key.getId())) {
                this.origins.put(key, value);
                this.keysUsed.add(key.getId());
            }
            return this;
        }

        Origin build() {
            return new Origin(this.origins);
        }

    }

}
