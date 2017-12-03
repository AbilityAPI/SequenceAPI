package com.abilityapi.sequenceapi;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SequenceContext {

    public static String ID = "uniqueId";
    public static String ROOT = "root";
    public static String SOURCE = "source";
    public static String OWNER = "owner";
    public static String STATE = "state";

    public static Builder builder() {
        return new Builder();
    }

    public static Builder from(final SequenceContext sequenceContext) {
        return new Builder(sequenceContext);
    }

    private final ImmutableMap<String, Object> context;

    private SequenceContext(final Map<String, Object> context) {
        this.context = ImmutableMap.copyOf(context);
    }

    @SuppressWarnings("unchecked")
    public final <T> T getId() {
        return (T) this.context.get(ID);
    }

    @SuppressWarnings("unchecked")
    public final <T> T getRoot() {
        return (T) this.context.get(ROOT);
    }

    @SuppressWarnings("unchecked")
    public final <T> T getSource() {
        return (T) this.context.get(SOURCE);
    }

    @SuppressWarnings("unchecked")
    public final <T> T getOwner() {
        return (T) this.context.get(OWNER);
    }

    @SuppressWarnings("unchecked")
    public final <T> T getState() {
        return (T) this.context.get(STATE);
    }

    @SuppressWarnings("unchecked")
    public final <T> T get(final String key) {
        return (T) this.context.get(key);
    }

    public static class Builder {

        Map<String, Object> context = new HashMap<>();
        Set<String> keysUsed = new HashSet<>();

        private Builder() {}

        private Builder(final SequenceContext sequenceContext) {
            merge(sequenceContext);
        }

        public final Builder merge(final SequenceContext sequenceContext) {
            sequenceContext.context.forEach(this::custom);
            return this;
        }

        public final Builder id(final UUID value) {
            if (!this.keysUsed.contains(SequenceContext.ID)) {
                this.context.put(SequenceContext.ID, value);
                this.keysUsed.add(SequenceContext.ID);
            }
            return this;
        }

        public final Builder root(final Object value) {
            if (!this.keysUsed.contains(SequenceContext.ROOT)) {
                this.context.put(SequenceContext.ROOT, value);
                this.keysUsed.add(SequenceContext.ROOT);
            }
            return this;
        }

        public final Builder source(final Object value) {
            if (!this.keysUsed.contains(SequenceContext.SOURCE)) {
                this.context.put(SequenceContext.SOURCE, value);
                this.keysUsed.add(SequenceContext.SOURCE);
            }
            return this;
        }

        public final Builder owner(final Object value) {
            if (!this.keysUsed.contains(SequenceContext.OWNER)) {
                this.context.put(SequenceContext.OWNER, value);
                this.keysUsed.add(SequenceContext.OWNER);
            }
            return this;
        }

        public final Builder state(final Object value) {
            if (!this.keysUsed.contains(SequenceContext.STATE)) {
                this.context.put(SequenceContext.STATE, value);
                this.keysUsed.add(SequenceContext.STATE);
            }
            return this;
        }

        public final Builder custom(final String key, final Object value) {
            if (!this.keysUsed.contains(key)) {
                this.context.put(key, value);
                this.keysUsed.add(key);
            }
            return this;
        }

        public final SequenceContext build() {
            return new SequenceContext(this.context);
        }

    }

}
