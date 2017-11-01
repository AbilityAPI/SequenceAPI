package com.abilityapi.sequenceapi.context;

import com.google.common.collect.ImmutableMap;

import java.util.*;

public class SequenceContext {

    public static SequenceContextKey ID = new SequenceContextKey("unique_key", null);
    public static SequenceContextKey ROOT = new SequenceContextKey("root", null);
    public static SequenceContextKey SOURCE = new SequenceContextKey("source", null);
    public static SequenceContextKey OWNER = new SequenceContextKey("owner", null);
    public static SequenceContextKey STATE = new SequenceContextKey("state", null);

    public static Builder builder() {
        return new Builder();
    }

    public static Builder from(SequenceContext sequenceContext) {
        return new Builder(sequenceContext);
    }

    private final ImmutableMap<SequenceContextKey, Object> context;

    private SequenceContext(Map<SequenceContextKey, Object> context) {
        this.context = ImmutableMap.copyOf(context);
    }

    public final Object getId() {
        return this.context.get(ID);
    }

    public final Object getRoot() {
        return this.context.get(ROOT);
    }

    public final Object getSource() {
        return this.context.get(SOURCE);
    }

    public final Object getOwner() {
        return this.context.get(OWNER);
    }

    public final Object getState() {
        return this.context.get(STATE);
    }

    public final Object get(final SequenceContextKey key) {
        return this.context.get(key);
    }

    public static class Builder {

        Map<SequenceContextKey, Object> context = new HashMap<>();
        Set<String> keysUsed = new HashSet<>();

        private Builder() {}

        private Builder(final SequenceContext sequenceContext) {
            merge(sequenceContext);
        }

        public Builder merge(final SequenceContext sequenceContext) {
            sequenceContext.context.forEach(this::custom);
            return this;
        }

        public Builder id(final UUID value) {
            if (!this.keysUsed.contains(SequenceContext.ID.getId())) {
                this.context.put(SequenceContext.ID, value);
                this.keysUsed.add(SequenceContext.ID.getId());
            }
            return this;
        }

        public Builder root(final Object value) {
            if (!this.keysUsed.contains(SequenceContext.ROOT.getId())) {
                this.context.put(SequenceContext.ROOT, value);
                this.keysUsed.add(SequenceContext.ROOT.getId());
            }
            return this;
        }

        public Builder source(final Object value) {
            if (!this.keysUsed.contains(SequenceContext.SOURCE.getId())) {
                this.context.put(SequenceContext.SOURCE, value);
                this.keysUsed.add(SequenceContext.SOURCE.getId());
            }
            return this;
        }

        public Builder owner(final Object value) {
            if (!this.keysUsed.contains(SequenceContext.OWNER.getId())) {
                this.context.put(SequenceContext.OWNER, value);
                this.keysUsed.add(SequenceContext.OWNER.getId());
            }
            return this;
        }

        public Builder state(final Object value) {
            if (!this.keysUsed.contains(SequenceContext.STATE.getId())) {
                this.context.put(SequenceContext.STATE, value);
                this.keysUsed.add(SequenceContext.STATE.getId());
            }
            return this;
        }

        public Builder custom(final SequenceContextKey key, final Object value) {
            if (!this.keysUsed.contains(key.getId())) {
                this.context.put(key, value);
                this.keysUsed.add(key.getId());
            }
            return this;
        }

        public SequenceContext build() {
            return new SequenceContext(this.context);
        }

    }

}
